package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AccidentStatus {
    OPEN,
    INVESTIGATING,
    CLAIM_FILED,
    RESOLVED
}

@Entity(tableName = "accident_cases")
data class AccidentCaseEntity(
    @PrimaryKey(autoGenerate = true) val caseId: Long = 0,
    val caseNumber: String, // e.g. ACC-2026-9812
    val bookingId: Long,
    val vehicleId: Long,
    val vehicleTitle: String,
    val customerName: String,
    val customerPhone: String,
    val dateTimeStamp: String,
    val locationAddress: String,
    val latitude: Double,
    val longitude: Double,
    val status: AccidentStatus,
    val description: String,
    val photoUri: String = "",
    val emergencyNotified: Boolean = true,
    val ownerNotified: Boolean = true,
    val insuranceClaimRef: String = ""
)
