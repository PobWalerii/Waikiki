package com.example.waikiki.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.adapters.GalleryAdapter
import com.example.dao.viewmodel.MediaView
import com.example.waikiki.R
import com.example.waikiki.databinding.FragmentCameraGalleryBinding
import java.io.File

class CameraGalleryFragment : Fragment() {

    lateinit var binding_: View
    lateinit var binding: FragmentCameraGalleryBinding
    lateinit var outputDirectory: File

    lateinit var adapter: GalleryAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var layerManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_ = inflater.inflate(R.layout.fragment_camera_gallery, container, false)
        binding = FragmentCameraGalleryBinding.bind(binding_)

        outputDirectory = CameraActivity.getOutputDirectory(requireContext())

        MediaView.mediaList = outputDirectory.listFiles().sortedDescending().toMutableList()

        recyclerView = binding.recyclerView
        adapter = GalleryAdapter()
        MediaView.adapterGallery = adapter
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        layerManager = binding.recyclerView.layoutManager as LinearLayoutManager

        adapter.setOnItemClickListener(
        object : GalleryAdapter.OnItemClickListener {
            override fun onItemClick() {
                if(MediaView.media_frag == 0) (activity as CameraActivity).startPhotoFragment()
            }
        })
        return binding_
    }

    override fun onResume() {
        super.onResume()
        MediaView.bindingActivity.textApp.text=getText(R.string.my_full_gallery)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}