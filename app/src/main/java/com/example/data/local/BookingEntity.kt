package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class BookingStatus {
    REQUESTED,
    CONFIRMED,
    ACTIVE,
    COMPLETED,
    CANCELLED
}

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val bookingId: Long = 0,
    val vehicleId: Long,
    val vehicleTitle: String,
    val vehicleCategory: String,
    val vehiclePhotoUrl: String,
    val customerName: String,
    val customerPhone: String,
    val startDate: String,
    val endDate: String,
    val totalDays: Int,
    val rentalAmount: Double,
    val securityDeposit: Double,
    val platformCommission: Double,
    val ownerShare: Double,
    val taxesFees: Double,
    val totalAmount: Double,
    val bookingStatus: BookingStatus,
    val paymentStatus: String = "PAID", // PAID, PENDING, REFUNDED
    val pickupOdometer: Double = 0.0,
    val returnOdometer: Double = 0.0,
    val pickupFuelLevelPercentage: Int = 100,
    val returnFuelLevelPercentage: Int = 100,
    val pickupConfirmedByCustomer: Boolean = false,
    val pickupConfirmedByOwner: Boolean = false,
    val returnConfirmedByCustomer: Boolean = false,
    val returnConfirmedByOwner: Boolean = false,
    val pickupTimestamp: Long = 0L,
    val returnTimestamp: Long = 0L,
    val agreementSigned: Boolean = true
)
