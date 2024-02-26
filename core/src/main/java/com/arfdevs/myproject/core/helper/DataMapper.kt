package com.arfdevs.myproject.core.helper

import com.arfdevs.myproject.core.data.remote.responses.PopularItem
import com.arfdevs.myproject.core.domain.model.PopularModel

object DataMapper {

    fun PopularItem.toUIData() = PopularModel(
        originalTitle = originalTitle,
        posterPath = posterPath,
        voteAverage = voteAverage
    )

    fun List<PopularItem>.toListData() = map {
        it.toUIData()
    }.toList()


}