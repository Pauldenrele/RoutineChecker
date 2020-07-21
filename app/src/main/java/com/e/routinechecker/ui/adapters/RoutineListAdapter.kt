package com.example.routinechecks.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e.routinechecker.R

import com.example.routinechecks.data.database.Routine
import kotlinx.android.synthetic.main.list_item_routine.view.*

class RoutineListAdapter(private var routines: List<Routine>, private val clickListener: ItemClickListener ) :
    RecyclerView.Adapter<RoutineListAdapter.RoutineViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_routine, parent, false)
        return RoutineViewHolder(view)

    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val currentRoutine = routines[position]
        holder.bind(currentRoutine)

    }

    //Class to handle click interactions
    sealed class ListenerType {
        class RoutineClickListener(val routine: Routine) : ListenerType()
        class EditClickListener (val routine: Routine) : ListenerType()
    }

    //Class to handle item clicks
    interface ItemClickListener{
        fun onItemClick(routine: Routine, listenerType: ListenerType)
    }

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            val pos = adapterPosition
            val routineClicked = routines[pos]
            var listenerType : ListenerType =
                ListenerType.RoutineClickListener(routineClicked)
            when (v?.id) {
                R.id.editIcon -> listenerType =
                    ListenerType.EditClickListener(routineClicked)
            }
            clickListener.onItemClick(routineClicked, listenerType )
        }

        fun bind(routine: Routine) {
            itemView.routineTitle.text = routine.title

            itemView.routineDescription.text = routine.description
            itemView.editIcon.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }
    }

    internal fun setRoutines(routines: List<Routine>) {
        this.routines = routines
        notifyDataSetChanged()
    }

    override fun getItemCount() = routines.size
}