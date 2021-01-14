package ru.mikhailskiy.intensiv.presentation.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.repository.PopularTvShowsRepository
import ru.mikhailskiy.intensiv.data.vo.TvShow
import ru.mikhailskiy.intensiv.domain.usecase.PopularTvShowsUseCase

class TvShowsFragment : Fragment(), TvShowsPresenter.TvShowView {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val presenter: TvShowsPresenter by lazy {
        TvShowsPresenter(PopularTvShowsUseCase(PopularTvShowsRepository()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tv_shows_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)
        presenter.getTvShows()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun openTvShowDetails(tvShow: TvShow) {
        Toast.makeText(context, tvShow.name, Toast.LENGTH_SHORT).show()
    }

    override fun showTvShows(tvShows: List<TvShowItem>) {
        tv_shows_recycler_view.adapter = adapter.apply { addAll(tvShows) }
    }

    override fun showLoading() {
        tv_shows_recycler_view.visibility = View.GONE
        tv_shows_progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        tv_shows_recycler_view.visibility = View.VISIBLE
        tv_shows_progress_bar.visibility = View.GONE
    }

    override fun showEmptyTvShows() {
        tv_shows_recycler_view.adapter = adapter.apply { addAll(listOf()) }
    }

    override fun showError() {

    }
}