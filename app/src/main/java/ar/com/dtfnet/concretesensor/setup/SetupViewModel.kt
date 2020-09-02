package ar.com.dtfnet.concretesensor.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetupViewModel(application: Application) : AndroidViewModel(application) {
    val repository: SetupRepository

    val allSetups: LiveData<List<Setup>>

    init {
        val setupsDao = SetupDatabase.getDatabase(application).setupDao()
        repository = SetupRepository(setupsDao)
        allSetups = repository.allSetups
    }

    fun insert(setup: Setup) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(setup)
    }

    fun delete() = viewModelScope.launch(Dispatchers.IO) {
        repository.delete()
    }


}