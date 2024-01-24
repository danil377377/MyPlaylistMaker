package com.example.myplaylistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class SearchActivity : AppCompatActivity() {
    private var editTextValue: String? = null
    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var tracksAdapter: TrackAdapter
    private lateinit var retryButton:Button
    private lateinit var nothingFound:LinearLayout
    private lateinit var internetProblems:LinearLayout


    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)


        recyclerView = findViewById(R.id.recyclerView)


        tracksAdapter = TrackAdapter(
            tracks)

        recyclerView.adapter = tracksAdapter





        val backButton = findViewById<ImageView>(R.id.back_button)

         retryButton = findViewById<Button>(R.id.retry)
         nothingFound = findViewById<LinearLayout>(R.id.nothing_found)
         internetProblems = findViewById<LinearLayout>(R.id.internet_problems)
        inputEditText = findViewById<EditText>(R.id.input_text)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        backButton.setOnClickListener {
            onBackPressed()
        }
        clearButton.setOnClickListener {
            inputEditText.setText("")
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
        }
        retryButton.setOnClickListener {
            search()
        }
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                search()
                true
            }
            false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                editTextValue = s.toString()
                clearButton.visibility = clearButtonVisibility(s)

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
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

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    companion object {
        const val INPUT = "input"

    }

    private val ITunesBaseUrl ="https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(ITunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val ITunesService = retrofit.create(ITunesApi::class.java)
    private val tracks = ArrayList<Track>()
    private fun search(){
        ITunesService.search(inputEditText.text.toString())
            .enqueue(object : Callback<ITunesResponse> {
                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: retrofit2.Response<ITunesResponse>
                ) {
                    when (response.code()){
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true){
                                Log.d("Deb", response.body()?.results.toString())
                                recyclerView.setVisibility(View.VISIBLE)
                                internetProblems.setVisibility(View.GONE)
                                nothingFound.setVisibility(View.GONE)
                                tracks.clear()
                                tracks.addAll(response.body()?.results!!)
                                tracksAdapter.notifyDataSetChanged()
                                showMessage("","")
                            } else {
                                showMessage("Ничего не найдено","")
                                recyclerView.setVisibility(View.GONE)
                                nothingFound.setVisibility(View.VISIBLE)
                                internetProblems.setVisibility(View.GONE)
                                Log.d("Deb", response.body()?.results.toString())
                            }
                        }
                        else -> {
                            showMessage("Что-то пошло не так", response.code().toString())
                            internetProblems.setVisibility(View.VISIBLE)
                            recyclerView.setVisibility(View.GONE)
                            nothingFound.setVisibility(View.GONE)
                        }
                    }
                }
                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    showMessage("Что-то пошло не так", t.message.toString())
                    internetProblems.setVisibility(View.VISIBLE)
                    recyclerView.setVisibility(View.GONE)
                    nothingFound.setVisibility(View.GONE)
                }
            })
    }
    private fun showMessage(text: String, additionalText: String) {
        if (text.isNotEmpty()){
                Toast.makeText(this,additionalText, Toast.LENGTH_LONG)
                    .show()
            }

    }

}
