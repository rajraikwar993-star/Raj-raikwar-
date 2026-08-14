package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.repository.GaadiRentRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class UserRole {
    CUSTOMER,
    VEHICLE_OWNER,
    ADMIN
}

data class VehicleFilterState(
    val selectedCity: String = "All Cities",
    val selectedCategory: String = "All",
    val searchQuery: String = "",
    val maxPricePerDay: Double = 10000.0
)

class GaadiRentViewModel(application: Application) : AndroidViewModel(application) {

    private val db = GaadiRentDatabase.getDatabase(application, viewModelScope)
    private val repository = GaadiRentRepository(db.dao())

    // Role state
    private val _currentRole = MutableStateFlow(UserRole.CUSTOMER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Filter State
    private val _filterState = MutableStateFlow(VehicleFilterState())
    val filterState: StateFlow<VehicleFilterState> = _filterState.asStateFlow()

    // Data Flows from Repository
    val verifiedVehicles: StateFlow<List<VehicleEntity>> = repository.verifiedVehicles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allVehicles: StateFlow<List<VehicleEntity>> = repository.allVehicles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAccidentCases: StateFlow<List<AccidentCaseEntity>> = repository.allAccidentCases
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminSettings: StateFlow<AdminSettingsEntity?> = repository.adminSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allInsurancePolicies: StateFlow<List<InsuranceEntity>> = repository.allInsurancePolicies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.allVehicles.firstOrNull().let { list ->
                if (list.isNullOrEmpty()) {
                    GaadiRentDatabase.populateDatabase(db.dao())
                }
            }
        }
    }

    // Filtered vehicles based on search query, city, and category
    val filteredVerifiedVehicles: StateFlow<List<VehicleEntity>> = combine(
        verifiedVehicles,
        _filterState
    ) { vehicles, filters ->
        vehicles.filter { v ->
            val matchCity = filters.selectedCity == "All Cities" || v.city.contains(filters.selectedCity, ignoreCase = true)
            val matchCategory = filters.selectedCategory == "All" || v.category.equals(filters.selectedCategory, ignoreCase = true)
            val matchQuery = filters.searchQuery.isBlank() ||
                    v.title.contains(filters.searchQuery, ignoreCase = true) ||
                    v.city.contains(filters.searchQuery, ignoreCase = true) ||
                    v.brand.contains(filters.searchQuery, ignoreCase = true)
            val matchPrice = v.pricePerDay <= filters.maxPricePerDay
            matchCity && matchCategory && matchQuery && matchPrice
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // User / Toast Message state
    private val _uiEventMessage = MutableStateFlow<String?>(null)
    val uiEventMessage: StateFlow<String?> = _uiEventMessage.asStateFlow()

    fun switchRole(role: UserRole) {
        _currentRole.value = role
        _uiEventMessage.value = "Switched to ${role.name.replace("_", " ")} Mode"
    }

    fun updateFilters(
        city: String? = null,
        category: String? = null,
        query: String? = null,
        maxPrice: Double? = null
    ) {
        _filterState.value = _filterState.value.copy(
            selectedCity = city ?: _filterState.value.selectedCity,
            selectedCategory = category ?: _filterState.value.selectedCategory,
            searchQuery = query ?: _filterState.value.searchQuery,
            maxPricePerDay = maxPrice ?: _filterState.value.maxPricePerDay
        )
    }

    fun clearUiMessage() {
        _uiEventMessage.value = null
    }

    fun getInsuranceForVehicle(vehicleId: Long): Flow<InsuranceEntity?> = repository.getInsuranceByVehicleId(vehicleId)
    fun getVehicleReviews(vehicleId: Long): Flow<List<ReviewEntity>> = repository.getVehicleReviews(vehicleId)

    // Booking actions
    fun createBooking(
        vehicle: VehicleEntity,
        customerName: String,
        customerPhone: String,
        startDate: String,
        endDate: String,
        totalDays: Int,
        onSuccess: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val ownerPct = adminSettings.value?.ownerCommissionPercentage ?: 70.0
            val platformPct = adminSettings.value?.platformCommissionPercentage ?: 30.0

            val bookingId = repository.createBooking(
                vehicle = vehicle,
                customerName = customerName,
                customerPhone = customerPhone,
                startDate = startDate,
                endDate = endDate,
                totalDays = totalDays,
                ownerSharePercent = ownerPct,
                platformSharePercent = platformPct
            )
            _uiEventMessage.value = "Booking #$bookingId Confirmed Successfully!"
            onSuccess(bookingId)
        }
    }

    fun reportAccident(
        bookingId: Long,
        vehicleId: Long,
        vehicleTitle: String,
        customerName: String,
        customerPhone: String,
        description: String,
        latitude: Double,
        longitude: Double,
        photoUri: String,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val caseNo = repository.reportAccident(
                bookingId = bookingId,
                vehicleId = vehicleId,
                vehicleTitle = vehicleTitle,
                customerName = customerName,
                customerPhone = customerPhone,
                description = description,
                latitude = latitude,
                longitude = longitude,
                photoUri = photoUri
            )
            _uiEventMessage.value = "EMERGENCY ALERT SENT! Case Number: $caseNo created."
            onSuccess(caseNo)
        }
    }

    fun recordPickupInspection(
        bookingId: Long,
        odometer: Double,
        fuelPercentage: Int,
        confirmedByCustomer: Boolean,
        confirmedByOwner: Boolean
    ) {
        viewModelScope.launch {
            repository.recordPickupInspection(bookingId, odometer, fuelPercentage, confirmedByCustomer, confirmedByOwner)
            _uiEventMessage.value = "Pickup Inspection Recorded Successfully"
        }
    }

    fun recordReturnInspection(
        bookingId: Long,
        odometer: Double,
        fuelPercentage: Int,
        confirmedByCustomer: Boolean,
        confirmedByOwner: Boolean
    ) {
        viewModelScope.launch {
            repository.recordReturnInspection(bookingId, odometer, fuelPercentage, confirmedByCustomer, confirmedByOwner)
            _uiEventMessage.value = "Return Inspection Completed & Vehicle Handed Back"
        }
    }

    fun cancelBooking(bookingId: Long) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, BookingStatus.CANCELLED)
            _uiEventMessage.value = "Booking #$bookingId Cancelled. Refund initiated."
        }
    }

    // Owner actions
    fun addNewVehicle(
        title: String,
        brand: String,
        modelName: String,
        category: String,
        city: String,
        ownerName: String,
        ownerPhone: String,
        pricePerDay: Double,
        securityDeposit: Double,
        registrationNumber: String,
        rcExpiryDate: String,
        permitType: String,
        permitExpiryDate: String,
        fitnessCertNumber: String,
        fitnessExpiryDate: String,
        photoUrl: String,
        transmission: String,
        fuelType: String,
        seatingCapacity: Int,
        insuranceCompany: String,
        policyNumber: String,
        insuranceStartDate: String,
        insuranceExpiryDate: String,
        isSelfDriveApplicable: Boolean,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            // Self-drive insurance compliance check
            val initialStatus = if (isSelfDriveApplicable) VehicleVerificationStatus.UNDER_REVIEW else VehicleVerificationStatus.REJECTED

            val vehicle = VehicleEntity(
                title = title,
                brand = brand,
                modelName = modelName,
                category = category,
                city = city,
                ownerName = ownerName,
                ownerPhone = ownerPhone,
                pricePerDay = pricePerDay,
                securityDeposit = securityDeposit,
                status = initialStatus,
                registrationNumber = registrationNumber,
                rcExpiryDate = rcExpiryDate,
                permitType = permitType,
                permitExpiryDate = permitExpiryDate,
                fitnessCertificateNumber = fitnessCertNumber,
                fitnessExpiryDate = fitnessExpiryDate,
                photoUrl = if (photoUrl.isNotBlank()) photoUrl else "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80",
                transmission = transmission,
                fuelType = fuelType,
                seatingCapacity = seatingCapacity,
                isVerifiedOwner = true,
                isVerifiedVehicle = false,
                isInsuranceVerified = isSelfDriveApplicable
            )

            val insurance = InsuranceEntity(
                vehicleId = 0,
                insuranceCompany = insuranceCompany,
                policyNumber = policyNumber,
                startDate = insuranceStartDate,
                expiryDate = insuranceExpiryDate,
                coverageType = if (isSelfDriveApplicable) "Commercial Self-Drive Cover" else "Private Vehicle Cover (Non-Eligible)",
                isSelfDriveApplicable = isSelfDriveApplicable,
                verificationStatus = if (isSelfDriveApplicable) InsuranceVerificationStatus.PENDING else InsuranceVerificationStatus.REJECTED
            )

            repository.addNewVehicleWithCompliance(vehicle, insurance)
            if (isSelfDriveApplicable) {
                _uiEventMessage.value = "Vehicle submitted for Admin Compliance Review (RC & Insurance)"
            } else {
                _uiEventMessage.value = "REJECTED: Standard private motor insurance is NOT eligible for rental. Commercial self-drive policy required."
            }
            onSuccess()
        }
    }

    // Admin actions
    fun verifyVehicle(vehicleId: Long, approve: Boolean) {
        viewModelScope.launch {
            val status = if (approve) VehicleVerificationStatus.VERIFIED else VehicleVerificationStatus.REJECTED
            repository.updateVehicleVerificationStatus(vehicleId, status)
            _uiEventMessage.value = if (approve) "Vehicle Approved & Live on Marketplace!" else "Vehicle Rejected for Compliance Issues"
        }
    }

    fun updateCommissionSettings(ownerPct: Double, platformPct: Double) {
        viewModelScope.launch {
            repository.updateAdminCommissionSettings(ownerPct, platformPct)
            _uiEventMessage.value = "Commission Settings Updated: ${ownerPct.toInt()}% Owner / ${platformPct.toInt()}% Platform"
        }
    }
}
