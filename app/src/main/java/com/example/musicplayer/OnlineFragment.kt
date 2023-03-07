package com.example.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.musicplayer.`class`.PermissionClass
import com.example.musicplayer.databinding.FragmentOnlineBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OnlineFragment : Fragment() {

    private lateinit var binding: FragmentOnlineBinding
    private var playBoolean = true
    private var isPlaying = false
    private lateinit var mediaPlayer: MediaPlayer
    private var lenMusic = 0
    private var endMusic = 0

    private lateinit var animImge: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineBinding.inflate(inflater, container, false)
        main()
        return binding.root
    }

    private fun main() {

        val permissionClass = PermissionClass(requireContext())

        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        permissionClass.permissionFun(readPermission)
        permissionClass.permissionFun(writePermission)

        animImge = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_img_music)
        mediaPlayer = MediaPlayer()

        btnClick()
        durationBtnClick()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    override fun onPause() {
        if (mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }
        super.onPause()
    }

    private fun btnClick() {

        binding.imgBtnPlay.setOnClickListener {


            val edtLink = binding.edtLinkPlay.text.toString()
            if (edtLink.isEmpty()){
                Toast.makeText(requireContext(), "No link is play", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {

                CoroutineScope(Dispatchers.Default).launch {
                    playMusicBtnPly(edtLink)
                }


            }catch (e: Exception){
                print(e.message)
            }

        }

        binding.imgBtnDelLink.setOnClickListener {
            binding.edtLinkPlay.text.clear()
            binding.imgBtnPlay.setImageResource(R.drawable.play)
            if (mediaPlayer.isPlaying){
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            playBoolean = true
            isPlaying = false
            binding.cirImgMusic.clearAnimation()
        }

    }

    private suspend fun seekBarMain(){
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                try {
                    Thread.sleep(1000)
                }catch (e: Exception){

                }
                withContext(Dispatchers.Main){
                    binding.seekBarMusic.progress = mediaPlayer.currentPosition

                    endMusic = mediaPlayer.currentPosition

                    if (endMusic == lenMusic){

                        binding.imgBtnPlay.setImageResource(R.drawable.play)
                        if (mediaPlayer.isPlaying){
                            mediaPlayer.stop()
                        }
                        mediaPlayer.reset()
                        playBoolean = true
                        isPlaying = false
                        binding.cirImgMusic.clearAnimation()
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun txtDuration() {
        var ss = lenMusic / 1000
        val mm = ss / 60
        ss -= mm * 60

        if (mm >=10){
            binding.txtLenMusic.text = "$mm:$ss"
        }else{
            binding.txtLenMusic.text = "0$mm:$ss"
        }

    }

    private fun durationBtnClick(){

        binding.imgBtnNext.setOnClickListener {
            if (mediaPlayer.isPlaying){
                mediaPlayer.seekTo(mediaPlayer.currentPosition + 10000)
            }

        }

        binding.imgBtnBack.setOnClickListener {
            if (mediaPlayer.isPlaying){
                mediaPlayer.seekTo(mediaPlayer.currentPosition - 10000)
            }
        }
    }

    private suspend fun playMusicBtnPly(link: String){


        if (playBoolean){

            if (!isPlaying){

                withContext(Dispatchers.Main){
                    binding.imgBtnPlay.setImageResource(R.drawable.puase)
                    binding.cirImgMusic.startAnimation(animImge)
                }

                mediaPlayer.setDataSource(link)
                mediaPlayer.prepare()
                lenMusic = mediaPlayer.duration
                binding.seekBarMusic.max = lenMusic
                mediaPlayer.start()
                withContext(Dispatchers.Main){
                    txtDuration()
                }
                seekBarMain()
                isPlaying = true
                playBoolean = false

            }else{

                withContext(Dispatchers.Main){
                    binding.imgBtnPlay.setImageResource(R.drawable.puase)
                    binding.cirImgMusic.startAnimation(animImge)
                }
                binding.imgBtnPlay.setImageResource(R.drawable.puase)
                mediaPlayer.start()
                isPlaying = true
                playBoolean = false

            }

        }else{
            withContext(Dispatchers.Main){
                binding.imgBtnPlay.setImageResource(R.drawable.play)
                binding.cirImgMusic.clearAnimation()
            }

            mediaPlayer.pause()
            isPlaying = true
            playBoolean = true
        }
    }







}