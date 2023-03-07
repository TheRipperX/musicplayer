package com.example.musicplayer.dataclass

import android.graphics.Bitmap

data class DataMusicList(
    val num: Int,
    val imgMusic: Bitmap,
    val uriMusic: String,
    val name: String,
    val decription: String,
    val durationMusic: Int
)