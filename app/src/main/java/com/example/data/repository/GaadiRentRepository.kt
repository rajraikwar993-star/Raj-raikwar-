package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class GaadiRentRepository(private val dao: GaadiRentDao) {

    val verifiedVehicles: Flow<List<VehicleEntity>> = dao.getVerifiedVehicles()
    val allVehicles: Flow<List<VehicleEntity>> = dao.getAllVehicles()
    val allBookings: Flow<List<BookingEntity>> = dao.getAllBookings()
    val allAccidentCases: Flow<List<AccidentCaseEntity>> = dao.getAllAccidentCases()
    val adminSettings: Flow<AdminSettingsEntity?> = dao.getAdminSettings()
    val allInsurancePolicies: Flow<List<InsuranceEntity>> = dao.getAllInsurancePolicies()

    fun getVehicleById(vehicleId: Long): Flow<VehicleEntity?> = dao.getVehicleById(vehicleId)
    fun getInsuranceByVehicleId(vehicleId: Long): Flow<InsuranceEntity?> = dao.getInsuranceByVehicleId(vehicleId)
    fun getCustomerBookings(phone: String): Flow<List<BookingEntity>> = dao.getCustomerBookings(phone)
    fun getOwnerVehicles(phone: String): Flow<List<VehicleEntity>> = dao.getOwnerVehicles(phone)
    fun getVehicleReviews(vehicleId: Long): Flow<List<ReviewEntity>> = dao.getVehicleReviews(vehicleId)
    fun getBookingById(bookingId: Long): Flow<BookingEntity?> = dao.getBookingById(bookingId)

    suspend fun createBooking(
        vehicle: VehicleEntity,
        customerName: String,
        customerPhone: String,
        startDate: String,
        endDate: String,
        totalDays: Int,
        ownerSharePercent: Double = 70.0,
        platformSharePercent: Double = 30.0
    ): Long {
        val rentalAmount = vehicle.pricePerDay * totalDays
        val securityDeposit = vehicle.securityDeposit
        val ownerShare = rentalAmount * (ownerSharePercent / 100.0)
        val platformCommission = rentalAmount * (platformSharePercent / 100.0)
        val taxesFees = rentalAmount * 0.18 // 18% GST
        val totalAmount = rentalAmount + securityDeposit + taxesFees

        val booking = BookingEntity(
            vehicleId = vehicle.id,
            vehicleTitle = vehicle.title,
            vehicleCategory = vehicle.category,
            vehiclePhotoUrl = vehicle.photoUrl,
            customerName = customerName,
            customerPhone = customerPhone,
            startDate = startDate,
            endDate = endDate,
            totalDays = totalDays,
            rentalAmount = rentalAmount,
            securityDeposit = securityDeposit,
            platformCommission = platformCommission,
            ownerShare = ownerShare,
            taxesFees = taxesFees,
            totalAmount = totalAmount,
            bookingStatus = BookingStatus.ACTIVE,
            paymentStatus = "PAID via Demo Razorpay UPI",
            agreementSigned = true
        )
        return dao.insertBooking(booking)
    }

    suspend fun reportAccident(
        bookingId: Long,
        vehicleId: Long,
        vehicleTitle: String,
        customerName: String,
        customerPhone: String,
        description: String,
        latitude: Double = 28.6139,
        longitude: Double = 77.2090,
        photoUri: String = ""
    ): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault())
        val dateDisplay = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        val randomDigits = Random.nextInt(1000, 9999)
        val caseNumber = "ACC-${dateFormat.format(Date())}-$randomDigits"

        val accidentCase = AccidentCaseEntity(
            caseNumber = caseNumber,
            bookingId = bookingId,
            vehicleId = vehicleId,
            vehicleTitle = vehicleTitle,
            customerName = customerName,
            customerPhone = customerPhone,
            dateTimeStamp = dateDisplay,
            locationAddress = "GPS Coordinates: $latitude, $longitude (Delhi NCR Sector 62)",
            latitude = latitude,
            longitude = longitude,
            status = AccidentStatus.OPEN,
            description = description,
            photoUri = photoUri,
            emergencyNotified = true,
            ownerNotified = true,
            insuranceClaimRef = "CLM-$caseNumber"
        )
        dao.insertAccidentCase(accidentCase)
        return caseNumber
    }

    suspend fun recordPickupInspection(
        bookingId: Long,
        odometer: Double,
        fuelPercentage: Int,
        confirmedByCustomer: Boolean,
        confirmedByOwner: Boolean
    ) {
        val booking = dao.getBookingById(bookingId).firstOrNull()
        booking?.let {
            val updated = it.copy(
                pickupOdometer = odometer,
                pickupFuelLevelPercentage = fuelPercentage,
                pickupConfirmedByCustomer = confirmedByCustomer,
                pickupConfirmedByOwner = confirmedByOwner,
                pickupTimestamp = System.currentTimeMillis()
            )
            dao.updateBooking(updated)
        }
    }

    suspend fun recordReturnInspection(
        bookingId: Long,
        odometer: Double,
        fuelPercentage: Int,
        confirmedByCustomer: Boolean,
        confirmedByOwner: Boolean
    ) {
        val booking = dao.getBookingById(bookingId).firstOrNull()
        booking?.let {
            val updated = it.copy(
                returnOdometer = odometer,
                returnFuelLevelPercentage = fuelPercentage,
                returnConfirmedByCustomer = confirmedByCustomer,
                returnConfirmedByOwner = confirmedByOwner,
                returnTimestamp = System.currentTimeMillis(),
                bookingStatus = BookingStatus.COMPLETED
            )
            dao.updateBooking(updated)
        }
    }

    suspend fun addNewVehicleWithCompliance(
        vehicle: VehicleEntity,
        insurance: InsuranceEntity
    ): Long {
        val vehicleId = dao.insertVehicle(vehicle)
        val insuranceWithId = insurance.copy(vehicleId = vehicleId)
        dao.insertInsurance(insuranceWithId)
        return vehicleId
    }

    suspend fun updateVehicleVerificationStatus(vehicleId: Long, status: VehicleVerificationStatus) {
        dao.updateVehicleStatus(vehicleId, status)
    }

    suspend fun updateBookingStatus(bookingId: Long, status: BookingStatus) {
        dao.updateBookingStatus(bookingId, status)
    }

    suspend fun updateAdminCommissionSettings(ownerPct: Double, platformPct: Double) {
        dao.insertOrUpdateAdminSettings(
            AdminSettingsEntity(
                id = 1,
                ownerCommissionPercentage = ownerPct,
                platformCommissionPercentage = platformPct
            )
        )
    }

    suspend fun addReview(review: ReviewEntity) {
        dao.insertReview(review)
    }
}
