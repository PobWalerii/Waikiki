package com.example.waikiki.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.adapters.GalleryAdapterRow
import com.example.dao.viewmodel.MediaView
import com.example.waikiki.R
import com.example.waikiki.databinding.FragmentGalleryRowBinding

class InfoGalleryRowFragment() : Fragment() {

    lateinit var binding_: View
    lateinit var binding: FragmentGalleryRowBinding

    lateinit var adapter: GalleryAdapterRow
    lateinit var recyclerView: RecyclerView
    lateinit var layerManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_ = inflater.inflate(R.layout.fragment_gallery_row, container, false)
        binding = FragmentGalleryRowBinding.bind(binding_)

        recyclerView = binding.recyclerView
        adapter = GalleryAdapterRow()
        recyclerView.adapter = adapter
        //recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
        layerManager = binding.recyclerView.layoutManager as LinearLayoutManager

        adapter.setOnItemClickListener(
        object : GalleryAdapterRow.OnItemClickListener {
            override fun onItemClick() {
                if(MediaView.media_frag == 0) {
                    MediaView.media_patch = 4  //галерея просмотр

                    (activity as CameraActivity).startPhotoFragment()
                }
            }
        })
        return binding_
    }

    override fun onResume() {
        super.onResume()
        MediaView.bindingActivity.textApp.text=getText(R.string.current_gallery)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}