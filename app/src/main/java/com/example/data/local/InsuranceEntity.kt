package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class InsuranceVerificationStatus {
    PENDING,
    VERIFIED,
    EXPIRED,
    REJECTED
}

@Entity(tableName = "insurance_policies")
data class InsuranceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val vehicleId: Long,
    val insuranceCompany: String,
    val policyNumber: String,
    val startDate: String,
    val expiryDate: String,
    val coverageType: String, // Commercial Self-Drive Comprehensive / Third-Party
    val isSelfDriveApplicable: Boolean, // Crucial compliance check: must be true for rental
    val verificationStatus: InsuranceVerificationStatus,
    val policyDocumentUrl: String = ""
)
