package com.example.dao.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dao.viewmodel.MediaView
import com.example.waikiki.R
import com.example.waikiki.databinding.PhotoItemMiniBinding
import java.io.File

class GalleryAdapterMini() : RecyclerView.Adapter<GalleryAdapterMini.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    override fun getItemCount() = MediaView.myPhoto.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = PhotoItemMiniBinding.bind(itemView)
        fun bind(current: File) {
            var itemPhoto = binding.imageView
            Glide.with(itemPhoto).load(current).into(itemPhoto as ImageView)
        }
    }

    /*
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PhotoItemMiniBinding.bind(itemView)
    }
    */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_item_mini, parent, false)
        return ViewHolder(view)

    //val inflater = LayoutInflater.from(parent.context)
        //val binding = PhotoItemMiniBinding.inflate(inflater, parent, false)
        //return ViewHolder(binding.root)
    }
    /*
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoFile = MediaView.myPhoto[position]
        val imageView = holder.binding.imageView

        Glide.with(imageView.context).load(photoFile).into(imageView)

        imageView.setOnClickListener {
            listener?.onItemClick()
        }
    }
     */

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = MediaView.myPhoto[position]
        holder.setIsRecyclable(true)
        holder.bind(current)
        holder.setIsRecyclable(false)
        val binding = PhotoItemMiniBinding.bind(holder.itemView)
        binding.CardView.setOnClickListener {
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

