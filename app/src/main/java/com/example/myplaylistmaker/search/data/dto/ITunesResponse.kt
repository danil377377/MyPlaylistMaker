package com.example.myplaylistmaker.search.data.dto

import com.example.myplaylistmaker.search.domain.models.Track

class ITunesResponse (
   val results: ArrayList<TrackDto>
):Response()