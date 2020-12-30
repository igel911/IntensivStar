package ru.mikhailskiy.intensiv.presentation.ui.feed

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable.fromIterable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.dto.MovieDto
import ru.mikhailskiy.intensiv.data.dto.MoviesResponseDto
import ru.mikhailskiy.intensiv.data.network.MovieApiClient
import ru.mikhailskiy.intensiv.presentation.ui.movie_details.MovieDetailsFragment
import ru.mikhailskiy.intensiv.utils.SingleThreadTransformer
import ru.mikhailskiy.intensiv.utils.showProgressBarOnLoad
import ru.mikhailskiy.intensiv.utils.subscribeOnIoObserveOnMain
import timber.log.Timber

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

        val searchDisposable = search_toolbar.search()
            .subscribeOnIoObserveOnMain()
            .subscribe(
                {
                    openSearch(it.toString())
                },
                { Timber.e(it.toString()) })

        compositeDisposable.add(searchDisposable)


        val getTopRatedMovies = MovieApiClient.apiClient.getTopRatedMovies()
        val topRatedMovies = createMoviesWithTitleSingle(getTopRatedMovies, R.string.recommended)

        val getUpcomingMovies = MovieApiClient.apiClient.getUpComingMovies()
        val upcomingMovies = createMoviesWithTitleSingle(getUpcomingMovies, R.string.upcoming)

        val getPopularMovies = MovieApiClient.apiClient.getPopularMovies()
        val popularMovies = createMoviesWithTitleSingle(getPopularMovies, R.string.popular)

        addMovieZipObserver(topRatedMovies, upcomingMovies, popularMovies)
    }

    private fun createMoviesWithTitleSingle(
        movieSingle: Single<MoviesResponseDto>,
        @StringRes stringId: Int
    ): Single<MainCardContainer> = movieSingle
        .map { it.results ?: emptyList() }
        .flatMapObservable { fromIterable(it) }
        .map {
            MovieItem(it) { movie ->
                openMovieDetails(
                    movie
                )
            }
        }
        .toList()
        .map { MainCardContainer(stringId, it) }

    private fun addMovieZipObserver(
        topRatedMovies: Single<MainCardContainer>,
        upcomingMovies: Single<MainCardContainer>,
        popularMovies: Single<MainCardContainer>
    ): Disposable {
        return Single
            .zip(
                topRatedMovies,
                upcomingMovies,
                popularMovies,
                { topRated, upcoming, popular -> listOf(topRated, upcoming, popular) })
            .compose(SingleThreadTransformer<List<MainCardContainer>>())
            .showProgressBarOnLoad(movies_progress_bar, movies_recycler_view)
            .subscribe(
                { movies_recycler_view.adapter = adapter.apply { addAll(it) } },
                { Timber.e(it.toString()) }
            )
    }


    private fun openMovieDetails(movie: MovieDto) {
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