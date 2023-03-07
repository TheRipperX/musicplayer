package com.example.musicplayer.`class`

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.musicplayer.dataclass.DataMusicList

class ListMusicViewModel: ViewModel() {

    var listMusicLive = ArrayList<DataMusicList>()


    @SuppressLint("Range")
    fun  loadAudio(context: Context) {

        try{

            if (listMusicLive.count() == 0){
                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
                //val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
                val cursor = context.contentResolver.query(uri, null, selection, null, null)


                if (cursor != null && cursor.count > 0) {
                    var numberItems = 0

                    while (cursor.moveToNext()) {
                        val data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                        val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                        //val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                        val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                        val duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

                        var imgAny: Bitmap?

                        val mmr = MediaMetadataRetriever()
                        mmr.setDataSource(data)
                        val dataImage = mmr.embeddedPicture

                        if (dataImage != null) {
                            imgAny = BitmapFactory.decodeByteArray(dataImage, 0, dataImage.size)

                            listMusicLive.add(DataMusicList(numberItems, imgAny, data, title, artist, duration))
                        }

                        numberItems++
                    }

                }
                cursor?.close()
            }


        }catch (e: Exception){
            Toast.makeText(context, "no music", Toast.LENGTH_SHORT).show()
        }

    }

}