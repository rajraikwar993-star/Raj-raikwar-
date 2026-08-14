package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Database(
    entities = [
        VehicleEntity::class,
        InsuranceEntity::class,
        BookingEntity::class,
        AccidentCaseEntity::class,
        AdminSettingsEntity::class,
        ReviewEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GaadiRentDatabase : RoomDatabase() {

    abstract fun dao(): GaadiRentDao

    companion object {
        @Volatile
        private var INSTANCE: GaadiRentDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): GaadiRentDatabase {
            return INSTANCE ?: synchronized(this) {
                var createdInstance: GaadiRentDatabase? = null
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GaadiRentDatabase::class.java,
                    "gaadirent_database.db"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        scope.launch(Dispatchers.IO) {
                            val targetDb = createdInstance ?: INSTANCE
                            targetDb?.dao()?.let { dao ->
                                populateDatabase(dao)
                            }
                        }
                    }
                })
                .build()
                createdInstance = instance
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateDatabase(dao: GaadiRentDao) {
            val existing = dao.getAllVehicles().firstOrNull()
            if (!existing.isNullOrEmpty()) return

            // Admin Settings
                dao.insertOrUpdateAdminSettings(
                    AdminSettingsEntity(
                        id = 1,
                        ownerCommissionPercentage = 70.0,
                        platformCommissionPercentage = 30.0,
                        isAutoApproveKyc = false,
                        helplineNumber = "+91 1800 890 4223"
                    )
                )

                // Seed Demo Vehicles covering all categories
                val v1Id = dao.insertVehicle(
                    VehicleEntity(
                        id = 1,
                        title = "Mahindra Thar 4x4 Convertible LX",
                        brand = "Mahindra",
                        modelName = "Thar LX",
                        category = "SUV",
                        city = "Delhi NCR",
                        ownerName = "Rajesh Sharma (Verified Owner)",
                        ownerPhone = "+91 98765 43210",
                        pricePerDay = 3500.0,
                        securityDeposit = 5000.0,
                        status = VehicleVerificationStatus.VERIFIED,
                        registrationNumber = "DL 01 AX 9988",
                        rcExpiryDate = "2032-10-15",
                        permitType = "All India Self-Drive Commercial Permit",
                        permitExpiryDate = "2028-05-20",
                        fitnessCertificateNumber = "FIT-DL-2024-8812",
                        fitnessExpiryDate = "2027-11-30",
                        photoUrl = "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80",
                        transmission = "Automatic",
                        fuelType = "Diesel",
                        seatingCapacity = 4,
                        isVerifiedOwner = true,
                        isVerifiedVehicle = true,
                        isInsuranceVerified = true,
                        rating = 4.9,
                        reviewCount = 28
                    )
                )

                dao.insertInsurance(
                    InsuranceEntity(
                        vehicleId = v1Id,
                        insuranceCompany = "ICICI Lombard Motor Insurance",
                        policyNumber = "POL-ICICI-9988221",
                        startDate = "2025-01-10",
                        expiryDate = "2027-01-09",
                        coverageType = "Commercial Self-Drive Comprehensive Zero-Dep",
                        isSelfDriveApplicable = true,
                        verificationStatus = InsuranceVerificationStatus.VERIFIED
                    )
                )

                val v2Id = dao.insertVehicle(
                    VehicleEntity(
                        id = 2,
                        title = "Tata Nexon EV Max Tech Lux",
                        brand = "Tata",
                        modelName = "Nexon EV",
                        category = "EV",
                        city = "Bengaluru",
                        ownerName = "Ananya Murthy (Verified Owner)",
                        ownerPhone = "+91 98112 23344",
                        pricePerDay = 2400.0,
                        securityDeposit = 3000.0,
                        status = VehicleVerificationStatus.VERIFIED,
                        registrationNumber = "KA 05 M 4521",
                        rcExpiryDate = "2035-02-12",
                        permitType = "State Self-Drive Green Commercial Permit",
                        permitExpiryDate = "2029-08-14",
                        fitnessCertificateNumber = "FIT-KA-2025-0012",
                        fitnessExpiryDate = "2028-02-12",
                        photoUrl = "https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=800&q=80",
                        transmission = "Automatic",
                        fuelType = "Electric",
                        seatingCapacity = 5,
                        isVerifiedOwner = true,
                        isVerifiedVehicle = true,
                        isInsuranceVerified = true,
                        rating = 4.8,
                        reviewCount = 19
                    )
                )

                dao.insertInsurance(
                    InsuranceEntity(
                        vehicleId = v2Id,
                        insuranceCompany = "HDFC ERGO General Insurance",
                        policyNumber = "POL-HDFC-EV-4521",
                        startDate = "2025-03-01",
                        expiryDate = "2027-02-28",
                        coverageType = "Self-Drive Rental Commercial EV Shield",
                        isSelfDriveApplicable = true,
                        verificationStatus = InsuranceVerificationStatus.VERIFIED
                    )
                )

                val v3Id = dao.insertVehicle(
                    VehicleEntity(
                        id = 3,
                        title = "Maruti Suzuki Swift ZXi+",
                        brand = "Maruti",
                        modelName = "Swift ZXi",
                        category = "Hatchback",
                        city = "Delhi NCR",
                        ownerName = "Vikram Malhotra",
                        ownerPhone = "+91 98100 11223",
                        pricePerDay = 1500.0,
                        securityDeposit = 2000.0,
                        status = VehicleVerificationStatus.VERIFIED,
                        registrationNumber = "DL 03 CA 1234",
                        rcExpiryDate = "2033-04-10",
                        permitType = "NCR Commercial Self-Drive Permit",
                        permitExpiryDate = "2028-04-10",
                        fitnessCertificateNumber = "FIT-DL-2023-1234",
                        fitnessExpiryDate = "2027-04-10",
                        photoUrl = "https://images.unsplash.com/photo-1590362891991-f776e747a588?auto=format&fit=crop&w=800&q=80",
                        transmission = "Manual",
                        fuelType = "Petrol",
                        seatingCapacity = 5,
                        isVerifiedOwner = true,
                        isVerifiedVehicle = true,
                        isInsuranceVerified = true,
                        rating = 4.7,
                        reviewCount = 35
                    )
                )

                dao.insertInsurance(
                    InsuranceEntity(
                        vehicleId = v3Id,
                        insuranceCompany = "New India Assurance",
                        policyNumber = "POL-NIA-33211",
                        startDate = "2025-01-01",
                        expiryDate = "2027-01-01",
                        coverageType = "Commercial Self-Drive Comprehensive",
                        isSelfDriveApplicable = true,
                        verificationStatus = InsuranceVerificationStatus.VERIFIED
                    )
                )

                val v4Id = dao.insertVehicle(
                    VehicleEntity(
                        id = 4,
                        title = "Honda City ZX i-VTEC",
                        brand = "Honda",
                        modelName = "City ZX",
                        category = "Sedan",
                        city = "Mumbai",
                        ownerName = "Suresh Nambiar",
                        ownerPhone = "+91 98450 66778",
                        pricePerDay = 2800.0,
                        securityDeposit = 4000.0,
                        status = VehicleVerificationStatus.VERIFIED,
                        registrationNumber = "MH 04 ET 8822",
                        rcExpiryDate = "2031-08-15",
                        permitType = "Maharashtra Commercial Self-Drive Permit",
                        permitExpiryDate = "2028-08-15",
                        fitnessCertificateNumber = "FIT-MH-2024-8822",
                        fitnessExpiryDate = "2027-08-15",
                        photoUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=800&q=80",
                        transmission = "CVT Automatic",
                        fuelType = "Petrol",
                        seatingCapacity = 5,
                        isVerifiedOwner = true,
                        isVerifiedVehicle = true,
                        isInsuranceVerified = true,
                        rating = 4.85,
                        reviewCount = 22
                    )
                )

                dao.insertInsurance(
                    InsuranceEntity(
                        vehicleId = v4Id,
                        insuranceCompany = "Bajaj Allianz",
                        policyNumber = "POL-BAJAJ-8822",
                        startDate = "2025-02-15",
                        expiryDate = "2027-02-14",
                        coverageType = "Commercial Self-Drive Package",
                        isSelfDriveApplicable = true,
                        verificationStatus = InsuranceVerificationStatus.VERIFIED
                    )
                )

                val v5Id = dao.insertVehicle(
                    VehicleEntity(
                        id = 5,
                        title = "Royal Enfield Classic 350 Chrome Red",
                        brand = "Royal Enfield",
                        modelName = "Classic 350",
                        category = "Bike",
                        city = "Goa",
                        ownerName = "Rohan D'Souza (Verified Owner)",
                        ownerPhone = "+91 98221 55443",
                        pricePerDay = 1200.0,
                        securityDeposit = 2000.0,
                        status = VehicleVerificationStatus.VERIFIED,
                        registrationNumber = "GA 03 B 3500",
                        rcExpiryDate = "2034-01-20",
                        permitType = "Goa Tourist Two-Wheeler Self-Drive Licence",
                        permitExpiryDate = "2029-01-10",
                        fitnessCertificateNumber = "FIT-GA-2024-3500",
                        fitnessExpiryDate = "2028-01-20",
                        photoUrl = "https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=800&q=80",
                        transmission = "Manual",
                        fuelType = "Petrol",
                        seatingCapacity = 2,
                        isVerifiedOwner = true,
                        isVerifiedVehicle = true,
                        isInsuranceVerified = true,
                        rating = 4.95,
                        reviewCount = 42
                    )
                )

                dao.insertInsurance(
                    InsuranceEntity(
                        vehicleId = v5Id,
                        insuranceCompany = "Tata AIG General Insurance",
                        policyNumber = "POL-TATA-GA3500",
                        startDate = "2025-02-01",
                        expiryDate = "2027-01-31",
                        coverageType = "Two-Wheeler Commercial Self-Drive Package",
                        isSelfDriveApplicable = true,
                        verificationStatus = InsuranceVerificationStatus.VERIFIED
                    )
                )

                val v6Id = dao.insertVehicle(
                    VehicleEntity(
                        id = 6,
                        title = "TVS Ntorq 125 Race Edition",
                        brand = "TVS",
                        modelName = "Ntorq 125",
                        category = "Scooter",
                        city = "Bengaluru",
                        ownerName = "Pradeep Kumar",
                        ownerPhone = "+91 97312 88990",
                        pricePerDay = 700.0,
                        securityDeposit = 1000.0,
                        status = VehicleVerificationStatus.VERIFIED,
                        registrationNumber = "KA 01 J 9090",
                        rcExpiryDate = "2035-06-12",
                        permitType = "Karnataka Yellow Plate Self-Drive Permit",
                        permitExpiryDate = "2029-06-12",
                        fitnessCertificateNumber = "FIT-KA-2025-9090",
                        fitnessExpiryDate = "2028-06-12",
                        photoUrl = "https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&w=800&q=80",
                        transmission = "Automatic (CVT)",
                        fuelType = "Petrol",
                        seatingCapacity = 2,
                        isVerifiedOwner = true,
                        isVerifiedVehicle = true,
                        isInsuranceVerified = true,
                        rating = 4.6,
                        reviewCount = 18
                    )
                )

                dao.insertInsurance(
                    InsuranceEntity(
                        vehicleId = v6Id,
                        insuranceCompany = "Oriental Insurance",
                        policyNumber = "POL-ORIENTAL-9090",
                        startDate = "2025-05-01",
                        expiryDate = "2027-04-30",
                        coverageType = "Commercial Two-Wheeler Self-Drive",
                        isSelfDriveApplicable = true,
                        verificationStatus = InsuranceVerificationStatus.VERIFIED
                    )
                )

                val v7Id = dao.insertVehicle(
                    VehicleEntity(
                        id = 7,
                        title = "BMW 5 Series 530i M Sport",
                        brand = "BMW",
                        modelName = "5 Series",
                        category = "Luxury",
                        city = "Delhi NCR",
                        ownerName = "Karan Oberoi (Chauffeur & Self-Drive Fleet)",
                        ownerPhone = "+91 99100 88776",
                        pricePerDay = 8500.0,
                        securityDeposit = 15000.0,
                        status = VehicleVerificationStatus.VERIFIED,
                        registrationNumber = "DL 01 C 0007",
                        rcExpiryDate = "2032-11-01",
                        permitType = "All India Luxury Commercial Self-Drive Permit",
                        permitExpiryDate = "2028-11-01",
                        fitnessCertificateNumber = "FIT-DL-2024-0007",
                        fitnessExpiryDate = "2027-11-01",
                        photoUrl = "https://images.unsplash.com/photo-1555215695-3004980ad54e?auto=format&fit=crop&w=800&q=80",
                        transmission = "Automatic",
                        fuelType = "Petrol",
                        seatingCapacity = 5,
                        isVerifiedOwner = true,
                        isVerifiedVehicle = true,
                        isInsuranceVerified = true,
                        rating = 4.98,
                        reviewCount = 15
                    )
                )

                dao.insertInsurance(
                    InsuranceEntity(
                        vehicleId = v7Id,
                        insuranceCompany = "HDFC ERGO Luxury Motor Shield",
                        policyNumber = "POL-HDFC-BMW530",
                        startDate = "2025-01-15",
                        expiryDate = "2027-01-14",
                        coverageType = "Comprehensive Luxury Commercial Zero-Dep",
                        isSelfDriveApplicable = true,
                        verificationStatus = InsuranceVerificationStatus.VERIFIED
                    )
                )

                // Seed an Active Booking for testing the Red Emergency Button and Inspection!
                val activeBookingId = dao.insertBooking(
                    BookingEntity(
                        bookingId = 1001,
                        vehicleId = v1Id,
                        vehicleTitle = "Mahindra Thar 4x4 Convertible LX",
                        vehicleCategory = "SUV",
                        vehiclePhotoUrl = "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80",
                        customerName = "Aarav Gupta",
                        customerPhone = "+91 99999 88888",
                        startDate = "2026-08-13",
                        endDate = "2026-08-16",
                        totalDays = 3,
                        rentalAmount = 10500.0,
                        securityDeposit = 5000.0,
                        platformCommission = 3150.0, // 30% of 10500
                        ownerShare = 7350.0, // 70% of 10500
                        taxesFees = 1890.0, // 18% GST
                        totalAmount = 17390.0,
                        bookingStatus = BookingStatus.ACTIVE,
                        paymentStatus = "PAID via Razorpay UPI",
                        pickupOdometer = 14250.0,
                        pickupFuelLevelPercentage = 95,
                        pickupConfirmedByCustomer = true,
                        pickupConfirmedByOwner = true,
                        pickupTimestamp = System.currentTimeMillis() - 86400000L
                    )
                )

                // Seed Reviews
                dao.insertReview(
                    ReviewEntity(
                        vehicleId = v1Id,
                        customerName = "Vikram Aditya",
                        rating = 5,
                        comment = "Vehicle was in super condition! All documents were clear, permit verified. Excellent experience in Delhi NCR.",
                        date = "2026-08-01"
                    )
                )
                dao.insertReview(
                    ReviewEntity(
                        vehicleId = v2Id,
                        customerName = "Priya Sharma",
                        rating = 5,
                        comment = "Very smooth EV drive in Bengaluru. Fast charger included and insurance verified badge gave total peace of mind.",
                        date = "2026-08-05"
                    )
                )
            }
        }
    }
