package com.example.waikiki.camera

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.commit
import com.example.constants.KeyConstants.CAMERA
import com.example.constants.KeyConstants.INFOGALLERY
import com.example.constants.KeyConstants.INFOGALLERYBROWSE
import com.example.constants.KeyConstants.MYGALLERY
import com.example.dao.viewmodel.MediaView
import com.example.dao.viewmodel.TopicViewModel
import com.example.waikiki.R
import com.example.waikiki.databinding.ActivityCameraBinding
import java.io.File

lateinit var binding: ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val oper = intent.getStringExtra("oper")

        MediaView.bindingActivity= binding
        binding.textApp.text = TopicViewModel.currentTopicName

        MediaView.media_oper = oper==MYGALLERY || oper== CAMERA
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        when(oper) {
            MYGALLERY -> startMyGallery()
            INFOGALLERY -> startInfoGallery()
            INFOGALLERYBROWSE -> startInfoGalleryRow()
        else -> startCamera()
        }

        binding.imageClose.setOnClickListener() {
            closeCamera()
        }
    }

    fun closeCamera() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        if (MediaView.media_frag != 0) {
            binding.appBar.visibility= VISIBLE
            when(MediaView.media_frag) {
                3 -> {
                    MediaView.media_frag = 0
                    supportFragmentManager.popBackStack("InfoGallery", 0)
                    }
                4 -> {
                    MediaView.media_frag = 0
                    supportFragmentManager.popBackStack("InfoGalleryRow", 0)
                }
                else -> {
                    MediaView.media_frag = 0
                    supportFragmentManager.popBackStack("MyGallery", 0)
                }
            }
        } else {
            if (MediaView.media_oper) {

                if (MediaView.media_patch == 2) {
                    MediaView.media_patch = 1
                    supportFragmentManager.popBackStack("Camera", 0)
                } else finish()
            } else finish()
        }
    }

    fun startPhotoFragment() {
        MediaView.media_frag=MediaView.media_patch
        supportFragmentManager.beginTransaction()
            .addToBackStack("MyPhoto")
            .add(R.id.fragmentCameraContainerView, PhotoFragment())
            .commit()
    }

    fun startMyGallery() {
        supportFragmentManager.beginTransaction()
            .addToBackStack("MyGallery")
            .add(R.id.fragmentCameraContainerView, CameraGalleryFragment())
            .commit()
    }

    fun startInfoGallery() {
        supportFragmentManager.commit() {
            addToBackStack("InfoGallery")
            add(R.id.fragmentCameraContainerView, InfoGalleryFragment(),"InfoGallery")
        }
    }

    fun startInfoGalleryRow() {
        supportFragmentManager.commit() {
            setReorderingAllowed(true)
            addToBackStack("InfoGalleryRow")
            add(R.id.fragmentCameraContainerView, InfoGalleryRowFragment(),"InfoGalleryRow")
        }
    }

    fun startCamera() {
        PermissionsFragment(1).show(supportFragmentManager,"Permis")
    }

    override fun onBackPressed() {
            closeCamera()
    }

    fun hideSystemUI(hide: Boolean) {
        if(hide) {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(
                window,
                binding.fragmentCameraContainerView
            ).let { controller ->
                controller.hide(WindowInsetsCompat.Type.statusBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(
                window,
                binding.fragmentCameraContainerView
            ).let { controller ->
                controller.show(WindowInsetsCompat.Type.statusBars())
             }
        }
    }

    companion object {
        /** Use external media if it is available, our app's file directory otherwise */ //getExternalFilesDirs("Media")
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.getExternalFilesDir(null).let {
                File(it,  appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }

    }

}