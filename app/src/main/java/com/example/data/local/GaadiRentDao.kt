package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GaadiRentDao {

    // Vehicles
    @Query("SELECT * FROM vehicles WHERE status = 'VERIFIED'")
    fun getVerifiedVehicles(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles")
    fun getAllVehicles(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE id = :vehicleId")
    fun getVehicleById(vehicleId: Long): Flow<VehicleEntity?>

    @Query("SELECT * FROM vehicles WHERE ownerPhone = :ownerPhone")
    fun getOwnerVehicles(ownerPhone: String): Flow<List<VehicleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity): Long

    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity)

    @Query("UPDATE vehicles SET status = :status WHERE id = :vehicleId")
    suspend fun updateVehicleStatus(vehicleId: Long, status: VehicleVerificationStatus)

    // Insurance
    @Query("SELECT * FROM insurance_policies WHERE vehicleId = :vehicleId LIMIT 1")
    fun getInsuranceByVehicleId(vehicleId: Long): Flow<InsuranceEntity?>

    @Query("SELECT * FROM insurance_policies")
    fun getAllInsurancePolicies(): Flow<List<InsuranceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsurance(insurance: InsuranceEntity): Long

    @Update
    suspend fun updateInsurance(insurance: InsuranceEntity)

    // Bookings
    @Query("SELECT * FROM bookings ORDER BY bookingId DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE customerPhone = :customerPhone ORDER BY bookingId DESC")
    fun getCustomerBookings(customerPhone: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE bookingId = :bookingId")
    fun getBookingById(bookingId: Long): Flow<BookingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET bookingStatus = :status WHERE bookingId = :bookingId")
    suspend fun updateBookingStatus(bookingId: Long, status: BookingStatus)

    // Accident Cases
    @Query("SELECT * FROM accident_cases ORDER BY caseId DESC")
    fun getAllAccidentCases(): Flow<List<AccidentCaseEntity>>

    @Query("SELECT * FROM accident_cases WHERE bookingId = :bookingId")
    fun getAccidentCasesForBooking(bookingId: Long): Flow<List<AccidentCaseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccidentCase(caseEntity: AccidentCaseEntity): Long

    @Update
    suspend fun updateAccidentCase(caseEntity: AccidentCaseEntity)

    // Admin Settings
    @Query("SELECT * FROM admin_settings WHERE id = 1 LIMIT 1")
    fun getAdminSettings(): Flow<AdminSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAdminSettings(settings: AdminSettingsEntity)

    // Reviews
    @Query("SELECT * FROM reviews WHERE vehicleId = :vehicleId ORDER BY id DESC")
    fun getVehicleReviews(vehicleId: Long): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long
}
