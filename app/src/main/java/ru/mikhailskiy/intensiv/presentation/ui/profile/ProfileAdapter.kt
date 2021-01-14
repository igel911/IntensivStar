package ru.mikhailskiy.intensiv.presentation.ui.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.mikhailskiy.intensiv.presentation.ui.watchlist.WatchlistFragment

class ProfileAdapter(fragment: Fragment, private val itemsCount: Int) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return WatchlistFragment.newInstance()
    }
}