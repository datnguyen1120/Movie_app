package com.datnguyen.movie_dev.ui.fragment.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.datnguyen.movie_dev.MyApplication
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.databinding.FragmentDetailsBinding
import com.datnguyen.movie_dev.ui.activity.secondary.SecondaryActivity
import com.datnguyen.movie_dev.ui.base.BaseFragment
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import com.datnguyen.movie_dev.ui.fragment.signin.SignInFragment
import com.datnguyen.movie_dev.ui.fragment.trailer_view.TrailerViewDialog
import com.datnguyen.movie_dev.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class DetailsFragment : BaseFragment() {
    private var _detailsViewModel: DetailsViewModel? = null
    private lateinit var detailsViewModelFactory: DetailsViewModelFactory

    
    private val binding: FragmentDetailsBinding get() = (_binding as FragmentDetailsBinding?)!!
    private val detailsViewModel get() = _detailsViewModel!!

    var movie: Movie? = null

    companion object {
        const val KEY_MOVIE = "KEY_MOVIE"

        fun createIntent(): DetailsFragment {
            return DetailsFragment()
        }
    }

    override fun loadPassedParamsIfNeeded(extras: Bundle) {
        super.loadPassedParamsIfNeeded(extras)
        movie = Utils.getParcelable(extras, KEY_MOVIE, Movie::class.java)
    }

    override fun setLayoutXml(): Int {
        return R.layout.fragment_details
    }

    override fun initUI() {
        //set title
        binding.appBar.tvTitle.text = getString(R.string.details_title)
        //set mark icon
        binding.appBar.btnRight.visibility = View.VISIBLE
    }

    override fun initViewModel() {
        detailsViewModelFactory = DetailsViewModelFactory(movie)
        _detailsViewModel = ViewModelProvider(this, detailsViewModelFactory)[DetailsViewModel::class.java]


        binding.viewModel = _detailsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //handle loading layout
        detailsViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })

        detailsViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        detailsViewModel.loadMovieDetailsEvent.observe(viewLifecycleOwner) {
            updateInfo(it)
        }

        detailsViewModel.displayTrailer.observe(viewLifecycleOwner) {
            binding.btnPlay.visibility = if (it == true) View.VISIBLE else View.INVISIBLE
        }

        detailsViewModel.markFavoriteEvent.observe(viewLifecycleOwner) {
            updateFavoriteState()
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
        //mark favorite movie
        binding.appBar.btnRight.setOnClickListener {
            markFavoriteMovie()
        }

        //btn play trailer
        binding.btnPlay.setOnClickListener {
            detailsViewModel.trailerVideo?.key?.let {
                val dialog = TrailerViewDialog.createIntent(it)
                dialog.show(parentFragmentManager, TrailerViewDialog::class.java.name)
            }
        }
    }

    private fun updateInfo(movie: Movie?) {
        movie?.let {
            context?.let { context ->
                //cover image
                Glide.with(context)
                    .load(Utils.getImageUrl(movie.backdropPath))
                    .override(Utils.convertDPtoPixel(375), Utils.convertDPtoPixel(210))
                    .centerCrop()
                    .into(binding.imvCover)

                //avatar
                Glide.with(context)
                    .load(Utils.getImageUrl(movie.posterPath))
                    .override(Utils.convertDPtoPixel(100), Utils.convertDPtoPixel(126))
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))      //round corner
                    .into(binding.imvAvatar)
            }

            //title
            binding.tvMovieTitle.text = movie.title

            //released date
            binding.tvReleaseYear.text = movie.releaseDate?.subSequence(0, 4)

            //released status
            binding.tvStatus.text = movie.status

            //first genres
            binding.tvGenres.text = if (movie.genres?.isNotEmpty() == true) movie.genres[0].name else "Unknown"

            //rating score
            binding.ratingBar.rating = (movie.voteAverage?.toFloat()?.div(2)) ?: 0f     //total rate is 10 point
            binding.tvRatingScore.text = movie.voteAverage.toString()

            //overview
            binding.tvOverview.text = movie.overview

            //favorite
            updateFavoriteState();
        }
    }

    private fun updateFavoriteState() {
        val isFavorite = detailsViewModel.movie?.isMarkedFavorite ?: false
        if (isFavorite) {
            binding.appBar.btnRight.setImageResource(R.drawable.ic_bookmark_filled)
        } else {
            binding.appBar.btnRight.setImageResource(R.drawable.ic_bookmark_outline)
        }
    }

    private fun markFavoriteMovie() {
        val session = MyApplication.instance().session
        session?.let {
            //show dialog to confirm
            context?.let { context ->
                val isFavorite = detailsViewModel.movie?.isMarkedFavorite ?: false
                val msg = if (isFavorite) getString(R.string.favorite_remove_movie_from_fav) else getString(R.string.favorite_add_movie_to_fav)
                MaterialAlertDialogBuilder(context)
                    .setMessage(msg)
                    .setNegativeButton(
                        getString(R.string.no)
                    ) { dialog, which ->

                    }
                    .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        detailsViewModel.markFavorite()
                    }
                    .show()
            }
        } ?: kotlin.run {
            //show dialog ask sign in
            context?.let { context ->
                MaterialAlertDialogBuilder(context)
                    .setTitle(getString(R.string.login_session_requirement))
                    .setMessage(getString(R.string.login_require_msg))
                    .setNegativeButton(
                        getString(R.string.no)
                    ) { dialog, which ->

                    }
                    .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        //open sign in screen
                        val screen = SignInFragment::class.java.name
                        val intent = SecondaryActivity.createIntent(context)
                        //pass the screen key to bundle
                        intent.putExtra(SecondaryActivity.KEY_SCREEN, screen)
                        //start activity
                        startActivity(intent)
                    }
                    .show()
            }
        }
    }
}