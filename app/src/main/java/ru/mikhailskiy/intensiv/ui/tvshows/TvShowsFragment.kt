package ru.mikhailskiy.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.TvShow
import ru.mikhailskiy.intensiv.data.TvShowsResponse
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.utils.SingleThreadTransformer
import ru.mikhailskiy.intensiv.utils.showProgressBarOnLoad
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TvShowsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var disposable: Disposable? = null

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tv_shows_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getPopularTvShows = MovieApiClient.apiClient.getPopularTvShows()
        disposable = getPopularTvShows
            .compose(SingleThreadTransformer<TvShowsResponse>())
            .map { it.results ?: emptyList() }
            .flatMapObservable { Observable.fromIterable(it) }
            .map {
                TvShowItem(it) { tvShow ->
                    openTvShowDetails(
                        tvShow
                    )
                }
            }
            .toList()
            .showProgressBarOnLoad(tv_shows_progress_bar, tv_shows_recycler_view)
            .subscribe(
                { tv_shows_recycler_view.adapter = adapter.apply { addAll(it) } },
                { Timber.e(it.toString()) }
            )
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }

    private fun openTvShowDetails(tvShow: TvShow) {

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TvShowsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}