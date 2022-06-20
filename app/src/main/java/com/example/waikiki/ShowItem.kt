package com.example.waikiki

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.example.constants.KeyConstants
import com.example.dao.viewmodel.MediaView
import com.example.waikiki.camera.CameraActivity
import com.example.waikiki.databinding.ContentItemBinding
import java.io.File

    fun showItem(binding: ContentItemBinding, isCurrent: Boolean,
                 content: String,
                 photoList: String,
                 comment: String) {

        var card = binding.cardText
        if(content.isNullOrBlank())
            card.visibility= GONE
        else {
            card.visibility= VISIBLE
            //binding.text.text = content
            binding.text.setText(content)
        }

        card = binding.cardPhoto
        if(photoList.isNullOrEmpty())
            card.visibility= GONE
        else {
            var photo = photoList.split(",")
            var myPhoto = mutableListOf<File>()
            if (!photo.isNullOrEmpty()) {
                for (i in photo) {
                    if (i.isNotEmpty() && File(i).isFile) {
                        myPhoto.add(File(i))
                    }
                }
                var kk = myPhoto.size
                for (k in 1..7) {
                    var image = when (k) {
                        2 -> binding.imageView1
                        3 -> binding.imageView2
                        4 -> binding.imageView3
                        5 -> binding.imageView4
                        6 -> binding.imageView5
                        7 -> binding.imageView6
                        else -> binding.imageView0
                    }
                    var file = if(k<=kk) myPhoto[k-1] else File("")
                    Glide.with(image).load(file).into(image as ImageView)
                }
                card.visibility = if(myPhoto.size==0) GONE else VISIBLE
            }
        }

        card = binding.cardComment
        if(comment.isNullOrBlank())
            card.visibility= GONE
        else {
            card.visibility= VISIBLE
            binding.textComment.text = comment
        }

        val backcolor = if(isCurrent) R.color.gray else R.color.white
        binding.container.setCardBackgroundColor(
            ContextCompat.getColor(
                binding.container.context,
                backcolor
            )
        )
    }

    fun showItemPhotoGallery(context: Context,activity: Activity) {
        var photo = MediaView.photoList.split(",")
        MediaView.myPhoto.clear()
        if (!photo.isNullOrEmpty()) {
            for (i in photo) {
                if (i.isNotEmpty() && File(i).isFile)
                    MediaView.myPhoto.add(File(i))
            }
        }
        if(MediaView.myPhoto.size!=0) {
            MediaView.media_patch = 1
            val intent = Intent(context, CameraActivity::class.java)
            with(intent) {
                putExtra("oper", KeyConstants.INFOGALLERYBROWSE)
                startActivity(context, this, null)
            }
            activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }
    }
