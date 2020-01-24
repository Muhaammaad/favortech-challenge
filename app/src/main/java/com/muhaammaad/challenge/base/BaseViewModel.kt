package com.muhaammaad.challenge.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.muhaammaad.challenge.injection.component.DaggerViewModelInjector
import com.muhaammaad.challenge.injection.component.ViewModelInjector
import com.muhaammaad.challenge.injection.module.NetworkModule
import com.muhaammaad.challenge.ui.home.viewmodel.HomeViewModel
import com.muhaammaad.challenge.ui.login.viewmodel.LoginViewModel
import com.muhaammaad.challenge.ui.photoDetail.viewmodel.PhotoDetailViewModel

/**
 * Base ViewModel class
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is HomeViewModel -> injector.inject(this)
            is PhotoDetailViewModel -> injector.inject(this)
            is LoginViewModel -> injector.inject(this)
        }
    }

}