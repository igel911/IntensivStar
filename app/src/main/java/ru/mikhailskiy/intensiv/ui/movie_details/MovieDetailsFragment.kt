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
import kotlinx.android.synthetic.main.movie_details_fragment.*
import kotlinx.android.synthetic.main.movie_details_fragment.movie_rating
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.CreditsResponse
import ru.mikhailskiy.intensiv.data.MovieDetailResponse
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment
import timber.log.Timber


class MovieDetailsFragment : Fragment() {

    private var title: String? = null
    private var rating: Float? = null
    private var posterPath: String? = null
    private var overview: String? = null
    private var id: Int? = null

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

        back_arrow_movie.setOnClickListener {
            findNavController().popBackStack()
        }

        title_text_view.text = title
        movie_rating.rating = rating ?: 0.0f
        text_movie_desc.text = overview


        Picasso.get()
            .load(posterPath)
            .into(image_movie)

        val getMovieDetails =
            MovieApiClient.apiClient.getMovieDetails(id ?: 0, FeedFragment.API_KEY, "ru")
        getMovieDetails.enqueue(object : Callback<MovieDetailResponse> {

            override fun onFailure(call: Call<MovieDetailResponse>, error: Throwable) {
                Timber.e(error.toString())
            }

            override fun onResponse(
                call: Call<MovieDetailResponse>,
                detailResponse: Response<MovieDetailResponse>
            ) {
                val details = detailResponse.body()
                details?.let {
                    text_view_studio.text = it.companyNamesString
                    text_view_genre.text = it.genresString
                    text_view_year.text = it.year
                }
            }
        })

        val getCredits = MovieApiClient.apiClient.getCredits(id ?: 0, FeedFragment.API_KEY, "ru")
        getCredits.enqueue(object : Callback<CreditsResponse> {
            override fun onResponse(
                call: Call<CreditsResponse>,
                response: Response<CreditsResponse>
            ) {
                val actors = response.body()?.actors
                actors?.let { list ->
                    val actorItemList = list.map {
                        ActorItem(it)
                    }.toList()

                    recycler_view_actors.adapter = adapter.apply { addAll(actorItemList) }
                }
            }

            override fun onFailure(call: Call<CreditsResponse>, error: Throwable) {
                Timber.e(error.toString())
            }

        })
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