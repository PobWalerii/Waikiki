package com.example.waikiki.camera

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.camera.core.*
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.CameraSelector.LENS_FACING_FRONT
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dao.viewmodel.MediaView
import com.example.waikiki.R
import com.example.waikiki.databinding.FragmentCameraPreviewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

const val ANIMATION_FAST_MILLIS = 50L
const val ANIMATION_SLOW_MILLIS = 100L
class CameraPreviewFragment : Fragment() {
    val EXTENSION_WHITELIST = arrayOf("JPG")
    lateinit var binding_: View
    lateinit var binding: FragmentCameraPreviewBinding
    lateinit var imageCapture: ImageCapture
    lateinit var outputDirectory: File
    lateinit var cameraSelector: CameraSelector
    var lensFacing: Int = LENS_FACING_BACK
    private var cameraProvider: ProcessCameraProvider? = null
    private var displayId: Int = -1
    private var preview: Preview? = null
    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_=inflater.inflate(R.layout.fragment_camera_preview, container, false)
        binding= FragmentCameraPreviewBinding.bind(binding_)
        return binding_
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()
        // Determine the output directory
        outputDirectory = CameraActivity.getOutputDirectory(requireContext())
        // Wait for the views to be properly laid out
        binding.viewFinder.post {
            // Keep track of the display in which this view is attached
            displayId = binding.viewFinder.display.displayId
            // Set up the camera and its use cases
            setUpCamera()
        }

        setBackGroundGallery()

        binding.cameraSwitchButton.let {
            it.isEnabled = false
            it.setOnClickListener() {
                lensFacing = if(lensFacing==LENS_FACING_FRONT) LENS_FACING_BACK else LENS_FACING_FRONT
                setUpCamera()
            }
        }
        binding.cameraCaptureButton.setOnClickListener() {
            onPhotoClick()
        }

        binding.photoViewButton.setOnClickListener() {
            // Only navigate when the gallery has photos
            if (outputDirectory.listFiles()?.isNotEmpty() == true) {
                //var media = outputDirectory.listFiles()
                MediaView.media_patch=2
                (activity as CameraActivity).startMyGallery()
            }
        }

    }
    //Initialize CameraX, and prepare to bind the camera use cases
    fun setUpCamera() {
        var cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            // Camera provider is now guaranteed to be available
            cameraProvider = cameraProviderFuture.get()
            // Enable or disable switching between cameras
            updateCameraSwitchButton()
            // Build and bind the camera use cases
            bindCameraUseCases()
        },ContextCompat.getMainExecutor(requireContext()))

    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases() {
        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")
        // Choose the camera by requiring a lens facing
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
        // Set up the preview use case to display camera preview.
        preview = Preview.Builder().build()
        // Set up the capture use case to allow users to take photos.
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            //.setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)

            // We request aspect ratio but no resolution to match preview config, but letting
            // CameraX optimize for whatever specific resolution best fits our use cases
            //.setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            .setTargetRotation(view?.display!!.rotation)
            .build()

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()
        // Attach use cases to the camera with the same lifecycle owner
        val camera = cameraProvider?.bindToLifecycle(
            this as LifecycleOwner, cameraSelector, preview, imageCapture)
        // Connect the preview use case to the previewView
        preview?.setSurfaceProvider(
            binding.viewFinder.getSurfaceProvider())


        val orientationEventListener = object : OrientationEventListener(requireContext()) {
            override fun onOrientationChanged(orientation : Int) {
                // Monitors orientation values to determine the target rotation value
                val rotation : Int = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture.targetRotation = rotation
            }
        }
        orientationEventListener.enable()
    }

    /** Enabled or disabled a button to switch cameras depending on the available cameras */
    private fun updateCameraSwitchButton() {
        binding.cameraSwitchButton.isEnabled =
            if(hasBackCamera() && hasFrontCamera()) true
            else false
    }
    /** Returns true if the device has an available back camera. False otherwise */
    private fun hasBackCamera(): Boolean {
        //return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
        return requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }
    /** Returns true if the device has an available front camera. False otherwise */
    private fun hasFrontCamera(): Boolean {
        //return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
        return requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
    }

    // In the background, load latest photo taken (if any) for gallery thumbnail
    fun setBackGroundGallery() {
        lifecycleScope.launch(Dispatchers.IO) {
            outputDirectory.listFiles { file ->
                EXTENSION_WHITELIST.contains(file.extension.uppercase(Locale.ROOT))
            }?.maxOrNull()?.let {
                setGalleryThumbnail(Uri.fromFile(it))
            }
        }
    }
    private fun setGalleryThumbnail(uri: Uri) {
        // Run the operations in the view's thread
        binding.photoViewButton.let { photoViewButton ->
            photoViewButton.post {
                // Remove thumbnail padding
                photoViewButton.setPadding(resources.getDimension(R.dimen.stroke_small).toInt())
                // Load thumbnail into circular button using Glide
                Glide.with(photoViewButton)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(photoViewButton)
            }
        }
    }

    private fun onPhotoClick() {

        // Create output file to hold the image
        val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)

        // Setup image capture metadata
        val metadata = ImageCapture.Metadata().apply {
            // Mirror image when using the front camera
            isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
        }
        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .setMetadata(metadata)
            .build()
        imageCapture.takePicture(
            outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("tag1", "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    Log.d("tag1", "Photo capture succeeded: $savedUri")

                    // We can only change the foreground Drawable using API level 23+ API
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // Update the gallery thumbnail with latest picture taken
                        setGalleryThumbnail(savedUri)
                    }

/*
                    // If the folder selected is an external media directory, this is
                    // unnecessary but otherwise other apps will not be able to access our
                    // images unless we scan them using [MediaScannerConnection]
                    val mimeType = MimeTypeMap.getSingleton()
                        .getMimeTypeFromExtension(savedUri.toFile().extension)
                    MediaScannerConnection.scanFile(
                        context,
                        arrayOf(savedUri.toFile().absolutePath),
                        arrayOf(mimeType)
                    ) { _, uri ->
                        Log.d("tag1", "Image capture scanned into media store: $uri")
                    }
*/

                }
            })

        // We can only change the foreground Drawable using API level 23+ API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Display flash animation to indicate that photo was captured
            binding.root.postDelayed({
                binding.root.foreground = ColorDrawable(Color.WHITE)
                binding.root.postDelayed(
                    { binding.root.foreground = null }, ANIMATION_FAST_MILLIS)
            }, ANIMATION_SLOW_MILLIS)
        }

/*
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException)
                {
                    // insert your code here.
                }
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                    setGalleryThumbnail(savedUri)
                }
            })

 */
    }


    companion object {

    private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
    private const val PHOTO_EXTENSION = ".jpg"
    private const val RATIO_4_3_VALUE = 4.0 / 3.0
    private const val RATIO_16_9_VALUE = 16.0 / 9.0

    /** Helper function used to create a timestamped file */
    private fun createFile(baseFolder: File, format: String, extension: String) =
        File(baseFolder, SimpleDateFormat(format, Locale.US)
            .format(System.currentTimeMillis()) + extension)
    }

}
