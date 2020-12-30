package ru.mikhailskiy.intensiv.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.data.dto.CreditsResponseDto
import ru.mikhailskiy.intensiv.data.dto.MovieDetailResponseDto
import ru.mikhailskiy.intensiv.data.dto.MoviesResponseDto
import ru.mikhailskiy.intensiv.data.dto.TvShowsResponseDto


interface MovieApiInterface {

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = LANGUAGE
    ): Single<MoviesResponseDto>

    @GET("movie/upcoming")
    fun getUpComingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = LANGUAGE
    ): Single<MoviesResponseDto>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = LANGUAGE
    ): Single<MoviesResponseDto>

    @GET("tv/popular")
    fun getPopularTvShows(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = LANGUAGE
    ): Single<TvShowsResponseDto>

    @GET("movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = LANGUAGE
    ): Single<MovieDetailResponseDto>

    @GET("movie/{id}/credits")
    fun getCredits(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = LANGUAGE
    ): Single<CreditsResponseDto>

    @GET("search/movie")
    fun searchByQuery(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = LANGUAGE,
        @Query("query") query: String
    ): Single<MoviesResponseDto>

    companion object {
        const val LANGUAGE = "ru"
        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
    }
}
