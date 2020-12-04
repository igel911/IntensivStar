package ru.mikhailskiy.intensiv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.mikhailskiy.intensiv.BuildConfig.THE_MOVIE_DATABASE_API
import ru.mikhailskiy.intensiv.data.CreditsResponse
import ru.mikhailskiy.intensiv.data.MovieDetailResponse
import ru.mikhailskiy.intensiv.data.MoviesResponse
import ru.mikhailskiy.intensiv.data.TvShowsResponse


interface MovieApiInterface {

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = "ru"
    ): Call<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpComingMovies(
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = "ru"
    ): Call<MoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = "ru"
    ): Call<MoviesResponse>

    @GET("tv/popular")
    fun getPopularTvShows(
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = "ru"
    ): Call<TvShowsResponse>

    @GET("movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = "ru"
    ): Call<MovieDetailResponse>

    @GET("movie/{id}/credits")
    fun getCredits(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = "ru"
    ): Call<CreditsResponse>

    @GET("search/movie")
    fun searchByQuery(
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = "ru",
        @Query("query") query: String
    ): Call<MoviesResponse>
}
