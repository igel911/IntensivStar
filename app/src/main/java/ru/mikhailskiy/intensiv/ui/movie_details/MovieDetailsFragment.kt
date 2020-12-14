package ru.mikhailskiy.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable.fromIterable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.CreditsResponse
import ru.mikhailskiy.intensiv.data.MovieDetailResponse
import ru.mikhailskiy.intensiv.data.MovieEntity
import ru.mikhailskiy.intensiv.database.MovieDatabase
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.utils.SingleThreadTransformer
import ru.mikhailskiy.intensiv.utils.showProgressBarOnLoad
import ru.mikhailskiy.intensiv.utils.subscribeOnIoObserveOnMain
import timber.log.Timber


class MovieDetailsFragment : Fragment() {

    private var title: String? = null
    private var rating: Float? = null
    private var posterPath: String? = null
    private var overview: String? = null
    private var id: Int? = null
    private val compositeDisposable = CompositeDisposable()

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE)
            rating = it.getFloat(RATING)
            posterPath = it.getString(POSTER_PATH)
            overview = it.getString(OVERVIEW)
            id = it.getInt(ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.movie_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie_back_arrow.setOnClickListener {
            findNavController().popBackStack()
        }

        title_text_view.text = title
        movie_rating.rating = rating ?: 0.0f
        overview_movie_text_view.text = overview


        Picasso.get()
            .load(posterPath)
            .into(movie_image)

        val getMovieDetails =
            MovieApiClient.apiClient.getMovieDetails(id ?: 0)
        compositeDisposable.add(
            getMovieDetails
                .compose(SingleThreadTransformer<MovieDetailResponse>())
                .showProgressBarOnLoad(details_progress_bar, studio_text_view)
                .subscribe(
                    {
                        it?.let {
                            studio_text_view.text = it.companyNamesString
                            genre_text_view.text = it.genresString
                            year_text_view.text = it.year
                        }
                    },
                    { Timber.e(it.toString()) }
                )
        )

        val getCredits = MovieApiClient.apiClient.getCredits(id ?: 0)
        compositeDisposable.add(getCredits
            .compose(SingleThreadTransformer<CreditsResponse>())
            .map { it.actors ?: emptyList() }
            .flatMapObservable { fromIterable(it) }
            .map { ActorItem(it) }
            .toList()
            .showProgressBarOnLoad(actors_progress_bar, actors_recycler_view)
            .subscribe(
                { actors_recycler_view.adapter = adapter.apply { addAll(it) } },
                { Timber.e(it.toString()) }
            )
        )

        val movieStore = MovieDatabase.getDatabase(requireContext()).movies()

        movie_like_button.setOnCheckedChangeListener { compoundButton, isChecked ->
            val movieEntity = MovieEntity(id ?: 0, overview ?: "", posterPath ?: "", title ?: "", rating ?: 0.0F)

            if (isChecked) {
                movieStore.insert(movieEntity)
                    .subscribeOnIoObserveOnMain()
                    .subscribe()
            } else {
                movieStore.delete(movieEntity)
                    .subscribeOnIoObserveOnMain()
                    .subscribe()
            }
        }

        compositeDisposable.add(movieStore.getById(id ?: 0)
            .compose(SingleThreadTransformer())
            .subscribe(
                {
                    movie_like_button.isChecked = true
                },
                { Timber.e(it.toString()) }
            ))
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    companion object {
        const val TITLE = "title"
        const val RATING = "rating"
        const val OVERVIEW = "overview"
        const val ID = "id"
        const val POSTER_PATH = "posterPath"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, param1)
                    putString(RATING, param2)
                }
            }
    }
}