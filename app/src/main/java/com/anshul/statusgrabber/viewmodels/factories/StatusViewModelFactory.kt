package com.anshul.statusgrabber.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anshul.statusgrabber.data.StatusRepo

class StatusViewModelFactory (private val repo: StatusRepo):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>):T{
        return StatusViewModel(repo) as  T
    }
}