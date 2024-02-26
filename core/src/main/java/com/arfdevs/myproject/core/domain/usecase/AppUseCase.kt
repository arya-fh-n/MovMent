package com.arfdevs.myproject.core.domain.usecase

import android.util.Log
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.helper.DataMapper.toListData
import com.arfdevs.myproject.core.helper.safeDataCall

interface AppUseCase {

    suspend fun getPopular(page: Int): List<PopularModel>

}

class AppInteractor(private val repository: MovieRepository) : AppUseCase {

    override suspend fun getPopular(page: Int): List<PopularModel> = safeDataCall {
        val data = repository.fetchPopular(page).results.toListData()
        Log.d("Retrofit INTERACTOR", "getPopular: $data")
        data
    }

}