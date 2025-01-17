package com.zygotecnologia.zygotv.domain.model

import com.squareup.moshi.Json

data class Show(
    val genres: List<Genre>?,
    @Json(name = "original_name")
    val original_name: String?,
    @Json(name = "genre_ids")
    val genreIds: List<Int>?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "vote_count")
    val voteCount: Int?,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "original_language")
    val originalLanguage: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "overview")
    val overview: String?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "popularity")
    val popularity: Double,
    @Json(name = "title")
    val title: String?

)
