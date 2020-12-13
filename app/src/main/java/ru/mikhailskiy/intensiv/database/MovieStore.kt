package ru.mikhailskiy.intensiv.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.MovieEntity

@Dao
interface MovieStore {

    @Query("SELECT * FROM movies")
    fun getAll(): Observable<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id=:id")
    fun getById(id: Int): Single<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieEntity: MovieEntity): Completable

    @Delete
    fun delete(movieEntity: MovieEntity): Completable
}