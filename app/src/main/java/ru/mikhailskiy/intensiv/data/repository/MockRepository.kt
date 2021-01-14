package ru.mikhailskiy.intensiv.data.repository

import ru.mikhailskiy.intensiv.data.dto.MovieDto

object MockRepository {

    fun getMovies(): List<MovieDto> {

        val moviesList = mutableListOf<MovieDto>()
        /*for (x in 0..10) {
            val movie = Movie(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x
            )
            moviesList.add(movie)
        }*/

        return moviesList
    }

    fun getTvShows(): List<MovieDto> {

        val moviesList = mutableListOf<MovieDto>()
        /*for (x in 0..10) {
            val movie = Movie(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x
            )
            moviesList.add(movie)
        }*/

        return moviesList
    }
}