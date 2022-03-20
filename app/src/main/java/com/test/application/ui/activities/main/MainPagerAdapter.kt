package com.test.application.ui.activities.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class MainPagerAdapter(fm: FragmentActivity,
                       private val pages: List<PagerModel>) :
    androidx.viewpager2.adapter.FragmentStateAdapter(fm){


    override fun getItemCount(): Int {
       return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = pages[position].fragment
        return fragment
    }

    data class PagerModel( val fragment: Fragment,val  title: String)

}
