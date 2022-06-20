package com.example.waikiki

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.adapters.ContaktAdapter
import com.example.dao.viewmodel.*
import com.example.waikiki.databinding.FragmentRecyclerBinding

class ContaktFragment() : Fragment() {

    lateinit var binding: FragmentRecyclerBinding
    lateinit var adapter: ContaktAdapter
    lateinit var layerManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView

    private val contaktViewModel: ContaktViewModel by viewModels {
        ContaktViewModelFactory(ServApplication().repository1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contaktViewModel.allContakt.observe(this) { contakt ->
            contakt.let {
                adapter.submitList(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRecyclerBinding.bind(inflater.inflate(R.layout.fragment_recycler,container,false))
        recyclerView = binding.recyclerViewFragment
        adapter = ContaktAdapter()
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        layerManager = binding.recyclerViewFragment.layoutManager as LinearLayoutManager

        BottomBarView.viewModel_contakt=contaktViewModel
        BottomBarView.adapter_contakt=adapter

        adapter.setOnItemClickListener(object : ContaktAdapter.OnItemClickListener {
            override fun onItemClick(oper: Int) {
                when(oper) {
                    -1-> (activity as MainActivity).onClickEdit()
                }
            }
        })
        onUpdate()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        onUpdate()
    }

    fun onUpdate() {
        contaktViewModel.refresh(TopicViewModel.currentTopicId)
        adapter.notifyDataSetChanged()
    }

}

