package ar.com.dtfnet.concretesensor.setup

import androidx.lifecycle.LiveData

class SetupRepository(private val setupDao: SetupDao) {
    val allSetups: LiveData<List<Setup>> = setupDao.getAll()

    suspend fun insert(setup: Setup) {
        setupDao.insert(setup)
    }

    suspend fun delete() {
        setupDao.delete()
    }
}