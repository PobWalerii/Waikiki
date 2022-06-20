package com.example.dao.viewmodel

import androidx.lifecycle.*
import com.example.dao.data.Contakt
import com.example.dao.repository.ContaktRepository
import kotlinx.coroutines.launch

class ContaktViewModel(val repository: ContaktRepository): ViewModel() {

    private val _allContakt: MutableLiveData<List<Contakt>> = MutableLiveData()
    val allContakt: LiveData<List<Contakt>> = _allContakt

    fun refresh(currentTopicId: Int) {
        viewModelScope.launch {
            _allContakt.postValue(repository.getAll(currentTopicId))
        }
    }

    companion object {
        var currentContaktId: Int = 0
    }
}

class ContaktViewModelFactory(val repository: ContaktRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContaktViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContaktViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

