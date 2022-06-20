package com.example.dao.viewmodel

import androidx.lifecycle.*
import com.example.dao.data.Topic
import com.example.dao.repository.TopicRepository
import kotlinx.coroutines.launch

class TopicViewModel(val repository: TopicRepository): ViewModel() {

    private val _allTopic: MutableLiveData<List<Topic>> = MutableLiveData()
    val allTopic: LiveData<List<Topic>> = _allTopic

    fun refresh() {
        viewModelScope.launch {
            _allTopic.postValue(repository.getAll())
        }
    }

    companion object {
        var currentTopicId: Int = 0
        var currentTopicName = ""
        var recyclerAnim = true
    }
}

class TopicViewModelFactory(val repository: TopicRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TopicViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

