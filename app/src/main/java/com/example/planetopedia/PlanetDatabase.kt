package com.example.planetopedia

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Planet::class], version = 1, exportSchema = false)
abstract class PlanetDatabase : RoomDatabase() {
    abstract fun planetDao(): PlanetDao

    companion object {
        @Volatile
        private var INSTANCE: PlanetDatabase? = null

        fun getDatabase(context: Context): PlanetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlanetDatabase::class.java,
                    "planet_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
