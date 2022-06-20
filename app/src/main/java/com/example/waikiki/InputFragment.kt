package com.example.waikiki

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.adapters.InitAdapter
import com.example.dao.viewmodel.*
import com.example.waikiki.databinding.FragmentRecyclerBinding


class InputFragment : Fragment() {

    lateinit var binding: FragmentRecyclerBinding
    lateinit var adapter: InitAdapter
    lateinit var layerManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView

    private val initViewModel: InitViewModel by viewModels {
        InitViewModelFactory(ServApplication().repository3)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel.infoList.observe(this) { info ->
            info.let {
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
        adapter = InitAdapter()
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        layerManager = binding.recyclerViewFragment.layoutManager as LinearLayoutManager

        BottomBarView.viewModel_init=initViewModel
        BottomBarView.adapter_init=adapter

        adapter.setOnItemClickListener(object : InitAdapter.OnItemClickListener {
            override fun onItemClick(inf: Int) {
                when(inf) {
                   -1-> (activity as MainActivity).onClickEdit()
                    3 -> {
                        showItemPhotoGallery(requireContext(), activity as MainActivity)
                    }
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
        initViewModel.refresh(TopicViewModel.currentTopicId)
        adapter.notifyDataSetChanged()
    }
}