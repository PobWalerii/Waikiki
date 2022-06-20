package com.example.waikiki.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.adapters.GalleryAdapterInfo
import com.example.dao.viewmodel.MediaView
import com.example.dao.viewmodel.Photo
import com.example.waikiki.R
import com.example.waikiki.databinding.FragmentCameraGalleryBinding

class InfoGalleryFragment() : Fragment() {

    lateinit var binding_: View
    lateinit var binding: FragmentCameraGalleryBinding

    lateinit var adapter: GalleryAdapterInfo
    lateinit var recyclerView: RecyclerView

    private val itemTouchHelper by lazy {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END,0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val recyclerviewAdapter = recyclerView.adapter as GalleryAdapterInfo
                var startPosition = viewHolder.adapterPosition
                var endPosition = target.adapterPosition
                recyclerviewAdapter.moveItem(startPosition, endPosition)
                recyclerviewAdapter.notifyItemMoved(startPosition, endPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.scaleY = 1.2f
                    viewHolder?.itemView?.scaleX = 1.2f
                    viewHolder?.itemView?.alpha = 0.7f
                }
            }
            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.scaleX = 1.0f
                viewHolder.itemView.scaleY = 1.0f
                viewHolder?.itemView?.alpha = 1.0f
            }
        }
        ItemTouchHelper(itemTouchCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_ = inflater.inflate(R.layout.fragment_camera_gallery, container, false)
        binding = FragmentCameraGalleryBinding.bind(binding_)

        recyclerView = binding.recyclerView
        //var countRow = if(MediaView.myPhoto.size<3) 1 else 2
        adapter = GalleryAdapterInfo()
        MediaView.adapterGalleryInfo = adapter
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //GridLayoutManager(requireContext(), 1)// countRow)
        //recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
        //layerManager = binding.recyclerView.layoutManager as LinearLayoutManager
        itemTouchHelper.attachToRecyclerView(recyclerView)

        adapter.differ.submitList(getPhoto())

        adapter.setOnItemClickListener(
            object : GalleryAdapterInfo.OnItemClickListener {
                override fun onItemClick() {
                    if(MediaView.media_frag == 0) {
                        MediaView.media_patch = 3  //галерея редактирования
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

    private fun getPhoto() : List<Photo>{
        val movPhoto = mutableListOf<Photo>()
        for(pp in MediaView.myPhoto) movPhoto.add(Photo(pp))
        return movPhoto
    }
}