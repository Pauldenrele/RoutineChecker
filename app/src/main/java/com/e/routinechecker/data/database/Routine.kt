package com.example.routinechecks.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "routines_table")
data class Routine(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var description: String?,
    var frequency: String,
    var missedRoutines: Int = 0,
    var completedRoutines: Int = 0,
    var startTime: Date
) : Parcelable