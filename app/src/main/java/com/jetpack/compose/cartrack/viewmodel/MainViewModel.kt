package com.jetpack.compose.cartrack.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.jetpack.compose.cartrack.entities.ApiService
import com.jetpack.compose.cartrack.entities.Repository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel(application) {

    private val apiService = ApiService()
    private val disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}