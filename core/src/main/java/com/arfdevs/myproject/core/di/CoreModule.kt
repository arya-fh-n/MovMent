package com.arfdevs.myproject.core.di

import com.arfdevs.myproject.core.base.BaseModule
import com.arfdevs.myproject.core.data.remote.ApiClient
import com.arfdevs.myproject.core.data.remote.ApiEndpoint
import com.arfdevs.myproject.core.data.remote.datasource.RemoteDataSource
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.domain.repository.MovieRepositoryImpl
import com.arfdevs.myproject.core.domain.usecase.AppInteractor
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.chuckerteam.chucker.api.ChuckerInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object CoreModule : BaseModule {

    private val dataSourceModule = module {
        single {
            RemoteDataSource(get())
        }
    }

    private val repositoryModule = module {
        single<MovieRepository> {
            MovieRepositoryImpl(get())
        }
    }

    private val useCaseModule = module {
        single<AppUseCase> {
            AppInteractor(get())
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
        dataSourceModule,
        repositoryModule,
        useCaseModule,
        networkModule
    )

}