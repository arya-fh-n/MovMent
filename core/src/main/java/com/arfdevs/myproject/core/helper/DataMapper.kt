package com.arfdevs.myproject.core.helper

import com.arfdevs.myproject.core.data.remote.responses.GenresItem
import com.arfdevs.myproject.core.data.remote.responses.MovieDetailsResponse
import com.arfdevs.myproject.core.data.remote.responses.NowPlayingItem
import com.arfdevs.myproject.core.data.remote.responses.PopularItem
import com.arfdevs.myproject.core.domain.model.GenresModel
import com.arfdevs.myproject.core.domain.model.MovieDetailsModel
import com.arfdevs.myproject.core.domain.model.NowPlayingModel
import com.arfdevs.myproject.core.domain.model.PopularModel

object DataMapper {

    fun PopularItem.toUIData() = PopularModel(
        id = id,
        originalTitle = originalTitle,
        posterPath = posterPath,
        voteAverage = voteAverage
    )

    fun List<PopularItem>.toPopularList() = map {
        it.toUIData()
    }.toList()

    fun NowPlayingItem.toUIData() = NowPlayingModel(
        id = id,
        originalTitle = originalTitle,
        posterPath = posterPath,
        voteAverage = voteAverage,
        price = popularity.toInt()
    )

    fun List<NowPlayingItem>.toNowPlayingList() = map {
        it.toUIData()
    }.toList()

    fun MovieDetailsResponse.toUIData() = MovieDetailsModel(
        id = id,
        originalTitle = originalTitle,
        overview = overview,
        backdropPath = backdropPath,
        voteAverage = (voteAverage / 10.0 * 5.0),
        price = popularity.toInt(),
        tagline = tagline,
        genres = genres.toGenreNameList()
    )

    private fun List<GenresItem>.toGenreNameList() = map { genres ->
        genres.name
    }

}
