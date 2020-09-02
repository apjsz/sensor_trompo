package ar.com.dtfnet.concretesensor.setup

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SetupDao {
    @Query("SELECT * FROM setup")
    fun getAll(): LiveData<List<Setup>>

    @Insert
    fun insert(vararg  setups: Setup)

    @Query("DELETE FROM setup")
    fun delete()
}