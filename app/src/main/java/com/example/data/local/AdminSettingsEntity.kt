package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_settings")
data class AdminSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val ownerCommissionPercentage: Double = 70.0,
    val platformCommissionPercentage: Double = 30.0,
    val isAutoApproveKyc: Boolean = false,
    val helplineNumber: String = "+91 1800 890 4223"
)
