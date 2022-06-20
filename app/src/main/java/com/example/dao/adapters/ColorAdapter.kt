package com.example.dao.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.viewmodel.IconView
import com.example.dao.viewmodel.IconView.Companion.currentIco
import com.example.waikiki.R
import com.example.waikiki.databinding.IcoItemBinding

class ColorAdapter() : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    private var listener: ColorAdapter.OnItemClickListener? = null

    override fun getItemCount() = IconView.colorIco.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = IcoItemBinding.bind(itemView)
        fun bind(current: Int, position: Int) {
            var icon = binding.imageView
            icon.setImageResource(IconView.allIco[currentIco])
            icon.setColorFilter(ContextCompat.getColor(icon.context,IconView.colorIco[position]),PorterDuff.Mode.MULTIPLY)
            if(position==IconView.currentColor) {
                icon.setBackgroundColor(ContextCompat.getColor(icon.context, R.color.gray))
            }
            else {
                icon.setBackgroundColor(ContextCompat.getColor(icon.context,R.color.white))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.ico_item, parent, false)
        return ColorAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorAdapter.ViewHolder, position: Int) {
        val current = IconView.colorIco[position]
        holder.bind(current, position)
        val binding = IcoItemBinding.bind(holder.itemView)

        binding.imageView.setOnClickListener {
            IconView.currentColor=position
            notifyDataSetChanged()
            listener?.onItemClick()
        }
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    interface OnItemClickListener {
        fun onItemClick()
    }
}