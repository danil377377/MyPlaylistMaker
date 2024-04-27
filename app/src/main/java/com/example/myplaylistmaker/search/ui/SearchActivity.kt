package com.example.myplaylistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.player.ui.PlayerActivity
import com.example.myplaylistmaker.search.ui.models.HistoryState
import com.example.myplaylistmaker.search.ui.models.TracksState
import com.example.myplaylistmaker.search.ui.presentation.TracksSearchViewModel
import com.example.myplaylistmaker.utility.App
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchActivity : AppCompatActivity() {
    companion object {
        const val INPUT = "input"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
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
    private lateinit var viewModel :TracksSearchViewModel
    private lateinit var tracksAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
Log.d("MyTest", "onCreateSearchActivity")
        val view:TracksSearchViewModel by viewModel()
        viewModel = view


        setContentView(R.layout.search_layout)

        tracksAdapter = TrackAdapter(
        ) {
            if (clickDebounce()) {
              viewModel.addToHistory(it)
                Log.d("MyTest", "BeforeIntent")
                val playerActivityIntent = Intent(this, PlayerActivity::class.java)
                playerActivityIntent.putExtra("track", it)
                startActivity(playerActivityIntent)
                Log.d("MyTest", "afterIntent")
            }
        }
        historyAdapter = TrackAdapter() {
            if (clickDebounce()) {
                Log.d("MyTest", "BeforeIntent")
                val playerActivityIntent = Intent(this, PlayerActivity::class.java)
                playerActivityIntent.putExtra("track", it)
                startActivity(playerActivityIntent)
                Log.d("MyTest", "afterIntent")
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
        historyRecyclerView.adapter = historyAdapter
        if (historyLinearLayout.visibility == View.VISIBLE) recyclerView.visibility =
            View.GONE else recyclerView.visibility = View.VISIBLE
        backButton.setOnClickListener {
            onBackPressed()
        }
        clearButton.setOnClickListener {
            internetProblems.setVisibility(View.GONE)
            nothingFound.setVisibility(View.GONE)
            recyclerView.setVisibility(View.GONE)
            viewModel.hideHistory()
            inputEditText.setText("")
            tracksAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            inputEditText.clearFocus()
        }
        clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
viewModel.hideHistory()
        }
        retryButton.setOnClickListener {
            viewModel.searchRequest(inputEditText.text.toString())
        }
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty() && viewModel.getHistoryTrackList() != ArrayList<Track>()) {
                viewModel.showHistory(viewModel.getHistoryTrackList())
            } else {
                viewModel.hideHistory()
            }
        }
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
viewModel.searchRequest(inputEditText.text.toString())
                viewModel.hideHistory()
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
                if (s?.isEmpty() == true && viewModel.getHistoryTrackList() != ArrayList<Track>()) {

                    viewModel.showHistory(viewModel.getHistoryTrackList())
                }

                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                    viewModel.showHistory(viewModel.getHistoryTrackList())

                } else historyLinearLayout.visibility = View.GONE
                editTextValue = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                if (s?.isNotEmpty() == true && inputEditText.hasFocus()) {
                    searchDebounce(editTextValue!!)
                    viewModel.hideHistory()
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        viewModel.observeState().observe(this) {
            render(it)
        }
        viewModel.observeHistoryState().observe(this) {
            historyRender(it)
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
    private fun historyRender(state: HistoryState) {
        when (state) {
            is HistoryState.Content -> showContentHistory(state.tracks)
            is HistoryState.Empty -> showEmptyHistory()

        }
    }
    private fun showEmptyHistory() {
        historyLinearLayout.setVisibility(View.GONE)
    }
    private fun showContentHistory(tracks: List<Track>) {
        historyLinearLayout.setVisibility(View.VISIBLE)
        recyclerView.setVisibility(View.GONE)
        historyAdapter.trackList.clear()
        historyAdapter.trackList.addAll(tracks)
        historyAdapter.notifyDataSetChanged()

    }
    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Empty -> showEmpty()
            is TracksState.Error -> showError()
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
    private fun showError() {
        progressBar.visibility = View.GONE
        internetProblems.setVisibility(View.VISIBLE)
        recyclerView.setVisibility(View.GONE)
        nothingFound.setVisibility(View.GONE)
    }
    private fun showEmpty() {
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

