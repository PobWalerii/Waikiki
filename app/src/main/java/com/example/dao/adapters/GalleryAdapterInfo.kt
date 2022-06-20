package com.example.dao.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.dao.viewmodel.MediaView
import com.example.dao.viewmodel.Photo
import com.example.waikiki.databinding.PhotoItemBinding


class GalleryAdapterInfo : RecyclerView.Adapter<GalleryAdapterInfo.ItemViewHolder>() {

    private var listener: OnItemClickListener? = null

    override fun getItemCount() = differ.currentList.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //inner class ItemViewHolder(val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding = PhotoItemBinding.bind(itemView)
//        fun bind(current: Photo) {
//            var itemPhoto = binding.imageView
//            Glide.with(itemPhoto).load(current.name).into(itemPhoto)
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PhotoItemBinding.inflate(inflater, parent, false)
        //return ItemViewHolder(binding)
        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = differ.currentList[position]
        //holder.bind(current)
        val binding = PhotoItemBinding.bind(holder.itemView)

        var itemPhoto = binding.imageView
        Glide.with(itemPhoto).load(current.name).into(itemPhoto)

        //centerCrop()
        binding.CardView.setOnClickListener {
            MediaView.bindingCurrenGalleryItem = binding
            MediaView.currentFile=current.name
            MediaView.currentPosition=position
            listener?.onItemClick()
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val list = differ.currentList.toMutableList()
        val fromItem = list[fromPosition]
        list.removeAt(fromPosition)
        if (toPosition < fromPosition) {
            list.add(toPosition + 1, fromItem)
        } else {
            list.add(toPosition - 1, fromItem)
        }
        differ.submitList(list)

        val movElement = MediaView.myPhoto[fromPosition]
        MediaView.myPhoto.removeAt(fromPosition)
        MediaView.myPhoto.add(toPosition, movElement)
    }

//    override fun getItemViewType(position: Int): Int {
//        return position
//    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun removePhoto(position: Int) {
        val list = differ.currentList.toMutableList()
        list.removeAt(position)
        differ.submitList(list)
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    interface OnItemClickListener {
        fun onItemClick()
    }

}

