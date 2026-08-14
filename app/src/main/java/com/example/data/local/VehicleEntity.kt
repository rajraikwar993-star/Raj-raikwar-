package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class VehicleVerificationStatus {
    PENDING,
    UNDER_REVIEW,
    VERIFIED,
    REJECTED,
    SUSPENDED,
    EXPIRED
}

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val brand: String,
    val modelName: String,
    val category: String, // Hatchback, Sedan, SUV, EV, Bike/Scooter, Luxury
    val city: String,
    val ownerName: String,
    val ownerPhone: String,
    val pricePerDay: Double,
    val securityDeposit: Double,
    val status: VehicleVerificationStatus,
    val registrationNumber: String, // e.g. KA01AB1234
    val rcExpiryDate: String,
    val permitType: String, // e.g. Commercial Self-Drive Permit (All-India)
    val permitExpiryDate: String,
    val fitnessCertificateNumber: String,
    val fitnessExpiryDate: String,
    val photoUrl: String,
    val transmission: String, // Manual, Automatic
    val fuelType: String, // Petrol, Diesel, Electric, Hybrid
    val seatingCapacity: Int,
    val isVerifiedOwner: Boolean = true,
    val isVerifiedVehicle: Boolean = true,
    val isInsuranceVerified: Boolean = true,
    val rating: Double = 4.8,
    val reviewCount: Int = 12
)
