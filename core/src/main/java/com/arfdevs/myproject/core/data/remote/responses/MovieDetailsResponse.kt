package com.arfdevs.myproject.core.data.remote.responses

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MovieDetailsResponse(

    @field:SerializedName("original_language")
    val originalLanguage: String = "",

    @field:SerializedName("imdb_id")
    val imdbId: String = "",

    @field:SerializedName("video")
    val video: Boolean = false,

    @field:SerializedName("title")
    val title: String = "",

    @field:SerializedName("backdrop_path")
    val backdropPath: String = "",

    @field:SerializedName("revenue")
    val revenue: Int = 0,

    @field:SerializedName("genres")
    val genres: List<GenresItem> = listOf(),

    @field:SerializedName("popularity")
    val popularity: Double = 0.0,

    @field:SerializedName("production_countries")
    val productionCountries: List<ProductionCountriesItem> = listOf(),

    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("vote_count")
    val voteCount: Int = 0,

    @field:SerializedName("budget")
    val budget: Int = 0,

    @field:SerializedName("overview")
    val overview: String = "",

    @field:SerializedName("original_title")
    val originalTitle: String = "",

    @field:SerializedName("runtime")
    val runtime: Int = 0,

    @field:SerializedName("poster_path")
    val posterPath: String = "",

    @field:SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguagesItem> = listOf(),

    @field:SerializedName("production_companies")
    val productionCompanies: List<ProductionCompaniesItem> = listOf(),

    @field:SerializedName("release_date")
    val releaseDate: String = "",

    @field:SerializedName("vote_average")
    val voteAverage: Double = 0.0,

    @field:SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection,

    @field:SerializedName("tagline")
    val tagline: String = "",

    @field:SerializedName("adult")
    val adult: Boolean = false,

    @field:SerializedName("homepage")
    val homepage: String = "",

    @field:SerializedName("status")
    val status: String = ""
) : Parcelable

@Keep
@Parcelize
data class ProductionCountriesItem(

    @field:SerializedName("iso_3166_1")
    val iso31661: String = "",

    @field:SerializedName("name")
    val name: String = ""
) : Parcelable

@Keep
@Parcelize
data class SpokenLanguagesItem(

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("iso_639_1")
    val iso6391: String = "",

    @field:SerializedName("english_name")
    val englishName: String = ""
) : Parcelable

@Keep
@Parcelize
data class GenresItem(

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("id")
    val id: Int = 0
) : Parcelable

@Keep
@Parcelize
data class BelongsToCollection(

    @field:SerializedName("backdrop_path")
    val backdropPath: String = "",

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("poster_path")
    val posterPath: String = ""
) : Parcelable

@Keep
@Parcelize
data class ProductionCompaniesItem(

    @field:SerializedName("logo_path")
    val logoPath: String = "",

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("origin_country")
    val originCountry: String = ""
) : Parcelable
