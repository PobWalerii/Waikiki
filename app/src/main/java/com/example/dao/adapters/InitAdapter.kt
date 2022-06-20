package com.example.dao.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.data.InitialInfo
import com.example.dao.viewmodel.InfoViewModel
import com.example.dao.viewmodel.InitViewModel
import com.example.dao.viewmodel.MediaView
import com.example.waikiki.R
import com.example.waikiki.databinding.ContentItemBinding
import com.example.waikiki.showItem

class InitAdapter : ListAdapter<InitialInfo,InitAdapter.ViewHolder>(INIT_COMPARATOR) {

    private var listener: OnItemClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ContentItemBinding.bind(itemView)

        fun bind(current: InitialInfo, position: Int) {
            binding.textDate.visibility = GONE
            showItem(binding, current.id == InitViewModel.currentInfoId,
                current.text,
                current.photo,
                current.comment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current,position)
        val binding = ContentItemBinding.bind(holder.itemView)

        binding.container.setOnLongClickListener() {
            setCurrent(current,-1)
            return@setOnLongClickListener true
        }
        binding.cardText.setOnLongClickListener() {
            setCurrent(current,-1)
            return@setOnLongClickListener true
        }
        binding.cardPhoto.setOnLongClickListener() {
            setCurrent(current,-1)
            return@setOnLongClickListener true
        }
        binding.cardComment.setOnLongClickListener() {
            setCurrent(current,-1)
            return@setOnLongClickListener true
        }
        binding.container.setOnClickListener {
            setCurrent(current,0)
        }
        binding.cardText.setOnClickListener {
            setCurrent(current,1)
        }
        binding.cardComment.setOnClickListener {
            setCurrent(current,2)
        }
        binding.cardPhoto.setOnClickListener() {
            setCurrent(current,3)
        }
    }

    fun setCurrent(current: InitialInfo,inf: Int) {
        InitViewModel.currentInfoId = current.id
        notifyDataSetChanged()
        MediaView.photoList = current.photo
        listener?.onItemClick(inf)
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    interface OnItemClickListener {
        fun onItemClick(inf: Int)
    }

    override fun getItemCount() = currentList.size
    fun getItemPosition(item: InitialInfo) = currentList.indexOf(item)
    override fun getItemId(position: Int) = currentList.get(position).id.toLong()

    companion object {
        private val INIT_COMPARATOR = object : DiffUtil.ItemCallback<InitialInfo>() {
            override fun areItemsTheSame(oldItem: InitialInfo, newItem: InitialInfo): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: InitialInfo, newItem: InitialInfo): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.text == newItem.text
                        && oldItem.comment == newItem.comment
                        && oldItem.photo == newItem.photo
                        && oldItem.video == newItem.video
                        && oldItem.sound == newItem.sound
            }
        }
    }
}