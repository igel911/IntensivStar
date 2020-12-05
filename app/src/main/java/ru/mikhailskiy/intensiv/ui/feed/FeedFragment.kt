package ru.mikhailskiy.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.Movie
import ru.mikhailskiy.intensiv.data.MoviesResponse
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.afterTextChanged
import ru.mikhailskiy.intensiv.ui.movie_details.MovieDetailsFragment
import ru.mikhailskiy.intensiv.utils.ApiSingleTransformer
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FeedFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Добавляем recyclerView
        movies_recycler_view.layoutManager = LinearLayoutManager(context)
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        val searchDisposable = Observable.create(ObservableOnSubscribe<String> { emitter ->
            search_toolbar.search_edit_text.afterTextChanged {
                if (!emitter.isDisposed) {
                    emitter.onNext(it.toString())
                }
            }
        })
            .map { it.replace(" ", "") }
            .filter { it.length > 3 }
            .debounce(1, TimeUnit.SECONDS)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    openSearch(it.toString())
                },
                { Timber.e(it.toString()) })

        compositeDisposable.add(searchDisposable)


        val getTopRatedMovies = MovieApiClient.apiClient.getTopRatedMovies()
        compositeDisposable.add(addMovieObserver(getTopRatedMovies, R.string.recommended))

        val getUpcomingMovies = MovieApiClient.apiClient.getUpComingMovies()
        compositeDisposable.add(addMovieObserver(getUpcomingMovies, R.string.upcoming))

        val getPopularMovies = MovieApiClient.apiClient.getPopularMovies()
        compositeDisposable.add(addMovieObserver(getPopularMovies, R.string.popular))
    }

    private fun addMovieObserver(
        movieObservable: Single<MoviesResponse>,
        @StringRes stringId: Int
    ): Disposable {
        return movieObservable
            .compose(ApiSingleTransformer<MoviesResponse>())
            .subscribe(
                { response ->
                    // Передаем результат в adapter и отображаем элементы
                    response.results?.let { loadedMovies ->
                        val moviesList = listOf(
                            MainCardContainer(
                                stringId,
                                loadedMovies.map {
                                    MovieItem(it) { movie ->
                                        openMovieDetails(
                                            movie
                                        )
                                    }
                                }.toList()
                            )
                        )
                        movies_recycler_view.adapter = adapter.apply { addAll(moviesList) }
                    }
                },
                {
                    // Логируем ошибку
                    Timber.e(it.toString())
                }
            )
    }

    private fun openMovieDetails(movie: Movie) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putString(MovieDetailsFragment.TITLE, movie.title)
        bundle.putFloat(MovieDetailsFragment.RATING, movie.rating)
        bundle.putString(MovieDetailsFragment.POSTER_PATH, movie.posterPath)
        bundle.putString(MovieDetailsFragment.OVERVIEW, movie.overview)
        bundle.putInt(MovieDetailsFragment.ID, movie.id ?: 0)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun openSearch(searchText: String) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putString("search", searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
        compositeDisposable.clear()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
    }
}