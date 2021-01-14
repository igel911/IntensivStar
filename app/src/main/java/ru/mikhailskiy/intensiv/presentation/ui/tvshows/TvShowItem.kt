package ru.mikhailskiy.intensiv.presentation.ui.tvshows

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_tv_shows.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.vo.TvShow

class TvShowItem(
    private val content: TvShow,
    private val onClick: (tvShow: TvShow) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_tv_shows

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.title_text_view.text = content.name
        viewHolder.movie_rating.rating = content.rating
        viewHolder.content.setOnClickListener {
            onClick.invoke(content)
        }

        Picasso.get()
            .load(content.posterFullPath)
            .into(viewHolder.image_preview)
    }
}