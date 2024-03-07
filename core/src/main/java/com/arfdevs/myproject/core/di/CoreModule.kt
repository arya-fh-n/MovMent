package com.arfdevs.myproject.core.di

import android.content.Context
import androidx.room.Room
import com.arfdevs.myproject.core.base.BaseModule
import com.arfdevs.myproject.core.data.local.datasource.LocalDataSource
import com.arfdevs.myproject.core.data.local.db.Database
import com.arfdevs.myproject.core.data.local.sharedpref.SharedPreferencesHelper
import com.arfdevs.myproject.core.data.local.sharedpref.SharedPreferencesHelperImpl
import com.arfdevs.myproject.core.data.remote.ApiClient
import com.arfdevs.myproject.core.data.remote.ApiEndpoint
import com.arfdevs.myproject.core.data.remote.datasource.RemoteDataSource
import com.arfdevs.myproject.core.domain.repository.FirebaseRepository
import com.arfdevs.myproject.core.domain.repository.FirebaseRepositoryImpl
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.domain.repository.MovieRepositoryImpl
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.domain.repository.UserRepositoryImpl
import com.arfdevs.myproject.core.domain.usecase.AppInteractor
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.arfdevs.myproject.core.helper.Constants.SHARED_PREF_FILE
import com.arfdevs.myproject.core.helper.NoInternetInterceptor
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.database.database
import com.google.firebase.remoteconfig.remoteConfig
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

        single {
            Firebase.remoteConfig
        }

        single {
            Firebase.analytics
        }

        single {
            Firebase.database.reference
        }
    }

    private val databaseModule = module {
        single {
            Room.databaseBuilder(androidContext(), Database::class.java, "movment_db")
                .fallbackToDestructiveMigration()
                .build()
        }

        single {
            get<Database>().Dao()
        }
    }

    private val sharedPrefModule = module {
        single {
            androidContext().getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)
        }

        single<SharedPreferencesHelper> {
            SharedPreferencesHelperImpl(get())
        }
    }

    private val dataSourceModule = module {
        single {
            LocalDataSource(get(), get())
        }

        single {
            RemoteDataSource(get(), get())
        }
    }

    private val repositoryModule = module {
        single<MovieRepository> {
            MovieRepositoryImpl(get(), get())
        }

        single<UserRepository> {
            UserRepositoryImpl(get(), get())
        }

        single<FirebaseRepository> {
            FirebaseRepositoryImpl(get(), get(), get())
        }
    }

    private val useCaseModule = module {
        single<AppUseCase> {
            AppInteractor(get(), get(), get())
        }
    }

    private val networkModule = module {
        single {
            ChuckerInterceptor.Builder(androidContext()).build()
        }

        single {
            NoInternetInterceptor(androidContext())
        }

        single {
            ApiClient(get(), get())
        }

        single<ApiEndpoint> {
            get<ApiClient>().create()
        }
    }

    override fun getModules(): List<Module> = listOf(
        firebaseModule,
        databaseModule,
        sharedPrefModule,
        dataSourceModule,
        repositoryModule,
        useCaseModule,
        networkModule
    )

}