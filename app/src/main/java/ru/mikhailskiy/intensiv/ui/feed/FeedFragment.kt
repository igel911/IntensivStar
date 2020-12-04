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
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.Movie
import ru.mikhailskiy.intensiv.data.MoviesResponse
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.afterTextChanged
import ru.mikhailskiy.intensiv.ui.movie_details.MovieDetailsFragment
import timber.log.Timber

class FeedFragment : Fragment() {

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

        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > 3) {
                openSearch(it.toString())
            }
        }

        val getTopRatedMovies = MovieApiClient.apiClient.getTopRatedMovies()
        getTopRatedMovies.enqueue(getMovieCallback(R.string.recommended))

        val getUpcomingMovies = MovieApiClient.apiClient.getUpComingMovies()
        getUpcomingMovies.enqueue(getMovieCallback(R.string.upcoming))

        val getPopularMovies = MovieApiClient.apiClient.getPopularMovies()
        getPopularMovies.enqueue(getMovieCallback(R.string.popular))

    }

    private fun getMovieCallback(@StringRes stringId: Int) = object : Callback<MoviesResponse> {

        override fun onFailure(call: Call<MoviesResponse>, error: Throwable) {
            // Логируем ошибку
            Timber.e(error.toString())
        }

        override fun onResponse(
            call: Call<MoviesResponse>,
            response: Response<MoviesResponse>
        ) {

            val movies = response.body()?.results
            // Передаем результат в adapter и отображаем элементы
            movies?.let { loadedMovies ->
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
        }
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
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
    }
}