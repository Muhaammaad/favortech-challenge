package com.muhaammaad.challenge.injection.component

import com.muhaammaad.challenge.injection.module.NetworkModule
import com.muhaammaad.challenge.ui.home.viewmodel.HomeViewModel
import com.muhaammaad.challenge.ui.login.viewmodel.LoginViewModel
import com.muhaammaad.challenge.ui.photoDetail.viewmodel.PhotoDetailViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified ViewModels.
     * @param viewModel in which to inject the dependencies
     */
    fun inject(viewModel: HomeViewModel)
    fun inject(viewModel: LoginViewModel)
    fun inject(viewModel: PhotoDetailViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}