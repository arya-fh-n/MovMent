package com.arfdevs.myproject.core.helper

import com.arfdevs.myproject.core.data.local.db.entity.WishlistEntity
import com.arfdevs.myproject.core.data.remote.responses.GenresItem
import com.arfdevs.myproject.core.data.remote.responses.MovieDetailsResponse
import com.arfdevs.myproject.core.data.remote.responses.NowPlayingItem
import com.arfdevs.myproject.core.data.remote.responses.PopularItem
import com.arfdevs.myproject.core.data.remote.responses.SearchItem
import com.arfdevs.myproject.core.domain.model.MovieDetailsModel
import com.arfdevs.myproject.core.domain.model.NowPlayingModel
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.core.domain.model.SearchModel
import com.arfdevs.myproject.core.domain.model.SessionModel
import com.arfdevs.myproject.core.domain.model.WishlistModel

object DataMapper {

    fun PopularItem.toUIData() = PopularModel(
        id = id,
        originalTitle = originalTitle,
        posterPath = posterPath,
        voteAverage = (voteAverage / 10.0 * 5.0)
    )

    fun List<PopularItem>.toPopularList() = map {
        it.toUIData()
    }.toList()

    fun NowPlayingItem.toUIData() = NowPlayingModel(
        id = id,
        originalTitle = originalTitle,
        posterPath = posterPath,
        voteAverage = (voteAverage / 10.0 * 5.0),
        price = popularity.toInt()
    )

    fun List<NowPlayingItem>.toNowPlayingList() = map {
        it.toUIData()
    }.toList()

    fun MovieDetailsResponse.toUIData() = MovieDetailsModel(
        id = id,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = (voteAverage / 10.0 * 5.0),
        price = popularity.toInt(),
        tagline = tagline,
        genres = genres.toGenreNameList()
    )

    private fun List<GenresItem>.toGenreNameList() = map { genres ->
        genres.name
    }

    fun SearchItem.toUIData() = SearchModel(
        id = id,
        originalTitle = originalTitle,
        posterPath = posterPath,
        voteAverage = (voteAverage / 10.0 * 5.0),
        price = popularity.toInt()
    )

    fun List<SearchItem>.toSearchList() = map {
        it.toUIData()
    }.toList()

    fun WishlistEntity.toUIData() = WishlistModel(
        movieId = movieId,
        userId = userId,
        originalTitle = originalTitle,
        posterPath = posterPath,
        voteAverage = voteAverage,
        price = price
    )

    fun List<WishlistEntity>.toLocalList() = this.map { wishlist ->
        wishlist.toUIData()
    }

    fun WishlistModel.toEntityData() = WishlistEntity(
        movieId = movieId,
        userId = userId,
        originalTitle = originalTitle,
        posterPath = posterPath,
        voteAverage = voteAverage,
        price = price
    )

    fun SessionModel.toSplashState() = when {
        this.displayName.isEmpty() && this.uid.isEmpty().not() && this.onboardingState -> {
            SplashState.Profile
        }

        this.displayName.isEmpty() && this.uid.isEmpty() && this.onboardingState -> {
            SplashState.Login
        }

        this.displayName.isEmpty() && this.uid.isEmpty() && !this.onboardingState -> {
            SplashState.Onboarding
        }

        this.displayName.isNotEmpty() && this.uid.isNotEmpty() && this.onboardingState -> {
            SplashState.Main
        }

        else -> {
            SplashState.Onboarding
        }
    }

}
