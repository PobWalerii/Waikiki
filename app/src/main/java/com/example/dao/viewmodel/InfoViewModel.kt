package com.example.dao.viewmodel

import androidx.lifecycle.*
import com.example.dao.data.CollectedInfo
import com.example.dao.repository.InfoRepository
import kotlinx.coroutines.launch

class InfoViewModel(val repository: InfoRepository): ViewModel() {

    private val _infoList: MutableLiveData<List<CollectedInfo>> = MutableLiveData()
    val infoList: LiveData<List<CollectedInfo>> = _infoList

    fun refresh(currentTopicId: Int) {
        viewModelScope.launch {
            _infoList.postValue(repository.getAll(currentTopicId))
        }
    }

    companion object {
        var currentInfoId: Int = 0
    }
}

class InfoViewModelFactory(val repository: InfoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InfoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

