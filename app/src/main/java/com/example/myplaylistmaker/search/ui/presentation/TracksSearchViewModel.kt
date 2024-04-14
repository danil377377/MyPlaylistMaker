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
import com.example.myplaylistmaker.search.domain.SearchHistory
import com.example.myplaylistmaker.search.domain.api.TracksInteractor
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.search.ui.models.HistoryState
import com.example.myplaylistmaker.search.ui.models.TracksState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TracksSearchViewModel(
    application: Application
): AndroidViewModel(application), KoinComponent {

    private val tracksInteractor: TracksInteractor by inject()

    private val history = SearchHistory(application)
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

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
    private val historyStateLiveData = MutableLiveData<HistoryState>()
    fun observeState(): LiveData<TracksState> = stateLiveData
    fun observeHistoryState(): LiveData<HistoryState> = historyStateLiveData
    fun hideHistory() {
        historyStateLiveData.postValue(HistoryState.Empty())
    }
    fun getHistoryTrackList(): ArrayList<Track>{
        return history.getHistoryTrackList()
    }
    fun clearHistory() {
        history.clearHistory()
    }
    fun showHistory(tracks: ArrayList<Track>) {
        historyStateLiveData.postValue(HistoryState.Content(tracks))
    }
    fun addToHistory(track: Track){
        history.addToHistory(track)
    }

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
     fun searchRequest(newSearchText: String) {
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
                }
            })
        }
    }
    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }
}