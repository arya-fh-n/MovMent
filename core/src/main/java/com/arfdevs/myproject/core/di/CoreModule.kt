package com.arfdevs.myproject.core.di

import com.arfdevs.myproject.core.base.BaseModule
import com.arfdevs.myproject.core.data.remote.ApiClient
import com.arfdevs.myproject.core.data.remote.ApiEndpoint
import com.arfdevs.myproject.core.data.remote.datasource.RemoteDataSource
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.domain.repository.MovieRepositoryImpl
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.domain.repository.UserRepositoryImpl
import com.arfdevs.myproject.core.domain.usecase.AppInteractor
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object CoreModule : BaseModule {

    private val firebaseModule = module {
        single {
            Firebase.auth
        }

        single {
            Firebase.crashlytics
        }
    }

    private val dataSourceModule = module {
        single {
            RemoteDataSource(get(), get())
        }
    }

    private val repositoryModule = module {
        single<MovieRepository> {
            MovieRepositoryImpl(get())
        }

        single<UserRepository> {
            UserRepositoryImpl(get())
        }
    }

    private val useCaseModule = module {
        single<AppUseCase> {
            AppInteractor(get(), get())
        }
    }

    private val networkModule = module {
        single {
            ChuckerInterceptor.Builder(androidContext()).build()
        }

        single {
            ApiClient(get())
        }

        single<ApiEndpoint> {
            get<ApiClient>().create()
        }
    }

    override fun getModules(): List<Module> = listOf(
        firebaseModule,
        dataSourceModule,
        repositoryModule,
        useCaseModule,
        networkModule
    )

}