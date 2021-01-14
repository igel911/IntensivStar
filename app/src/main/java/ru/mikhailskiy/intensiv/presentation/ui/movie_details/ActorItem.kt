package ru.mikhailskiy.intensiv.presentation.ui.movie_details

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_actor.*
import kotlinx.android.synthetic.main.item_actor.image_preview
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.dto.ActorDto

class ActorItem(private val content: ActorDto) : Item() {


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.text_view_name.text = content.name?.replace(" ", "\n")

        Picasso.get()
            .load(content.posterPath)
            .placeholder(R.drawable.ic_avatar)
            .into(viewHolder.image_preview)
    }

    override fun getLayout() = R.layout.item_actor


}