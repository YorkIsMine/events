package com.r.events.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener
import com.r.events.R
import com.r.events.adapter.ScreenSlidePagerAdapter
import com.r.events.view.ui.Settings.SettingsFragment
import com.r.events.view.ui.favourites.FavouritesFragment
import com.r.events.view.ui.home.HomeFragment
import com.r.events.view.ui.user_info.UserInfoFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragList = ArrayList<Fragment>()
        fragList.add(HomeFragment())
        fragList.add(FavouritesFragment())
        fragList.add(SettingsFragment())
        fragList.add(UserInfoFragment())

        val pagerAdapter =
            ScreenSlidePagerAdapter(fragList, supportFragmentManager)




        view_pager.setAdapter(pagerAdapter)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
                bottom_navigation_view_linear.setCurrentActiveItem(i)
            }

            override fun onPageScrollStateChanged(i: Int) {


            }

        })

        bottom_navigation_view_linear.setNavigationChangeListener(BubbleNavigationChangeListener { view, position ->
            view_pager.setCurrentItem(
                position,
                true
            )
        })

    }
}
