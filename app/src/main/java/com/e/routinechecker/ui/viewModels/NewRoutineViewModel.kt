package com.example.routinechecks.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.routinechecks.data.Repository
import com.example.routinechecks.data.database.Routine
import com.example.routinechecks.data.database.RoutineRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewRoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val allRoutines: LiveData<List<Routine>>

    init {
        val routineDao = RoutineRoomDatabase.getDatabase(application).routineDao()
        repository = Repository(routineDao)
        allRoutines = repository.allRoutines
    }

    fun addRoutine(routine: Routine) = viewModelScope.launch(Dispatchers.IO) {
        repository.addRoutine(routine)
    }

    fun updateRoutine(routine: Routine) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateRoutine(routine)
    }
}