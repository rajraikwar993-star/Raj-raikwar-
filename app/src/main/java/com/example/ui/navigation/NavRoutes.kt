package com.example.ui.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Search : NavRoutes("search")
    object VehicleDetails : NavRoutes("vehicle_details/{vehicleId}") {
        fun createRoute(vehicleId: Long) = "vehicle_details/$vehicleId"
    }
    object Booking : NavRoutes("booking/{vehicleId}") {
        fun createRoute(vehicleId: Long) = "booking/$vehicleId"
    }
    object ActiveBooking : NavRoutes("active_booking/{bookingId}") {
        fun createRoute(bookingId: Long) = "active_booking/$bookingId"
    }
    object AccidentReport : NavRoutes("accident_report/{bookingId}") {
        fun createRoute(bookingId: Long) = "accident_report/$bookingId"
    }
    object CustomerDashboard : NavRoutes("customer_dashboard")
    object OwnerDashboard : NavRoutes("owner_dashboard")
    object AddVehicle : NavRoutes("add_vehicle")
    object AdminDashboard : NavRoutes("admin_dashboard")
    object SafetyInsurance : NavRoutes("safety_insurance")
    object Support : NavRoutes("support")
}
