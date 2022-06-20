package com.example.dao.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.data.Topic
import com.example.dao.viewmodel.IconView
import com.example.dao.viewmodel.TopicViewModel
import com.example.waikiki.R
import com.example.waikiki.databinding.TopicItemBinding

class TopicAdapter : ListAdapter<Topic,TopicAdapter.ViewHolder>(TOPIC_COMPARATOR) {

    private var listener: TopicAdapter.OnItemClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TopicItemBinding.bind(itemView)

        fun bind(current: Topic) {
            binding.textDate.text=current.topicDate
            binding.textName.text=current.topicName
            binding.imageView.setImageResource(IconView.allIco[current.icon])
            binding.imageView.setColorFilter(ContextCompat.getColor(binding.imageView.context,IconView.colorIco[current.icoColor]), PorterDuff.Mode.MULTIPLY)
            val cardComm = binding.textComment
            val comm = current.topicComment
            if(current.id == TopicViewModel.currentTopicId){
                binding.contLayout.setBackgroundColor(ContextCompat.getColor(binding.contLayout.context, R.color.text_back))
                 }
            else {
                val backcolor = if(current.actualization==0) R.color.white else R.color.favirite
                binding.contLayout.setBackgroundColor(ContextCompat.getColor(binding.contLayout.context, backcolor))
            }
            binding.textComment.text = comm
            cardComm.visibility = if(comm.isNullOrBlank()) GONE else VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.topic_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = TopicItemBinding.bind(holder.itemView)
        if(TopicViewModel.recyclerAnim) holder.binding.container.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.alpha)
        val current = getItem(position)
        holder.bind(current)

        binding.container.setOnClickListener {
            setCurrent(current, 1)
        }
        binding.container.setOnLongClickListener() {
            setCurrent(current, -1)
            return@setOnLongClickListener true
        }
    }

    fun setCurrent(current: Topic, oper: Int) {
        with(TopicViewModel) {
            currentTopicId = current.id
            currentTopicName= current.topicName
            notifyDataSetChanged()
        }
        listener?.onItemClick(oper)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(oper: Int)
    }

    override fun getItemCount() = currentList.size
    fun getItemPosition(item: Topic) = currentList.indexOf(item)
    override fun getItemId(position: Int) = currentList.get(position).id.toLong()

    companion object {
        private val TOPIC_COMPARATOR = object : DiffUtil.ItemCallback<Topic>() {
            override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.actualization == newItem.actualization
                        && oldItem.topicName == newItem.topicName
                        && oldItem.icon == newItem.icon
                        && oldItem.icoColor == newItem.icoColor
                        && oldItem.topicComment == newItem.topicComment
            }
        }
    }
}