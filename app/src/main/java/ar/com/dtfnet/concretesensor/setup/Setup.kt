package ar.com.dtfnet.concretesensor.setup

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Setup (
    @PrimaryKey(autoGenerate = true) var idx: Int,
    var sensor_id: String?,
    var mixer_id: String?,
    var user_id: String?
)
