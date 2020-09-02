package ar.com.dtfnet.concretesensor.setup

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Setup::class], version = 1, exportSchema = false)
abstract class SetupDatabase : RoomDatabase() {
    abstract fun setupDao() : SetupDao

    companion object {
        @Volatile
        private var INSTANCE: SetupDatabase? = null

        fun getDatabase(context: Context) : SetupDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SetupDatabase::class.java,
                    "setup_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}