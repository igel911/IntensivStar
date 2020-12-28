package ru.mikhailskiy.intensiv.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_watchlist.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.database.MovieDatabase
import ru.mikhailskiy.intensiv.utils.SingleThreadTransformer
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class WatchlistFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var disposable: Disposable? = null
    val adapter by lazy {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movies_recycler_view.layoutManager = GridLayoutManager(context, 4)
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        val movieStore = MovieDatabase.getDatabase(requireContext()).movies()

        disposable = movieStore.getAll()
            .flatMapObservable { Observable.fromIterable(it) }
            .map {
                MoviePreviewItem(
                    it
                ) { movie -> }
            }
            .toList()
            .compose(SingleThreadTransformer())
            .subscribe(
                { movies_recycler_view.adapter = adapter.apply { addAll(it) } },
                { Timber.e(it.toString()) }
            )
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            WatchlistFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}