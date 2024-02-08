package com.datnguyen.movie_dev.ui.fragment.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.databinding.FragmentFavoritesBinding
import com.datnguyen.movie_dev.ui.activity.secondary.SecondaryActivity
import com.datnguyen.movie_dev.ui.base.BaseFragment
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import com.datnguyen.movie_dev.ui.fragment.details.DetailsFragment

class FavoritesFragment : BaseFragment() {
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var favoritesViewModelFactory: FavoritesViewModelFactory


    private val binding: FragmentFavoritesBinding get() = (_binding as FragmentFavoritesBinding?)!!
    override fun setLayoutXml(): Int {
        return R.layout.fragment_favorites
    }

    override fun initUI() {
    }

    override fun initViewModel() {
        favoritesViewModelFactory = FavoritesViewModelFactory(this)
        favoritesViewModel = ViewModelProvider(this, favoritesViewModelFactory)[FavoritesViewModel::class.java]

        binding.viewModel = favoritesViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //display loading progress
        favoritesViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })
        //display empty layout
        favoritesViewModel.showEmptyEvent.observe(viewLifecycleOwner, Observer {
            binding.llEmptyLayout.visibility = if (it) View.VISIBLE else View.GONE
        })
        //display refresh indicator
        favoritesViewModel.showRefreshEvent.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
        })

        //item click event
        favoritesViewModel.itemClickEvent.observe(viewLifecycleOwner) {
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
            favoritesViewModel.onRefresh()
        }
    }
}