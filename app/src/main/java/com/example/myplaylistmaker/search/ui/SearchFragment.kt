package com.example.myplaylistmaker.search.ui


import android.content.Context

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText

import android.widget.LinearLayout
import android.widget.ProgressBar

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R

import com.example.myplaylistmaker.databinding.FragmentSearchBinding

import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.search.ui.models.HistoryState
import com.example.myplaylistmaker.search.ui.models.TracksState
import com.example.myplaylistmaker.search.ui.presentation.TracksSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        const val INPUT = "input"
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        const val TAG = "SearchFragment"

        fun newInstance(): Fragment {
            return SearchFragment()
        }
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
    private lateinit var viewModel: TracksSearchViewModel
    private lateinit var tracksAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val view: TracksSearchViewModel by viewModel()
        viewModel = view
        tracksAdapter = TrackAdapter(
        ) {
            if (clickDebounce()) {
                val bundle = Bundle().apply {
                    putSerializable("track", it)
                }
                viewModel.addToHistory(it)
                Log.d("MyTest", "BeforeIntent")
                findNavController().navigate(R.id.action_searchFragment_to_playerActivity, bundle)

                Log.d("MyTest", "afterIntent")
            }
        }
        historyAdapter = TrackAdapter() {
            if (clickDebounce()) {
                Log.d("MyTest", "BeforeIntent")
                val bundle = Bundle().apply {
                    putSerializable("track", it)
                }
                findNavController().navigate(R.id.action_searchFragment_to_playerActivity, bundle)
                Log.d("MyTest", "afterIntent")
            }
        }
        val clearHistoryButton = binding.clearHistoryButton
        retryButton = binding.retry
        nothingFound = binding.nothingFound
        internetProblems = binding.internetProblems
        inputEditText = binding.inputText
        val clearButton = binding.clearIcon
        recyclerView = binding.recyclerView
        recyclerView.adapter = tracksAdapter
        progressBar = binding.progressBar
        val historyRecyclerView = binding.historyRecyclerView
        historyLinearLayout = binding.historyLinearLayout
        historyRecyclerView.adapter = historyAdapter

        binding.linearLayout.setOnClickListener {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            inputEditText.clearFocus()
        }




        if (historyLinearLayout.visibility == View.VISIBLE) recyclerView.visibility =
            View.GONE else recyclerView.visibility = View.VISIBLE
        clearButton.setOnClickListener {
            internetProblems.setVisibility(View.GONE)
            nothingFound.setVisibility(View.GONE)
            recyclerView.setVisibility(View.GONE)
            viewModel.hideHistory()
            inputEditText.setText("")
            tracksAdapter.notifyDataSetChanged()
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            inputEditText.clearFocus()
        }
        clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            viewModel.hideHistory()
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            inputEditText.clearFocus()
        }
        retryButton.setOnClickListener {
            viewModel.searchRequest(inputEditText.text.toString())
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            lifecycleScope.launch {
                if (hasFocus && inputEditText.text.isEmpty() && viewModel.getHistoryTrackList().isNotEmpty()) {
                    viewModel.showHistory(viewModel.getHistoryTrackList())
                } else {
                    viewModel.hideHistory()
                }
            }
        }
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchRequest(inputEditText.text.toString())
                viewModel.hideHistory()
                inputEditText.clearFocus()
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

                lifecycleScope.launch {

                    if (s.isNullOrEmpty() && inputEditText.hasFocus() && viewModel.getHistoryTrackList().isNotEmpty()) {
                        viewModel.showHistory(viewModel.getHistoryTrackList())
                    } else {
                        viewModel.hideHistory()
                    }
                }
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
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeHistoryState().observe(viewLifecycleOwner) {
            historyRender(it)
        }


    }



    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
        historyAdapter.trackList.clear()
        historyAdapter.trackList.addAll(viewModel.getHistoryTrackList())
            tracksAdapter.trackList.clear()
            viewModel.searchRequest(viewModel.latestSearchText?:"check")

        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT, editTextValue)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val savedInput = savedInstanceState?.getString(INPUT) ?: ""
        inputEditText.setText(savedInput)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
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

    private fun searchDebounce(text: String) {
        viewModel.searchDebounce(text)
    }

    private fun historyRender(state: HistoryState) {
        when (state) {
            is HistoryState.Content -> showContentHistory(state.tracks)
            is HistoryState.Empty -> showEmptyHistory()

            else -> {}
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
            else -> {}
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


