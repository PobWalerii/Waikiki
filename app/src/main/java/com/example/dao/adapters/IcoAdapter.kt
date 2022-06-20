package com.example.dao.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.viewmodel.IconView
import com.example.dao.viewmodel.IconView.Companion.currentColor
import com.example.waikiki.R
import com.example.waikiki.databinding.IcoItemBinding

class IcoAdapter() : RecyclerView.Adapter<IcoAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    override fun getItemCount() = IconView.allIco.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = IcoItemBinding.bind(itemView)
        fun bind(current: Int, position: Int) {
            var icon = binding.imageView
            icon.setImageResource(current)
            if(position==IconView.currentIco) {
                icon.setBackgroundColor(ContextCompat.getColor(icon.context,IconView.colorIco[currentColor]))
                icon.setColorFilter(ContextCompat.getColor(icon.context, R.color.white),PorterDuff.Mode.SRC_ATOP)
            }
            else {
                icon.setBackgroundColor(ContextCompat.getColor(icon.context,R.color.white))
                icon.setColorFilter(ContextCompat.getColor(icon.context,IconView.colorIco[currentColor]),PorterDuff.Mode.MULTIPLY)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IcoAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.ico_item, parent, false)
        return IcoAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: IcoAdapter.ViewHolder, position: Int) {
        val current = IconView.allIco[position]
        holder.bind(current, position)
        val binding = IcoItemBinding.bind(holder.itemView)

        binding.imageView.setOnClickListener {
            IconView.currentIco=position
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

