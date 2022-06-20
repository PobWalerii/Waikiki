package com.example.dao.viewmodel

import androidx.fragment.app.FragmentManager
import com.example.dao.adapters.GalleryAdapter
import com.example.dao.adapters.GalleryAdapterInfo
import com.example.dao.adapters.GalleryAdapterMini
import com.example.waikiki.databinding.ActivityCameraBinding
import com.example.waikiki.databinding.PhotoItemBinding
import java.io.File

class MediaView {
    companion object {
        var media_patch = 0
        var media_frag = 0
        var media_oper = false
        lateinit var adapterGallery: GalleryAdapter
        lateinit var adapterGalleryInfo: GalleryAdapterInfo
        lateinit var adapterGaleryMini: GalleryAdapterMini
        lateinit var mediaList: MutableList<File>
        lateinit var currentFile: File
        var currentPosition = 0
        lateinit var bindingActivity: ActivityCameraBinding
        var myPhoto = mutableListOf<File>()
        lateinit var bindingCurrenGalleryItem: PhotoItemBinding
        var photoList = ""


        fun isFileInList(current: File): Boolean {
            for(f in myPhoto) if(f== current) return true
            return false
        }
        fun isFileInListPosition(): Int {
            var i = 0
            for(f in myPhoto) {
                if (f == currentFile) return i
                else i++
            }
            return -1
        }
    }
}