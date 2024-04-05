package com.example.myplaylistmaker.search.ui

import com.example.myplaylistmaker.search.domain.SearchHistory
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.presentation.ui.PlayerActivity
import com.example.myplaylistmaker.search.ui.models.TracksState
import com.example.myplaylistmaker.search.ui.presentation.TracksSearchViewModel
import com.google.gson.Gson




class SearchActivity : ComponentActivity() {
    companion object {
        const val INPUT = "input"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var editTextValue: String? = null
    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView

    private lateinit var retryButton: Button
    private lateinit var nothingFound: LinearLayout
    private lateinit var internetProblems: LinearLayout
    private lateinit var historyLinearLayout: LinearLayout
    private lateinit var progressBar: ProgressBar
    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var viewModel: TracksSearchViewModel

    private lateinit var tracksAdapter: TrackAdapter


    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)
        viewModel = ViewModelProvider(this, TracksSearchViewModel.getViewModelFactory())[TracksSearchViewModel::class.java]
        val history = SearchHistory(this)
        tracksAdapter = TrackAdapter(
        ) {
            if (clickDebounce()) {
                history.addToHistory(it)

                val playerActivityIntent = Intent(this, PlayerActivity::class.java)
                playerActivityIntent.putExtra("track", createJsonFromTrack(it))
                startActivity(playerActivityIntent)
            }
        }
        val backButton = findViewById<ImageView>(R.id.back_button)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
        retryButton = findViewById<Button>(R.id.retry)
        nothingFound = findViewById<LinearLayout>(R.id.nothing_found)
        internetProblems = findViewById<LinearLayout>(R.id.internet_problems)
        inputEditText = findViewById<EditText>(R.id.input_text)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = tracksAdapter
        progressBar = findViewById(R.id.progressBar)
        val historyRecyclerView = findViewById<RecyclerView>(R.id.historyRecyclerView)
        historyLinearLayout = findViewById<LinearLayout>(R.id.historyLinearLayout)
        val historyAdapter = TrackAdapter() {
            if (clickDebounce()) {
                val playerActivityIntent = Intent(this, PlayerActivity::class.java)
                playerActivityIntent.putExtra("track", createJsonFromTrack(it))
                startActivity(playerActivityIntent)
            }
        }
        historyRecyclerView.adapter = historyAdapter
        fun updateHistoryAdapter() {
            val newHistoryList = history.getHistoryTrackList()
            historyAdapter.updateTracks(newHistoryList)
            historyAdapter.notifyDataSetChanged()
        }

//надо сделать изменение хранение и запрос historyList через LiveData в TracksSearchViewModel



        fun hideHistoryLayout() {
            historyLinearLayout.visibility = View.GONE
        }
        if (historyLinearLayout.visibility == View.VISIBLE) recyclerView.visibility =
            View.GONE else recyclerView.visibility = View.VISIBLE
        backButton.setOnClickListener {
            onBackPressed()
        }
        clearButton.setOnClickListener {
            internetProblems.setVisibility(View.GONE)
            nothingFound.setVisibility(View.GONE)
            hideHistoryLayout()
            inputEditText.setText("")

            tracksAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            inputEditText.clearFocus()

        }
        clearHistoryButton.setOnClickListener {

            history.clearHistory()
            updateHistoryAdapter()
            historyLinearLayout.visibility = View.GONE
        }
        retryButton.setOnClickListener {
            viewModel.searchRequest(inputEditText.text.toString())
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty() && history.getHistoryTrackList() != ArrayList<Track>()) {
                updateHistoryAdapter()
                historyLinearLayout.visibility = View.VISIBLE

            } else {
                historyLinearLayout.visibility = View.GONE
            }
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
viewModel.searchRequest(inputEditText.text.toString())
                hideHistoryLayout()
                true
            }
            false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                internetProblems.setVisibility(View.GONE)
                nothingFound.setVisibility(View.GONE)

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isEmpty() == true && history.getHistoryTrackList() != ArrayList<Track>()) {

                   updateHistoryAdapter()
                    historyLinearLayout.visibility = View.VISIBLE
                    recyclerView.setVisibility(View.GONE)
                }

                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                    updateHistoryAdapter()
                    historyLinearLayout.visibility = View.VISIBLE
                    recyclerView.setVisibility(View.GONE)

                } else historyLinearLayout.visibility = View.GONE
                editTextValue = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                if (s?.isNotEmpty() == true && inputEditText.hasFocus()) {
                    searchDebounce(editTextValue!!)
                    hideHistoryLayout()
                }


            }

            override fun afterTextChanged(s: Editable?) {

                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)



        viewModel.observeState().observe(this) {
            render(it)
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT, editTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)


        val savedInput = savedInstanceState.getString(INPUT)
        inputEditText.setText(savedInput)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }






    private fun searchDebounce(text:String) {
        viewModel.searchDebounce(text)
    }


    private fun createJsonFromTrack(track: Track): String {
        val gson = Gson()
        return gson.toJson(track)
    }





    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Empty -> showEmpty(state.message)
            is TracksState.Error -> showError(state.errorMessage)
            is TracksState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        recyclerView.setVisibility(View.GONE)
        internetProblems.setVisibility(View.GONE)
        nothingFound.setVisibility(View.GONE)
        historyLinearLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        progressBar.visibility = View.GONE
        internetProblems.setVisibility(View.VISIBLE)
        recyclerView.setVisibility(View.GONE)
        nothingFound.setVisibility(View.GONE)
    }

    private fun showEmpty(emptyMessage: String) {
        progressBar.visibility = View.GONE
        recyclerView.setVisibility(View.GONE)
        nothingFound.setVisibility(View.VISIBLE)
        internetProblems.setVisibility(View.GONE)
    }

    private fun showContent(tracks: List<Track>) {
        recyclerView.setVisibility(View.VISIBLE)
        internetProblems.setVisibility(View.GONE)
        nothingFound.setVisibility(View.GONE)
        tracksAdapter.trackList.clear()
        tracksAdapter.trackList.addAll(tracks)
        tracksAdapter.notifyDataSetChanged()
        progressBar.visibility = View.GONE
    }


}

