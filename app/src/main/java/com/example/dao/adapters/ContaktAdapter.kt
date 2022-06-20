package com.example.dao.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.data.Contakt
import com.example.dao.viewmodel.ContaktViewModel
import com.example.waikiki.R
import com.example.waikiki.databinding.ContaktItemBinding

class ContaktAdapter : ListAdapter<Contakt,ContaktAdapter.ViewHolder>(CONTAKT_COMPARATOR) {

    private var listener: OnItemClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ContaktItemBinding.bind(itemView)

        fun bind(current: Contakt, position: Int) {
            var n = position%3
            val icoColor=
                if(n==0) R.color.color1
                else {
                    if(n==1) R.color.color2 else R.color.color3
                }
            binding.imageView.setColorFilter(ContextCompat.getColor(binding.imageView.context,icoColor), PorterDuff.Mode.MULTIPLY)
            val txtArray = current.contaktName.toCharArray()
            binding.textViewIco.text=txtArray[0].toString().uppercase()

            binding.textName.text=current.contaktName
            val phone = current.contaktPhone
            binding.textPhone.text= phone
            val comm = current.contaktComment
            binding.textComment.text = comm
            val backcolor = if(current.id == ContaktViewModel.currentContaktId) R.color.text_back else R.color.white
            binding.contLayout.setBackgroundColor(ContextCompat.getColor(binding.contLayout.context, backcolor))
            binding.textComment.visibility = if(comm.isNullOrBlank()) GONE else VISIBLE
            binding.divider.visibility = binding.textComment.visibility
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.contakt_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current,position)
        val binding = ContaktItemBinding.bind(holder.itemView)

        binding.container.setOnLongClickListener() {
            setCurrent(current, -1)
            return@setOnLongClickListener true
        }
        binding.container.setOnClickListener {
            setCurrent(current, 0)
        }
    }

    fun setCurrent(current: Contakt, oper: Int) {
        ContaktViewModel.currentContaktId = current.id
        notifyDataSetChanged()
        listener?.onItemClick(oper)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    interface OnItemClickListener {
        fun onItemClick(oper: Int)
    }

    override fun getItemCount() = currentList.size
    fun getItemPosition(item: Contakt) = currentList.indexOf(item)
    override fun getItemId(position: Int) = currentList.get(position).id.toLong()

    companion object {
        private val CONTAKT_COMPARATOR = object : DiffUtil.ItemCallback<Contakt>() {
            override fun areItemsTheSame(oldItem: Contakt, newItem: Contakt): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Contakt, newItem: Contakt): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.contaktName == newItem.contaktName
                        && oldItem.contaktPhone == newItem.contaktPhone
                        && oldItem.contaktComment == newItem.contaktComment
            }
        }
    }
}