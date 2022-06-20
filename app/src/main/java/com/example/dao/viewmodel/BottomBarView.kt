package com.example.dao.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dao.adapters.ContaktAdapter
import com.example.dao.adapters.InfoAdapter
import com.example.dao.adapters.InitAdapter
import com.example.dao.adapters.TopicAdapter
import com.example.waikiki.databinding.ActivityMainBinding

class BottomBarView:  ViewModel() {
    companion object {
        lateinit var binding: ActivityMainBinding
        lateinit var viewModel_topic: TopicViewModel
        lateinit var adapter_topic: TopicAdapter
        lateinit var viewModel_contakt: ContaktViewModel
        lateinit var adapter_contakt: ContaktAdapter
        lateinit var viewModel_info: InfoViewModel
        lateinit var adapter_info: InfoAdapter
        lateinit var viewModel_init: InitViewModel
        lateinit var adapter_init: InitAdapter
    }
}