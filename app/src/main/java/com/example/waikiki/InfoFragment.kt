package com.example.waikiki

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.adapters.InfoAdapter
import com.example.dao.viewmodel.*
import com.example.waikiki.camera.InfoGalleryRowFragment
import com.example.waikiki.databinding.FragmentRecyclerBinding

class InfoFragment() : Fragment() {

    lateinit var binding: FragmentRecyclerBinding
    lateinit var adapter: InfoAdapter
    lateinit var layerManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView

    private val infoViewModel: InfoViewModel by viewModels {
        InfoViewModelFactory(ServApplication().repository2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        infoViewModel.infoList.observe(this) { info ->
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
        adapter = InfoAdapter()
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        layerManager = binding.recyclerViewFragment.layoutManager as LinearLayoutManager

        BottomBarView.viewModel_info=infoViewModel
        BottomBarView.adapter_info=adapter

        adapter.setOnItemClickListener(object : InfoAdapter.OnItemClickListener {
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
        infoViewModel.refresh(TopicViewModel.currentTopicId)
        adapter.notifyDataSetChanged()
    }

}