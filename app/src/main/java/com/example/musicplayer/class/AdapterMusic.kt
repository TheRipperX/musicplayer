package com.example.musicplayer.`class`

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.OnlineFragment
import com.example.musicplayer.R
import com.example.musicplayer.dataclass.DataMusicList

class AdapterMusic(private val data: List<DataMusicList>, private val context: Context, private val sendInfoMusic: SendInfoMusic): RecyclerView.Adapter<AdapterMusic.ViewListHolder>() {

    inner class ViewListHolder(viewItem: View): RecyclerView.ViewHolder(viewItem) {
        val imageMusic = viewItem.findViewById<ImageView>(R.id.rec_img_music)!!
        val adapterLayoutMusic = viewItem.findViewById<ConstraintLayout>(R.id.adapter_layout_music)!!
        //val playMusic = viewItem.findViewById<ImageView>(R.id.rec_img_play)!!
        val nameMusic = viewItem.findViewById<TextView>(R.id.rec_txt_name)!!
        val decMusic = viewItem.findViewById<TextView>(R.id.rec_txt_dec)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewListHolder {
        return ViewListHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_rec_list_music, parent, false))
    }

    override fun onBindViewHolder(holder: ViewListHolder, position: Int) {
        val itemView = data[position]

        //holder.imageMusic.setImageResource(itemView.imgMusic)
        holder.nameMusic.text = itemView.name
        holder.decMusic.text = itemView.decription

        holder.imageMusic.setImageBitmap(itemView.imgMusic)
        //holder.imageMusic.setImageResource(R.drawable.mu4)

        //holder.playMusic.setImageResource(R.drawable.play)

        val hashData = HashMap<String, Any>()
        hashData["dataList"] = itemView
        //hashData["dataImage"] = holder.playMusic

        holder.adapterLayoutMusic.setOnClickListener {
            sendInfoMusic.senInfoFragment(itemView)
        }


    }

    override fun getItemCount(): Int {
        return data.count()
    }

    interface SendInfoMusic {
        fun senInfoFragment(dataList: DataMusicList)
    }
}