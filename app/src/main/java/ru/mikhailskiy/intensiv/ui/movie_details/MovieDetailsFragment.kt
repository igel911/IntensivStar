package ru.mikhailskiy.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.mikhailskiy.intensiv.R


class MovieDetailsFragment : Fragment() {

    private var title: String? = null
    private var rating: Float? = null
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE)
            rating = it.getFloat(RATING)
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

        // TODO: Получать из модели
        val actorTempList =
            listOf(Actor("First Actor", ""), Actor("Second Actor", ""), Actor("Third Actor", ""))

        val actorList = actorTempList.map {
            ActorItem(it)
        }.toList()

        recycler_view_actors.adapter = adapter.apply { addAll(actorList) }

        // TODO: Получать из модели
        text_movie_desc.text =
            "In 1985 Maine, lighthouse keeper Thomas Curry rescues Atlanna, the queen of the underwater nation of Atlantis, during a storm. They eventually fall in love and have a son named Arthur, who is born with the power to communicate with marine lifeforms. "
        text_view_studio.text = "Warner Bros."
        text_view_genre.text = "Action, Adventure, Fantasy"
        text_view_year.text = "2018"
    }

    companion object {
        const val TITLE = "title"
        const val RATING = "rating"

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