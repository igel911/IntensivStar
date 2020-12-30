package ru.mikhailskiy.intensiv.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.mikhailskiy.intensiv.database.models.MovieEntity

@Dao
interface MovieStore {

    @Query("SELECT * FROM movies")
    fun getAll(): Single<List<MovieEntity>>

    @Query("SELECT COUNT (*) FROM movies ")
    fun getRowCount() : Single<Int>

    @Query("SELECT * FROM movies WHERE id=:id")
    fun getById(id: Int): Single<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieEntity: MovieEntity): Completable

    @Delete
    fun delete(movieEntity: MovieEntity): Completable
}