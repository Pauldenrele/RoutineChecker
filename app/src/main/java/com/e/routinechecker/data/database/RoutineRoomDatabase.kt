package com.example.routinechecks.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Routine::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RoutineRoomDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Create a new table
                database.execSQL("CREATE TABLE routines_table_new (id INTEGER, title TEXT NOT NULL, " +
                        "description TEXT, frequency TEXT NOT NULL, missedRoutines INTEGER, completedRoutines INTEGER, " +
                        "startTime DATE NOT NULL, PRIMARY KEY(id))")
                //Copy the data
                database.execSQL("INSERT INTO routines_table_new (id, title, description, frequency, " +
                        "missedRoutines, completedRoutines, startTime) SELECT id, title, description, frequency, " +
                        "missedRoutines, completedRoutines, startTime FROM routines_table")
                //Remove the old table
                database.execSQL("DROP TABLE routines_table")
                //Change the table name
                database.execSQL("ALTER TABLE routines_table_new RENAME TO routines_table")
            }
        }

        @Volatile
        private var INSTANCE: RoutineRoomDatabase? = null

        fun getDatabase(context: Context): RoutineRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoutineRoomDatabase::class.java,
                    "ROUTINE_DATABASE"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}