package com.example.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.`class`.AdapterMusic
import com.example.musicplayer.`class`.ListMusicViewModel
import com.example.musicplayer.`class`.PermissionClass
import com.example.musicplayer.databinding.FragmentListMusicBinding
import com.example.musicplayer.dataclass.DataMusicList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListMusicFragment : Fragment(), AdapterMusic.SendInfoMusic {

    private lateinit var binding: FragmentListMusicBinding

    private lateinit var mediaPlayer: MediaPlayer
    private var musicDuration = 0
    private var endMusicDuration = 0

    private lateinit var adapterMusic: AdapterMusic

    private lateinit var permissionClass: PermissionClass

    private lateinit var listViewModel: ListMusicViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListMusicBinding.inflate(inflater, container, false)
        main()
        return binding.root
    }


    private fun main() {
        listViewModel = ViewModelProvider(this).get(ListMusicViewModel::class.java)
        mediaPlayer = MediaPlayer()

        permissionClass = PermissionClass(requireContext())
        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE



        if (!permissionClass.permissionFun(readPermission)) {

            if (!permissionClass.permissionFun(writePermission)) {
                Toast.makeText(requireContext(), "is Permanently Denied...", Toast.LENGTH_SHORT).show()
                return
            }
        }
        CoroutineScope(Dispatchers.Default).launch {

            listMusicLiveDataMain()

        }

    }

    private suspend fun listMusicLiveDataMain(){

        listViewModel.loadAudio(requireContext())

        adapterMusic = AdapterMusic(listViewModel.listMusicLive, requireContext(), this)

        withContext(Dispatchers.Main){

            binding.progressBarListmusic.visibility = View.GONE
            binding.recyclerViewMusic.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewMusic.adapter = adapterMusic
        }
    }


    override fun senInfoFragment(dataList: DataMusicList) {
        playMusicAdapter(dataList)
    }

    override fun onPause() {
        mediaPlayer.reset()
        binding.imgBtnPlay.setImageResource(R.drawable.play)
        binding.seekMusicList.progress = 0
        super.onPause()
    }

    override fun onDestroy() {
        mediaPlayer.reset()
        binding.imgBtnPlay.setImageResource(R.drawable.play)
        binding.seekMusicList.progress = 0
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    private fun lenMusicMin(dur: Int) {


        var ss = dur / 1000
        val mm = ss / 60
        ss -= mm * 60

        if (mm >= 10) {
            binding.txtTimeList.text = "$mm:$ss"
        } else {
            binding.txtTimeList.text = "0$mm:$ss"
        }
    }


    private fun playMusicAdapter(list: DataMusicList) {

        binding.imgBtnPlay.isClickable = true

        lenMusicMin(list.durationMusic)

        binding.imgPlayerMusic.setImageBitmap(list.imgMusic)
        //binding.imgPlayerMusic.setImageResource(R.drawable.mu4)

        mediaPlayer.reset()
        binding.seekMusicList.progress = 0
        mediaPlayer.setDataSource(list.uriMusic)
        mediaPlayer.prepare()
        mediaPlayer.start()

        musicDuration = mediaPlayer.duration

        binding.seekMusicList.max = mediaPlayer.duration

        binding.seekMusicList.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                if (fromUser)
                    mediaPlayer.seekTo(progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })


        binding.imgBtnPlay.setImageResource(R.drawable.puase)

        CoroutineScope(Dispatchers.Default).launch {
            if (mediaPlayer.isPlaying)
                seekBarMediaPlayer()
        }


        binding.imgBtnPlay.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                binding.imgBtnPlay.setImageResource(R.drawable.play)
            } else {
                mediaPlayer.start()
                binding.imgBtnPlay.setImageResource(R.drawable.puase)
            }
        }


    }


    private suspend fun seekBarMediaPlayer() {

        CoroutineScope(Dispatchers.Default).launch {

            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (e: Exception) {

                }
                withContext(Dispatchers.Main) {

                    binding.seekMusicList.progress = mediaPlayer.currentPosition
                    endMusicDuration = mediaPlayer.currentPosition

                    if (endMusicDuration + 20 >= musicDuration) {

                        mediaPlayer.reset()
                        binding.imgBtnPlay.setImageResource(R.drawable.play)
                        binding.imgPlayerMusic.setImageResource(R.drawable.mu4)
                        binding.seekMusicList.progress = 0

                    }
                }

            }
        }
    }

}
