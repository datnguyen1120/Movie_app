package com.datnguyen.movie_dev.ui.fragment.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.databinding.FragmentSearchBinding
import com.datnguyen.movie_dev.ui.activity.secondary.SecondaryActivity
import com.datnguyen.movie_dev.ui.base.BaseFragment
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import com.datnguyen.movie_dev.ui.custom.DebouncingQueryTextListener
import com.datnguyen.movie_dev.ui.fragment.details.DetailsFragment
import com.datnguyen.movie_dev.utils.Utils


class SearchFragment : BaseFragment() {

    private lateinit var searchViewModel: SearchViewModel

    private val binding :FragmentSearchBinding get() = (_binding as FragmentSearchBinding?)!!


    companion object {
        fun createIntent(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun setLayoutXml(): Int {
        return R.layout.fragment_search
    }

    override fun initUI() {
        //set title
        binding.appBar.tvTitle.text = getString(R.string.search_title)
    }

    override fun initViewModel() {
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        binding.viewModel = searchViewModel
        binding.lifecycleOwner = viewLifecycleOwner


        //handle loading layout
        searchViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
                binding.swipeRefresh.isRefreshing = false
            }
        })

        //handle error
        searchViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        //show/hide empty layout
        searchViewModel.showEmpty.observe(viewLifecycleOwner) {
            showNotFound(it)
        }

        //item click event
        searchViewModel.itemClickEvent.observe(viewLifecycleOwner) {
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

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        //back action
        binding.appBar.btnBack.setOnClickListener {
            onBackPressed()
        }

        //handle edit text search
        //DebouncingQueryTextListener -> to delay the listen trigger while user inputs
        binding.edSearch.addTextChangedListener(DebouncingQueryTextListener(lifecycle) {
            it?.let {
                searchViewModel.searchMovie(it, false)
            }
        })
        //handle the search submit
        binding.edSearch.setOnEditorActionListener(OnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchViewModel.searchMovie(view.text.toString(), true)
                Utils.hideSoftKeyboard(activity)
                return@OnEditorActionListener true
            }
            false
        })

        //refresh layout
        binding.swipeRefresh.setOnRefreshListener {
            searchViewModel.searchMovie()
        }

        //hide keyboard when touch outside or on recycle view
        binding.parent.setOnTouchListener { v, event ->
            activity?.let { Utils.hideSoftKeyboard(it) }
            false
        }
        binding.recycleView.setOnTouchListener(OnTouchListener { v, event ->
            activity?.let { Utils.hideSoftKeyboard(it) }
            false
        })
    }

    private fun showNotFound(isShow: Boolean) {
        if (isShow) {
            binding.llEmptyLayout.visibility = View.VISIBLE
        } else {
            binding.llEmptyLayout.visibility = View.GONE
        }
    }
}