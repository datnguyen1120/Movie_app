package com.datnguyen.movie_dev.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.databinding.FragmentHomeBinding
import com.datnguyen.movie_dev.extras.enums.HomeType
import com.datnguyen.movie_dev.ui.base.BaseFragment
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import com.datnguyen.movie_dev.ui.fragment.home.tab_fragment.TabFragment
import kotlinx.coroutines.*

class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel

    private val binding: FragmentHomeBinding get() = (_binding as FragmentHomeBinding?)!!      //=> only can get. no set

    private val tabPopular = TabFragment.createIntent(HomeType.POPULAR)
    private val tabNowPlaying = TabFragment.createIntent(HomeType.NOW_PLAYING)
    private val tabUpcoming = TabFragment.createIntent(HomeType.UPCOMING)
    private val tabTopRated = TabFragment.createIntent(HomeType.TOP_RATED)
    private var activeFragment: Fragment = tabPopular

    override fun setLayoutXml(): Int {
        return R.layout.fragment_home
    }

    override fun initUI() {
        //init fragment for bottom nav
        childFragmentManager.beginTransaction().apply {
            if (!tabPopular.isAdded) {
                add(R.id.home_container, tabPopular, getString(R.string.home_popular))
            }
            if (!tabNowPlaying.isAdded) {
                add(R.id.home_container, tabNowPlaying, getString(R.string.home_now_playing)).hide(tabNowPlaying)
            }
            if (!tabUpcoming.isAdded) {
                add(R.id.home_container, tabUpcoming, getString(R.string.home_upcoming)).hide(tabUpcoming)
            }
            if (!tabTopRated.isAdded) {
                add(R.id.home_container, tabTopRated, getString(R.string.home_top_rated)).hide(tabTopRated)
            }
        }.commit()
    }

    override fun initViewModel() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun initData() {

    }

    override fun initListener() {
        //handle bottom navigation click
        binding.navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_popular -> {
                    childFragmentManager.beginTransaction().hide(activeFragment).show(tabPopular).commit()
                    activeFragment = tabPopular
                    true
                }
                R.id.action_now_playing -> {
                    childFragmentManager.beginTransaction().hide(activeFragment).show(tabNowPlaying).commit()
                    activeFragment = tabNowPlaying
                    true
                }
                R.id.action_upcoming -> {
                    childFragmentManager.beginTransaction().hide(activeFragment).show(tabUpcoming).commit()
                    activeFragment = tabUpcoming
                    true
                }
                R.id.action_top_rated -> {
                    childFragmentManager.beginTransaction().hide(activeFragment).show(tabTopRated).commit()
                    activeFragment = tabTopRated
                    true
                }
                else -> false
            }
        }
    }
}