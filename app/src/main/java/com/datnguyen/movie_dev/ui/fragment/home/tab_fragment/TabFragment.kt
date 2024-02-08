package com.datnguyen.movie_dev.ui.fragment.home.tab_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.databinding.FragmentHomeTabBinding
import com.datnguyen.movie_dev.extras.enums.HomeType
import com.datnguyen.movie_dev.ui.activity.secondary.SecondaryActivity
import com.datnguyen.movie_dev.ui.base.BaseFragment
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import com.datnguyen.movie_dev.ui.fragment.details.DetailsFragment
import com.datnguyen.movie_dev.utils.Utils

class TabFragment : BaseFragment() {
    companion object {
        const val KEY_HOME_TYPE = "KEY_HOME_TYPE"

        fun createIntent(homeType: HomeType): TabFragment {
            //create bundle
            val bundle = Bundle()
            bundle.putSerializable(KEY_HOME_TYPE, homeType)
            //create fragment and set bundle
            val fragment = TabFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    var homeType: HomeType = HomeType.POPULAR
    private lateinit var tabViewModel: TabViewModel
    private lateinit var tabViewModelFactory: TabViewModelFactory


    private val binding: FragmentHomeTabBinding get() = (_binding as FragmentHomeTabBinding?)!!

    override fun loadPassedParamsIfNeeded(extras: Bundle) {
        super.loadPassedParamsIfNeeded(extras)
        homeType = Utils.getSerializable(extras, KEY_HOME_TYPE, HomeType::class.java)
    }

    override fun setLayoutXml(): Int {
        return R.layout.fragment_home_tab
    }

    override fun initUI() {

    }

    override fun initViewModel() {
        tabViewModelFactory = TabViewModelFactory(homeType, this)
        tabViewModel = ViewModelProvider(this, tabViewModelFactory)[TabViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = tabViewModel


        //display loading progress
        tabViewModel.loading.observe(this, Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })
        //display empty layout
        tabViewModel.showEmptyEvent.observe(this, Observer {
            binding.llEmptyLayout.visibility = if (it) View.VISIBLE else View.GONE
        })

        //display refresh indicator
        tabViewModel.showRefreshEvent.observe(this, Observer {
            binding.swipeRefresh.isRefreshing = it
        })

        //item click event
        tabViewModel.itemClickEvent.observe(this) {
            context?.let { context ->
                val movie = it as Movie
                //open details screen
                val screen = DetailsFragment::class.java.name
                val intent = SecondaryActivity.createIntent(context)
                //bundle
                val bundle = Bundle()
                bundle.putParcelable(DetailsFragment.KEY_MOVIE, movie)
                //pass the screen key to bundle
                intent.putExtra(SecondaryActivity.KEY_SCREEN, screen)
                intent.putExtra(SecondaryActivity.KEY_BUNDLE, bundle)
                //start activity
                startActivity(intent)
            }
        }
    }

    override fun initData() {

    }

    override fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            tabViewModel.onRefresh()
        }
    }
}