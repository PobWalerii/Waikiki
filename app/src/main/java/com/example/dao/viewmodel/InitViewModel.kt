package com.example.dao.viewmodel

import androidx.lifecycle.*
import com.example.dao.data.InitialInfo
import com.example.dao.repository.InitRepository
import kotlinx.coroutines.launch

class InitViewModel(val repository: InitRepository): ViewModel() {

    private val _infoList: MutableLiveData<List<InitialInfo>> = MutableLiveData()
    val infoList: LiveData<List<InitialInfo>> = _infoList

    fun refresh(currentTopicId: Int) {
        viewModelScope.launch {
            _infoList.postValue(repository.getAll(currentTopicId))
        }
    }

    companion object {
        var currentInfoId: Int = 0
    }
}

class InitViewModelFactory(val repository: InitRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InitViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

