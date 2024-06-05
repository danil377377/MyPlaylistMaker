package com.example.myplaylistmaker.search.ui.presentation

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myplaylistmaker.search.domain.SearchHistory
import com.example.myplaylistmaker.search.domain.api.TracksInteractor
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.search.ui.models.HistoryState
import com.example.myplaylistmaker.search.ui.models.TracksState
import com.example.myplaylistmaker.utility.App
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

class TracksSearchViewModel(
    application: Application,
    val tracksInteractor: TracksInteractor,
) : AndroidViewModel(application) {


    private val history = SearchHistory()

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }

    private val tracks = ArrayList<Track>()
    private var lastSearchText: String? = null
    private var latestSearchText: String? = null
    private var searchJob: Job? = null
    private val stateLiveData = MutableLiveData<TracksState>()
    private val historyStateLiveData = MutableLiveData<HistoryState>()
    fun observeState(): LiveData<TracksState> = stateLiveData
    fun observeHistoryState(): LiveData<HistoryState> = historyStateLiveData
    fun hideHistory() {
        historyStateLiveData.postValue(HistoryState.Empty())
    }

    fun getHistoryTrackList(): ArrayList<Track> {
        return history.getHistoryTrackList()
    }

    fun clearHistory() {
        history.clearHistory()
    }

    fun showHistory(tracks: ArrayList<Track>) {
        historyStateLiveData.postValue(HistoryState.Content(tracks))
    }

    fun addToHistory(track: Track) {
        history.addToHistory(track)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        this.lastSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                TracksState.Loading
            )
            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(newSearchText)
                    .collect { pair ->

                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundMovies: List<Track>?, errorMessage: String?) {
        tracks.clear()
        if (foundMovies != null) {
            tracks.addAll(foundMovies)
        }
        when {
            errorMessage != null -> {
                renderState(
                    TracksState.Error()
                )
            }

            tracks.isEmpty() -> {
                renderState(
                    TracksState.Empty()
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

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }
}