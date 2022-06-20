package com.example.dao.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dao.viewmodel.MediaView
import com.example.waikiki.databinding.PhotoItemRowBinding
import java.io.File

class GalleryAdapterRow : RecyclerView.Adapter<GalleryAdapterRow.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    override fun getItemCount() = MediaView.myPhoto.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = PhotoItemRowBinding.bind(itemView)
        fun bind(current: File) {
            var itemPhoto = binding.imageView
            Glide.with(itemPhoto).load(current).into(itemPhoto as ImageView)
        }
    }

    /*
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PhotoItemRowBinding.bind(itemView)
    }
*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PhotoItemRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = MediaView.myPhoto[position]
        //holder.setIsRecyclable(true)
        holder.bind(current)
        //holder.setIsRecyclable(false)
        val binding = PhotoItemRowBinding.bind(holder.itemView)
        binding.CardView.setOnClickListener {
            MediaView.currentFile=current
            MediaView.currentPosition=position
            listener?.onItemClick()
        }
    }

    /*
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoFile = MediaView.myPhoto[position]
        val imageView = holder.binding.imageView

        Glide.with(imageView.context).load(photoFile).into(imageView)

        imageView.setOnClickListener {
            MediaView.currentFile=photoFile
            MediaView.currentPosition=position
            listener?.onItemClick()
        }
    }
     */

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    interface OnItemClickListener {
        fun onItemClick()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}

