package com.arfdevs.myproject.core.data.remote.responses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Parcelize
data class NowPlayingResponse(

	@field:SerializedName("dates")
	val dates: Dates,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("results")
	val results: List<NowPlayingItem>,

	@field:SerializedName("total_results")
	val totalResults: Int
) : Parcelable

@Keep
@Parcelize
data class Dates(

	@field:SerializedName("maximum")
	val maximum: String,

	@field:SerializedName("minimum")
	val minimum: String
) : Parcelable

@Keep
@Parcelize
data class NowPlayingItem(

	@field:SerializedName("overview")
	val overview: String,

	@field:SerializedName("original_language")
	val originalLanguage: String,

	@field:SerializedName("original_title")
	val originalTitle: String,

	@field:SerializedName("video")
	val video: Boolean,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("genre_ids")
	val genreIds: List<Int>,

	@field:SerializedName("poster_path")
	val posterPath: String,

	@field:SerializedName("backdrop_path")
	val backdropPath: String,

	@field:SerializedName("release_date")
	val releaseDate: String,

	@field:SerializedName("popularity")
	val popularity: Double,

	@field:SerializedName("vote_average")
	val voteAverage: Double,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("adult")
	val adult: Boolean,

	@field:SerializedName("vote_count")
	val voteCount: Int
) : Parcelable
