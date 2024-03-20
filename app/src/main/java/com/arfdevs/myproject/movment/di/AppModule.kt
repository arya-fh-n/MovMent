package com.arfdevs.myproject.movment.di

import com.arfdevs.myproject.core.base.BaseModule
import com.arfdevs.myproject.movment.presentation.viewmodel.AuthViewModel
import com.arfdevs.myproject.movment.presentation.viewmodel.HomeViewModel
import com.arfdevs.myproject.movment.presentation.viewmodel.FirebaseViewModel
import com.arfdevs.myproject.movment.presentation.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModule : BaseModule {

    val viewModelModule = module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::AuthViewModel)
        viewModelOf(::MovieViewModel)
        viewModelOf(::FirebaseViewModel)
    }

    override fun getModules(): List<Module> = listOf(
        viewModelModule
    )

}
