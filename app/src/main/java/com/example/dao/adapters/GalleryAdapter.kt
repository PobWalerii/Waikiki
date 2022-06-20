package com.example.dao.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dao.viewmodel.MediaView
import com.example.waikiki.R
import com.example.waikiki.databinding.PhotoItemBinding
import java.io.File


class GalleryAdapter() : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    override fun getItemCount() = MediaView.mediaList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(current: File) {
            var binding = PhotoItemBinding.bind(itemView)
            var itemPhoto = binding.imageView
            Glide.with(itemPhoto).load(current)
                .override(550, 750)
                .centerCrop()
                //.fitCenter()
                //.transform(RoundedCornersTransformation(30, 0))
                .into(itemPhoto as ImageView)  //
            binding.imageCollection.visibility =
                    if (MediaView.isFileInList(current)) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = MediaView.mediaList[position]
        holder.bind(current)
        val binding = PhotoItemBinding.bind(holder.itemView)
        binding.CardView.setOnClickListener {
            MediaView.bindingCurrenGalleryItem = binding
            MediaView.currentFile=current
            MediaView.currentPosition=position
            listener?.onItemClick()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    interface OnItemClickListener {
        fun onItemClick()
    }

}

