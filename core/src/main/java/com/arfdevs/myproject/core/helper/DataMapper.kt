package com.arfdevs.myproject.core.helper

import com.arfdevs.myproject.core.data.remote.responses.NowPlayingItem
import com.arfdevs.myproject.core.data.remote.responses.PopularItem
import com.arfdevs.myproject.core.domain.model.NowPlayingModel
import com.arfdevs.myproject.core.domain.model.PopularModel

object DataMapper {

    fun PopularItem.toUIData() = PopularModel(
        originalTitle = originalTitle,
        posterPath = posterPath,
        voteAverage = voteAverage
    )

    fun List<PopularItem>.toPopularList() = map {
        it.toUIData()
    }.toList()

    fun NowPlayingItem.toUIData() = NowPlayingModel(
        originalTitle = originalTitle,
        posterPath = posterPath,
        voteAverage = voteAverage,
        price = popularity.toInt()
    )

    fun List<NowPlayingItem>.toNowPlayingList() = map {
        it.toUIData()
    }.toList()

}