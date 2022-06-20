package com.example.waikiki

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.adapters.TopicAdapter
import com.example.dao.viewmodel.*
import com.example.waikiki.databinding.FragmentRecyclerBinding

class TopicFragment() : Fragment() {

    lateinit var binding_: View
    lateinit var adapter: TopicAdapter
    lateinit var layerManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView

    private val topicViewModel: TopicViewModel by viewModels {
        TopicViewModelFactory(ServApplication().repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topicViewModel.allTopic.observe(this) { topic ->
            topic.let {
                adapter.submitList(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_ = inflater.inflate(R.layout.fragment_recycler,container,false)
        return binding_
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding= FragmentRecyclerBinding.bind(binding_)
        recyclerView = binding.recyclerViewFragment
        adapter = TopicAdapter()
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        layerManager = binding.recyclerViewFragment.layoutManager as LinearLayoutManager

        BottomBarView.viewModel_topic=topicViewModel
        BottomBarView.adapter_topic=adapter

        adapter.setOnItemClickListener(object : TopicAdapter.OnItemClickListener {
            override fun onItemClick(oper: Int) {
                if(oper==1) {
                    (activity as MainActivity).onClickPage1()
                } else {    // -1
                    (activity as MainActivity).onClickEdit()
                }
            }
        })
        onUpdate()
    }

    override fun onResume() {
        super.onResume()
        onUpdate()
    }

    fun onUpdate() {
        topicViewModel.refresh()
        adapter.notifyDataSetChanged()
    }

}

