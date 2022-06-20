package com.example.waikiki

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.adapters.ColorAdapter
import com.example.dao.adapters.IcoAdapter
import com.example.dao.data.Topic
import com.example.dao.viewmodel.*
import com.example.waikiki.databinding.FragmentTopicEditBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TopicEditFragment(var curId: Int) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentTopicEditBinding
    lateinit var adapter: IcoAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var layerManager: LinearLayoutManager
    lateinit var adapter_color: ColorAdapter
    lateinit var recyclerView_color: RecyclerView
    lateinit var layerManager_color: LinearLayoutManager
    var currentDate = ""
    var nameTopic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTopicEditBinding.bind(inflater.inflate(R.layout.fragment_topic_edit,container))
        binding.topicComment.setHorizontallyScrolling(false)
        binding.topicComment.setLines(3)
        recyclerView = binding.rwIcon
        adapter = IcoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context, HORIZONTAL,false)
        layerManager = binding.rwIcon.layoutManager as LinearLayoutManager

        recyclerView_color = binding.rwColor
        adapter_color = ColorAdapter()
        recyclerView_color.adapter = adapter_color
        recyclerView_color.layoutManager = LinearLayoutManager(this.context, HORIZONTAL,false)
        layerManager_color = binding.rwColor.layoutManager as LinearLayoutManager

        adapter.setOnItemClickListener(object : IcoAdapter.OnItemClickListener {
            override fun onItemClick() {
                icoClick()
            }
        })
        adapter_color.setOnItemClickListener(object : ColorAdapter.OnItemClickListener {
            override fun onItemClick() {
                colorClick()
            }
        })

        return binding.root
    }

    fun icoClick(){
        adapter_color.notifyDataSetChanged()
    }
    fun colorClick(){
        adapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(curId!=0) {
            binding.textOperName.text = getText(R.string.text_edit_topic)
            lifecycleScope.launchWhenResumed {
                val curById: List<Topic> = ServApplication.database.topicDao().getByID(curId)
                val curTopic = curById.firstOrNull()?.let { it }
                binding.textTopicName.editText?.setText(curTopic?.topicName)
                binding.textTopicComment.editText?.setText(curTopic?.topicComment)
                currentDate = curTopic?.topicDate.toString()
                IconView.currentIco = curTopic!!.icon
                IconView.currentColor = curTopic!!.icoColor
                binding.switch1.isChecked = (curTopic!!.actualization == 1)
                recyclerView.scrollToPosition(IconView.currentIco)
                adapter.notifyDataSetChanged()
                adapter_color.notifyDataSetChanged()
            }
        } else {
            binding.textOperName.text=getText(R.string.text_new_topic)
            binding.butdelete.visibility= GONE
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            currentDate = sdf.format(Date())
            IconView.currentIco=0
            IconView.currentColor=0
        }

        binding.butexit.setOnClickListener(){
            dismiss()
        }

        binding.butsave.setOnClickListener(){
            nameTopic = binding.textTopicName.editText?.text.toString()
            if (nameTopic.isNotEmpty()) {
                insertTopic(Topic(
                    curId,
                    nameTopic,
                    currentDate,
                    binding.textTopicComment.editText?.text.toString(),
                    IconView.currentIco,
                    IconView.currentColor,
                    if(binding.switch1.isChecked) 1 else 0
                ))
                dismiss()
            }
        }

        binding.butdelete.setOnClickListener(){
            val dialog = MaterialAlertDialogBuilder(it.context)
                .setTitle(R.string.but_delete_txt)
                .setIcon(R.drawable.warning)
                .setMessage(R.string.txt_delete_topic)
                .setPositiveButton(R.string.but_yes_txt) { dialog, _ ->
                    deleteTopic(curId)
                    dismiss()
                }
                .setNegativeButton(R.string.but_no_txt) { dialog, _ -> }
                .create()
            dialog.show()
        }
    }

    fun deleteTopic(curId: Int){
        lifecycleScope.launchWhenResumed {
            ServApplication.database.topicDao().deleteByID(curId)
            activity?.runOnUiThread {
                TopicViewModel.currentTopicId = 0
            }
        }
    }

    fun insertTopic(topic: Topic) {
        lifecycleScope.launch {
            val new= ServApplication.database.topicDao().insertAll(topic).toInt()
            activity?.runOnUiThread {
                TopicViewModel.currentTopicId = new
                TopicViewModel.currentTopicName = nameTopic
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BottomBarView.viewModel_topic.refresh()
        BottomBarView.adapter_topic.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {}
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    with(binding) {
                        if(slideOffset < -0.3) {
                            if(slideOffset > -0.7) layoutCollapsed.alpha = 1.5F+2*slideOffset
                        } else layoutCollapsed.alpha = 1F
                    }
                }
            }
            )
        }
    }
}