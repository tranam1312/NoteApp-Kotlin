package com.example.noteapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.model.Model

class NoteFirebasAdapter(private val context: Context, private val onLongClickListener: (Model) -> Unit,
                         private val onClickListener: (Model) -> Unit): RecyclerView.Adapter<NoteFirebasAdapter.NoteViewHordel>() {
    private var notes :MutableList<Model> = mutableListOf()
    var hide:Boolean = false
    private  var selected :ArrayList<Model> = ArrayList()
    inner class NoteViewHordel(itemView: View):RecyclerView.ViewHolder(itemView){
        var txtTitle: TextView = itemView.findViewById(R.id.title_firebase)
        var txtContainer: TextView = itemView.findViewById(R.id.container_firebase)
        var txtTime : TextView = itemView.findViewById(R.id.date_firebase)
        var checkBox: CheckBox = itemView.findViewById(R.id.checked_firebase)
        var layoyItem : LinearLayout = itemView.findViewById(R.id.layotitem_firebase)
        fun noteFire( model: Model){
            txtTitle.text = model.title
            txtContainer.text = model.container
            txtTime.text = model.time
            if (hide == true){
                checkBox.visibility = View.VISIBLE
                checkBox.isChecked = model.check

            }
            else{
                checkBox.visibility = View.GONE
            }

            layoyItem.setOnLongClickListener {
                onLongClickListener(model)
                hide = true
                notifyDataSetChanged()
                true
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHordel {
        val itemView:View = LayoutInflater.from(context).inflate(R.layout.item_notefirebase,null,false)
        return NoteViewHordel(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHordel, position: Int) {
            holder.noteFire(notes[position])
            holder.itemView.setOnClickListener {
                if (hide){
                    if (!holder.checkBox.isChecked){
                        notes[position].check = true
                        selected.add(notes[position])
                        Log.d("nma",selected.toString())
                        notifyDataSetChanged()
                    }
                    else{
                        notes[position].check= false
                        selected.remove(notes[position])
                        Log.d("jjj",selected.toString())
                        notifyDataSetChanged()
                    }
                }else{
                    onClickListener(notes[position])
                }
            }
    }

    override fun getItemCount(): Int {
       if (notes.size != null){
           return notes.size
       }
        return 0
    }
    fun setNoteAdapterFibase(notes:MutableList<Model>){
        this.notes = notes
        notifyDataSetChanged()
    }
    fun getSelected(): ArrayList<Model>{
        return selected
    }
    fun removeSelect(){
        selected = ArrayList()
    }
    fun setHideCheckBox(){
        hide = false
        notifyDataSetChanged()
    }
}