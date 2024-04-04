package com.example.myplaylistmaker.search.ui.presentation

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.imdbtraining.utility.Creator
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.search.domain.api.TracksInteractor
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.search.ui.models.TracksState

class TracksSearchViewModel(
    application: Application
): AndroidViewModel(application) {

    private val tracksInteractor = Creator.provideTracksInteractor(getApplication<Application>())

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TracksSearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }
    private val tracks = ArrayList<Track>()

    private val handler = Handler(Looper.getMainLooper())


    private var lastSearchText: String? = null
    private var latestSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText)
    }

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    override fun onCleared() {
        handler.removeCallbacksAndMessages(searchRunnable)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                TracksState.Loading
            )

            tracksInteractor.searchTracks(newSearchText, object : TracksInteractor.TracksConsumer {
                override fun consume(foundMovies: List<Track>?, errorMessage: String?) {
                    handler.post {

                        if (foundMovies != null) {
                            tracks.addAll(foundMovies)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    TracksState.Error(
                                        errorMessage = getApplication<Application>().getString(R.string.something_went_wrong),
                                    )
                                )

                            }

                            tracks.isEmpty() -> {
                                renderState(
                                    TracksState.Empty(
                                        message = getApplication<Application>().getString(R.string.nothing_found),
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    TracksState.Content(
                                        tracks = tracks,
                                    )
                                )
                            }
                        }

                    }
                }
            })
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }


}