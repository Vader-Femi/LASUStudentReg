package com.femi.lasustudentreg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.femi.lasustudentreg.data.repository.MainActivityRepository

class ViewModelFactory(
    private val repository: MainActivityRepository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> MainActivityViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }
}