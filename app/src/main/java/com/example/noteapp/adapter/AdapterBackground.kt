package com.example.noteapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.model.BackGround

class AdapterBackground(private val context: Context): RecyclerView.Adapter<AdapterBackground.BackgroundViewHolder>() {

private lateinit var  itemBackground :ArrayList<BackGround>
    inner class BackgroundViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
       var imgBackground:ImageView = itemView.findViewById(R.id.item_background_3)
        fun BackGround(backGround: BackGround){
            imgBackground.setImageResource(backGround.recosun)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundViewHolder {
        val  itemView = LayoutInflater.from(context).inflate(R.layout.backgound_bottom_sheet,parent,false)
        return BackgroundViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BackgroundViewHolder, position: Int) {
      holder.BackGround(itemBackground[position])
    }

    override fun getItemCount(): Int {
        if(itemBackground != null){
            return itemBackground.size
        }
         return 0
    }
    fun setArrImgBack(imgBackground: ArrayList<BackGround>){
        this.itemBackground = imgBackground
        notifyDataSetChanged()
    }


}

