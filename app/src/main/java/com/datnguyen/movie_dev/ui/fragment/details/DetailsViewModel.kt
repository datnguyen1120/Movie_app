package com.datnguyen.movie_dev.ui.fragment.details

import com.datnguyen.movie_dev.MyApplication
import com.datnguyen.movie_dev.app_model.Cast
import com.datnguyen.movie_dev.app_model.Crew
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.app_model.Video
import com.datnguyen.movie_dev.extras.LiveEvent
import com.datnguyen.movie_dev.services.api_repository.FavoriteRepository
import com.datnguyen.movie_dev.services.api_repository.MovieRepository
import com.datnguyen.movie_dev.services.api_service.FavoriteService
import com.datnguyen.movie_dev.services.api_service.MovieService
import com.datnguyen.movie_dev.services.model.CreditsResponse
import com.datnguyen.movie_dev.services.model.FavoriteMovieRequest
import com.datnguyen.movie_dev.ui.adapter.casts.CastsAdapter
import com.datnguyen.movie_dev.ui.adapter.crews.CrewsAdapter
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import com.datnguyen.movie_dev.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Integer.min

class DetailsViewModel(var movie: Movie?) : BaseViewModel() {
    var movieRepository: MovieRepository
    var favoriteRepository: FavoriteRepository
    val loadMovieDetailsEvent = LiveEvent<Movie?>()
    val markFavoriteEvent = LiveEvent<Boolean>()

    val crews: MutableList<Crew?> = arrayListOf()
    val crewAdapter: CrewsAdapter = CrewsAdapter(crews)

    val casts: MutableList<Cast?> = arrayListOf()
    val castAdapter: CastsAdapter = CastsAdapter(casts)

    val displayTrailer = LiveEvent<Boolean?>()
    var trailerVideo: Video? = null

    init {
        val movieService = MovieService.getInstance()
        movieRepository = MovieRepository(movieService)

        val favoriteService = FavoriteService.getInstance()
        favoriteRepository = FavoriteRepository(favoriteService)

        loadMovieDetails()
    }

    private fun loadMovieDetails() {
        loading.postValue(true)
        movie?.id?.let {
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = movieRepository.getMovieDetails(it)
                withContext(Dispatchers.Main) {
                    if (response.success != false) {
                        movie = response
                        loadMovieDetailsEvent.postValue(response)

                        //load movie's credits
                        loadCredits()

                        //load videos
                        loadVideos()

                        //get video state
                        getMovieState()
                    } else {
                        onError(response.statusMessage)
                    }
                    loading.postValue(false)
                }
            }
        }
    }

    private fun loadCredits() {
        movie?.id?.let {
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = movieRepository.getMovieCredits(it)
                withContext(Dispatchers.Main) {
                    if (response.success != false) {
                        addDataToAdapters(response)
                    } else {
                        onError(response.statusMessage)
                    }
                }
            }
        }
    }

    private fun addDataToAdapters(responses: CreditsResponse) {
        //add crews into CrewsAdapter
        responses.crew?.let {
            //clear
            crews.clear()
            //get 4 first items
            val max = min(it.size, 4)
            //add items into adapter
            for (i in 0 until max) {
                crews.add(it[i])
            }
            //update list from 0-3
            crewAdapter.notifyItemRangeInserted(0, max - 1)
        }

        //add casts into CastsAdapter
        responses.cast?.let {
            //clear
            casts.clear()
            //get 4 first items
            val max = min(it.size, 4)
            //add items into adapter
            for (i in 0 until max) {
                casts.add(it[i])
            }
            //update list from 0-3
            castAdapter.notifyItemRangeInserted(0, max - 1)
        }
    }

    private fun loadVideos() {
        movie?.id?.let {
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = movieRepository.getMovieVideos(it)
                withContext(Dispatchers.Main) {
                    if (response.success != false) {
                        val videos = response.results
                        if (videos?.isNotEmpty() == true) {
                            trailerVideo = Utils.getTrailerVideo(videos)     //find the newest Youtube trailer
                            displayTrailer.postValue(true)
                        } else {
                            displayTrailer.postValue(false)
                        }
                    } else {
                        displayTrailer.postValue(false)
                    }
                }
            }
        }
    }

    fun markFavorite() {
        movie?.id?.let {
            loading.postValue(true)

            val accountId = MyApplication.instance().session?.accountId
            val isFavorite = !(movie?.isMarkedFavorite ?: false)
            accountId?.let {
                //only using the movie type
                val request = FavoriteMovieRequest(isFavorite, movie?.id, "movie")
                job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                    val response = favoriteRepository.markFavorite(accountId, request)
                    withContext(Dispatchers.Main) {
                        loading.postValue(false)
                        markFavoriteEvent.postValue(true)

                        //update local
                        movie?.isMarkedFavorite = isFavorite
                    }
                }
            }
        }
    }

    private fun getMovieState() {
        movie?.id?.let {
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = favoriteRepository.getMovieState(it)
                withContext(Dispatchers.Main) {
                    if (response.success != false) {
                        movie?.isMarkedFavorite = response.favorite ?: false
                        loadMovieDetailsEvent.postValue(movie)
                    }
                }
            }
        }
    }
}