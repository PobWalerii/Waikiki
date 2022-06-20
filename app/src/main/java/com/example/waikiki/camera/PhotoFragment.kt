package com.example.waikiki.camera

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.*
import android.view.View.*
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.dao.viewmodel.MediaView
import com.example.waikiki.BuildConfig
import com.example.waikiki.R
import com.example.waikiki.databinding.FragmentPhotoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PhotoFragment : Fragment() {

    lateinit var binding: FragmentPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPhotoBinding.bind(inflater.inflate(R.layout.fragment_photo,container,false))
        return binding.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            (activity as CameraActivity).hideSystemUI(true)
            MediaView.bindingActivity.appBar.visibility=GONE
            binding.bottomBar.visibility=GONE
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            (activity as CameraActivity).hideSystemUI(false)
            MediaView.bindingActivity.appBar.visibility=VISIBLE
            binding.bottomBar.visibility= VISIBLE
        }
//        updateFragmentUi()
    }

    /*
    fun updateFragmentUi() {
        Log.d("tag1","Выполнен")
        binding.root.let {
            binding.root.removeView(it)
        }
        binding = FragmentPhotoBinding.inflate(
            LayoutInflater.from(requireContext()),
            binding.root,
            true
        )
    }
*/


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CameraActivity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER)
        val itemPhoto = binding.imageView
        Glide.with(itemPhoto).load(MediaView.currentFile).into(itemPhoto as ImageView)

        with(binding) {
            if(MediaView.media_frag==4) {   // ЧИСТЫЙ ПРОСМОТР
                buttonAdd.visibility = GONE
                buttonRemove.visibility = GONE
                buttonDel.visibility = GONE
            } else {  // возможность редактирования
                if (MediaView.isFileInList(MediaView.currentFile)) {
                    buttonAdd.visibility = GONE
                    buttonRemove.visibility = VISIBLE
                } else {
                    buttonAdd.visibility = VISIBLE
                    buttonRemove.visibility = GONE
                }
            }

            buttonAdd.setOnClickListener() {
                it.isEnabled=false
                addToCurrent(it)
            }
            buttonRemove.setOnClickListener() {
                it.isEnabled=false
                removeFromCurrent(it)
            }
            buttonShare.setOnClickListener() {
                it.isEnabled=false
                sharePhoto(view)
            }
            buttonDel.setOnClickListener() {
                it.isEnabled=false
                deletePhoto(it)
            }
        }
    }

    fun addToCurrent(but: View) {   //только общая галерея
        val current = MediaView.currentFile
        var myPhoto = MediaView.myPhoto
        myPhoto.add(current)
        but.isEnabled=true
        binding.buttonAdd.visibility = GONE
        binding.buttonRemove.visibility = VISIBLE
        MediaView.adapterGallery.notifyDataSetChanged()
        MediaView.adapterGaleryMini.notifyDataSetChanged()
    }

    fun removeFromCurrent(but: View) {
        val current = MediaView.currentFile
        var myPhoto = MediaView.myPhoto
        if(MediaView.media_patch==1) {  //удаляем отметку в большой
            while(myPhoto.remove(current))
            MediaView.adapterGallery.notifyDataSetChanged()
            MediaView.adapterGaleryMini.notifyDataSetChanged()
            binding.buttonAdd.visibility = VISIBLE
            binding.buttonRemove.visibility = GONE
        }
        else {  // в текущей //////////////////////////////////////////
            val position = MediaView.isFileInListPosition()
            myPhoto.removeAt(position)
            MediaView.adapterGalleryInfo.removePhoto(position)
            MediaView.adapterGaleryMini.notifyDataSetChanged()
            (activity as CameraActivity).closeCamera()
        }
        but.isEnabled=true
    }

    fun sharePhoto(view: View) {
        var photoList = if(MediaView.media_patch==1) MediaView.mediaList else MediaView.myPhoto
        photoList.getOrNull(MediaView.currentPosition)?.let { mediaFile ->
            // Create a sharing intent
            val intent = Intent().apply {
                // Infer media type from file extension
                val mediaType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(mediaFile.extension)
                // Get URI from our FileProvider implementation
                val uri = FileProvider.getUriForFile(
                    view.context,
                    BuildConfig.APPLICATION_ID+".provider",
                    mediaFile)
                // Set the appropriate intent extra, type, action and flags
                putExtra(Intent.EXTRA_STREAM, uri)
                type = mediaType
                action = Intent.ACTION_SEND
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            // Launch the intent letting the user choose which app to share with
            startActivity(Intent.createChooser(intent, getString(R.string.share_hint)))
        }
        binding.buttonShare.isEnabled=true
    }

    fun deletePhoto(but: View) {
        //var photoList = if(MediaView.media_patch==1) MediaView.mediaList else MediaView.myPhoto
        //photoList.getOrNull(MediaView.currentPosition)?.let { mediaFile ->
            val current = MediaView.currentFile
            var myPhoto = MediaView.myPhoto
            val dialog = MaterialAlertDialogBuilder(but.context)
                .setTitle(R.string.but_delete_txt)
                .setIcon(R.drawable.warning)
                .setMessage(R.string.txt_delete_photo)
                .setPositiveButton(R.string.but_yes_txt) { dialog, _ ->
                    // Delete current photo
                    current.delete()
                    // Send relevant broadcast to notify other apps of deletion
                    MediaScannerConnection.scanFile(
                        but.context, arrayOf(current.absolutePath), null, null)

                    if(MediaView.media_patch==1) {  //удаляем отметку в большой
                        MediaView.mediaList.removeAt(MediaView.currentPosition)
                        while(myPhoto.remove(current))
                        MediaView.adapterGallery.notifyDataSetChanged()
                        MediaView.adapterGaleryMini.notifyDataSetChanged()
                    }
                    else {  // в текущей //////////////////////////////////////////
                        val position = MediaView.isFileInListPosition()
                        myPhoto.removeAt(position)
                        MediaView.adapterGalleryInfo.removePhoto(position)
                        MediaView.adapterGaleryMini.notifyDataSetChanged()
                    }

                    but.isEnabled=true
                    (activity as CameraActivity).closeCamera()
                }
                .setNegativeButton(R.string.but_no_txt) { dialog, _ ->
                    but.isEnabled=true
                }
                .create()
            dialog.show()
        //}
    }
}