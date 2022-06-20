package com.example.waikiki.camera

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.waikiki.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PermissionsFragment(myContent: Int) : BottomSheetDialogFragment() {

    val PERMISSION_REQUIRED = if(myContent==1) Manifest.permission.CAMERA else Manifest.permission.CAPTURE_AUDIO_OUTPUT
    val PERMISSION_REQUEST_CODE = 10
    val thisContent = myContent

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if(myContent ==1) {
                     if (isGranted) {
                        startCamera()
                    } else {
                        notPermishen()
                    }
            } else {}
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(requireContext(), PERMISSION_REQUIRED) != PERMISSION_GRANTED) {
            launchCameraPermissionRequest()
        } else {
            if(thisContent ==1) {
                startCamera()
            } else {}
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)
                if(thisContent ==1) {
                    startCamera()
                } else {}
            else {
                Toast.makeText(context, R.string.text_explanation_permision, Toast.LENGTH_LONG).show()
            }
            return
        }
    }

    private fun launchCameraPermissionRequest() {
        requestPermissionLauncher.launch(PERMISSION_REQUIRED)
    }





//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {

//        binding= ActivityMainBinding.bind(inflater.inflate(R.layout.activity_main,container))
            //FragmentPermissionsBinding.bind(inflater.inflate(R.layout.fragment_permissions,container))
//        return binding.root
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

        //if(ContextCompat.checkSelfPermission(this.requireContext(),pp)==PERMISSION_GRANTED) {
        //    startCamera()
        //} else {
        //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //        if (shouldShowRequestPermissionRationale(pp)) {
        //            onShouldShowRationale()
        //        } else {
        //            launchCameraPermissionRequest()
        //        }
        //    } else {
        //        launchCameraPermissionRequest()
        //    }
        //}
    //}

    //private fun onShouldShowRationale() {
    //    val dialog = MaterialAlertDialogBuilder(this.requireContext())
    //        .setTitle(R.string.text_permission_request)
    //        .setIcon(R.drawable.warning)
    //        .setMessage(R.string.text_explanation_permision)
    //        .setPositiveButton(R.string.but_yes_txt) { dialog, _ ->
    //            launchCameraPermissionRequest()
    //        }
    //        .create()
    //    dialog.show()
    //}
    //private fun onPermanentPermissionDenied() {
    //    val dialog = MaterialAlertDialogBuilder(this.requireContext())
    //        .setTitle(R.string.text_permission_request)
    //        .setIcon(R.drawable.warning)
    //        .setMessage(R.string.text_explanation_permision)
    //        .setPositiveButton(R.string.but_yes_txt) { dialog, _ ->
    //            launchCameraPermissionRequest()
    //        }
    //        .setNegativeButton(R.string.but_no_txt) { dialog, _ ->
    //            notPermishen()
    //        }
    //        .create()
    //    dialog.show()
    //}


    private fun startCamera(){
        (activity as CameraActivity).supportFragmentManager.beginTransaction()
            .addToBackStack("Camera")
            .add(R.id.fragmentCameraContainerView, CameraPreviewFragment())
            .commit()
        dismiss()
    }
    private fun notPermishen(){
        dismiss()
    }
}