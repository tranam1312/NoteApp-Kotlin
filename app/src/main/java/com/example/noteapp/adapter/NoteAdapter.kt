package com.example.noteapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.model.Note
import java.text.SimpleDateFormat


class NoteAdapter(private val context: Context,private val onLongClickListener: (Note) -> Unit,
                  private val onClickListener: (Note) -> Unit): RecyclerView.Adapter<NoteAdapter.NoteViewHordel>() {
    private var notes :List<Note> = listOf()
    var hide:Boolean = false
    private var selected = mutableListOf<Note>()
inner class NoteViewHordel(itemView: View):RecyclerView.ViewHolder(itemView) {
    var txtTitle: TextView = itemView.findViewById(R.id.title)

        var txtContainer:TextView = itemView.findViewById(R.id.container)
    var txtTime :TextView = itemView.findViewById(R.id.date)
    var checkBox:CheckBox= itemView.findViewById(R.id.checked)

    var layoyItem :ConstraintLayout = itemView.findViewById(R.id.layouttitem)
    fun Note(note: Note) {
        txtTitle.text = note.title
        txtContainer.text = note.container
        txtTime.text = note.time
        if (hide == true){
            checkBox.visibility = View.VISIBLE
            checkBox.isChecked = note.check
        }
        else{
            checkBox.visibility = View.GONE
        }
        if (note.backGround !=null){
            layoyItem.setBackgroundResource(note.backGround)
        }

        layoyItem.setOnLongClickListener {
            onLongClickListener(note)
            hide = true
            notifyDataSetChanged()
            true
        }
    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHordel {
        val itemView = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        return NoteViewHordel(itemView)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHordel, position: Int) {
        holder.Note(notes[position])
        holder.itemView.setOnClickListener {
            if (hide){
                if (!holder.checkBox.isChecked){
                    notes[position].check = true
                    selected.add(notes[position])
                    Log.d("nma", selected.toString())
                    notifyDataSetChanged()
                }
                else{
                    notes[position].check= false
                    selected.remove(notes[position])
                    Log.d("jjj", selected.toString())
                    notifyDataSetChanged()
                }
            }else{
                onClickListener(notes[position])
            }
        }

        }
        fun setNoteAdapter(notes:List<Note>) {
            this.notes = notes
            notifyDataSetChanged()
        }

        fun getSelected(): MutableList<Note> {
            return selected
        }

        fun removeSelect() {
            selected = mutableListOf()
        }

        fun setHideCheckBox() {
            hide = false
            notifyDataSetChanged()
        }


    }