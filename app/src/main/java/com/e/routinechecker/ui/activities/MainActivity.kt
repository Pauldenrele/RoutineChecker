package com.e.routinechecker.ui.activities

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.routinechecker.R
import com.example.routinechecks.ui.viewModels.NewRoutineViewModel

import com.example.routinechecks.ui.adapters.RoutineListAdapter
import com.example.routinechecks.data.database.Routine
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_routine.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), RoutineListAdapter.ItemClickListener {

    //ViewModel member variable
    private lateinit var mViewModel: NewRoutineViewModel
 //List of routines
    private lateinit var mRoutines: List<Routine>

    //Request code
    companion object {
        const val newRoutineActivityRequestCode = 1
        const val existingRoutineActivityRequestCode = 2
    }

    private val channelId: String = "reminder_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        createNotificationChannel()

        mRoutines = ArrayList()


        //Set up recycler view
        val recyclerView = routinesList
        val adapter = RoutineListAdapter(mRoutines, this)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.hasFixedSize()

        //Get ViewModel from Provider
        mViewModel = ViewModelProviders.of(this).get(NewRoutineViewModel::class.java)

        //Get all routines in database
        mViewModel.allRoutines.observe(this, Observer { routines ->

            routines?.let {
                mRoutines = routines
                adapter.setRoutines(routines)
            }
            //Show empty view if there are no routines
            if (routines.isEmpty()) {
                emptyView.visibility = View.VISIBLE
            } else {
                emptyView.visibility = View.GONE
            }


        })

        //Start new routine activity when FAB is clicked
        newRoutine.setOnClickListener {
            val intent = Intent(this, NewRoutineActivity::class.java)
            startActivityForResult(
                intent,
                newRoutineActivityRequestCode
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val routine = data?.getParcelableExtra(NewRoutineActivity.EXTRA_ROUTINE) as Routine
            when (requestCode) {
                newRoutineActivityRequestCode -> mViewModel.addRoutine(routine)
                existingRoutineActivityRequestCode -> {
                    mViewModel.updateRoutine(routine)
                }
            }
        }
    }


    override fun onItemClick(routine: Routine, listenerType: RoutineListAdapter.ListenerType) {
        when (listenerType) {
            is RoutineListAdapter.ListenerType.RoutineClickListener -> {
                Log.d("R>>>", routine.toString())
            }
            is RoutineListAdapter.ListenerType.EditClickListener -> {
                val editIntent = Intent(this, NewRoutineActivity::class.java)
                editIntent.putExtra(NewRoutineActivity.EXTRA_ROUTINE, routine)
                startActivityForResult(
                    editIntent,
                    existingRoutineActivityRequestCode
                )
            }
        }
    }



    private fun checkTime(routine:Routine) {
        //If routine is being updated, populate the UI with routine information
       //Format routine start date

        val startDate: Date = routine.startTime
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = startDate
        val c = Calendar.getInstance()
        val currentTime = c.timeInMillis

        val timeDiff = currentTime - startDate.time

        Toast.makeText(this , "TimeDiff $timeDiff" , Toast.LENGTH_LONG).show()

    }
    }
