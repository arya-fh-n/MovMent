package com.arfdevs.myproject.movment.di

import com.arfdevs.myproject.core.base.BaseModule
import com.arfdevs.myproject.movment.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModule: BaseModule {

    val viewModelModule = module {
        viewModel { HomeViewModel(get()) }
    }

    override fun getModules(): List<Module> = listOf(
        viewModelModule
    )

}