package com.example.noteapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.model.BackGround

class AdapterBackground(private val context: Context, val onClickListener: (BackGround)->Unit): RecyclerView.Adapter<AdapterBackground.BackgroundViewHolder>() {
    var  x:Int = 1000

    private lateinit var  itemBackground :ArrayList<BackGround>
    inner class BackgroundViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
       var imgBackground:ImageView = itemView.findViewById(R.id.item_background)
        var layoutBackGround:ConstraintLayout = itemView.findViewById(R.id.layout_item_background)
        fun BackGround(backGround: BackGround){
            imgBackground.setImageResource(backGround.recosun)

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundViewHolder {
        val  view  = LayoutInflater.from(context).inflate(R.layout.background_item,parent,false)
        return BackgroundViewHolder(view)
    }


    override fun onBindViewHolder(holder: BackgroundViewHolder, position: Int) {
      holder.BackGround(itemBackground[position])
        holder.layoutBackGround.setOnClickListener {
            if (x!=position) {
                Log.d("x","$x")
                holder.imgBackground.setBackgroundResource(R.drawable.backgroud_click)
                onClickListener(itemBackground[position])
                notifyItemChanged(x)
                notifyItemChanged(position)
                x = position
            Log.d("kk", "${x}")}
        }
    }

    override fun getItemCount(): Int {
        return itemBackground.size
    }
    fun setArrImgBack(imgBackground: ArrayList<BackGround>){
        this.itemBackground = imgBackground
        notifyDataSetChanged()
    }


}

