package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.components.RoleSwitcherHeader
import com.example.ui.navigation.NavRoutes
import com.example.ui.screens.*
import com.example.ui.theme.DarkBluePrimary
import com.example.ui.theme.GaadiRentTheme
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeVibrant
import com.example.ui.viewmodel.GaadiRentViewModel
import com.example.ui.viewmodel.UserRole

class MainActivity : ComponentActivity() {

    private val viewModel: GaadiRentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GaadiRentTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
                val filterState by viewModel.filterState.collectAsStateWithLifecycle()
                val verifiedVehicles by viewModel.filteredVerifiedVehicles.collectAsStateWithLifecycle()
                val allVehicles by viewModel.allVehicles.collectAsStateWithLifecycle()
                val allBookings by viewModel.allBookings.collectAsStateWithLifecycle()
                val accidentCases by viewModel.allAccidentCases.collectAsStateWithLifecycle()
                val adminSettings by viewModel.adminSettings.collectAsStateWithLifecycle()
                val uiMessage by viewModel.uiEventMessage.collectAsStateWithLifecycle()

                // Display snackbar alerts when ViewModel triggers a UI message
                LaunchedEffect(uiMessage) {
                    uiMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearUiMessage()
                    }
                }

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        RoleSwitcherHeader(
                            currentRole = currentRole,
                            onRoleSelected = { role ->
                                viewModel.switchRole(role)
                                when (role) {
                                    UserRole.CUSTOMER -> navController.navigate(NavRoutes.Home.route)
                                    UserRole.VEHICLE_OWNER -> navController.navigate(NavRoutes.OwnerDashboard.route)
                                    UserRole.ADMIN -> navController.navigate(NavRoutes.AdminDashboard.route)
                                }
                            },
                            onEmergencyClicked = {
                                val activeBooking = allBookings.firstOrNull()
                                if (activeBooking != null) {
                                    navController.navigate(NavRoutes.AccidentReport.createRoute(activeBooking.bookingId))
                                } else {
                                    navController.navigate(NavRoutes.Support.route)
                                }
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = DarkBluePrimary,
                            contentColor = Color.White
                        ) {
                            when (currentRole) {
                                UserRole.CUSTOMER -> {
                                    NavigationBarItem(
                                        selected = currentRoute == NavRoutes.Home.route,
                                        onClick = { navController.navigate(NavRoutes.Home.route) },
                                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                        label = { Text("Home", fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = OrangeVibrant,
                                            selectedTextColor = OrangeVibrant,
                                            unselectedIconColor = Color.White.copy(0.7f),
                                            unselectedTextColor = Color.White.copy(0.7f),
                                            indicatorColor = Color.White.copy(0.12f)
                                        ),
                                        modifier = Modifier.testTag("nav_home")
                                    )

                                    NavigationBarItem(
                                        selected = currentRoute == NavRoutes.Search.route,
                                        onClick = { navController.navigate(NavRoutes.Search.route) },
                                        icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                                        label = { Text("Search", fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = OrangeVibrant,
                                            selectedTextColor = OrangeVibrant,
                                            unselectedIconColor = Color.White.copy(0.7f),
                                            unselectedTextColor = Color.White.copy(0.7f),
                                            indicatorColor = Color.White.copy(0.12f)
                                        ),
                                        modifier = Modifier.testTag("nav_search")
                                    )

                                    NavigationBarItem(
                                        selected = currentRoute == NavRoutes.CustomerDashboard.route,
                                        onClick = { navController.navigate(NavRoutes.CustomerDashboard.route) },
                                        icon = { Icon(Icons.Default.DirectionsCar, contentDescription = "My Rentals") },
                                        label = { Text("My Rentals", fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = OrangeVibrant,
                                            selectedTextColor = OrangeVibrant,
                                            unselectedIconColor = Color.White.copy(0.7f),
                                            unselectedTextColor = Color.White.copy(0.7f),
                                            indicatorColor = Color.White.copy(0.12f)
                                        ),
                                        modifier = Modifier.testTag("nav_rentals")
                                    )

                                    NavigationBarItem(
                                        selected = currentRoute == NavRoutes.SafetyInsurance.route,
                                        onClick = { navController.navigate(NavRoutes.SafetyInsurance.route) },
                                        icon = { Icon(Icons.Default.Shield, contentDescription = "Safety") },
                                        label = { Text("Safety", fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = OrangeVibrant,
                                            selectedTextColor = OrangeVibrant,
                                            unselectedIconColor = Color.White.copy(0.7f),
                                            unselectedTextColor = Color.White.copy(0.7f),
                                            indicatorColor = Color.White.copy(0.12f)
                                        ),
                                        modifier = Modifier.testTag("nav_safety")
                                    )
                                }

                                UserRole.VEHICLE_OWNER -> {
                                    NavigationBarItem(
                                        selected = currentRoute == NavRoutes.OwnerDashboard.route,
                                        onClick = { navController.navigate(NavRoutes.OwnerDashboard.route) },
                                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Fleet Dashboard") },
                                        label = { Text("My Fleet", fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = OrangeVibrant,
                                            selectedTextColor = OrangeVibrant,
                                            unselectedIconColor = Color.White.copy(0.7f),
                                            unselectedTextColor = Color.White.copy(0.7f),
                                            indicatorColor = Color.White.copy(0.12f)
                                        ),
                                        modifier = Modifier.testTag("nav_owner_fleet")
                                    )

                                    NavigationBarItem(
                                        selected = currentRoute == NavRoutes.AddVehicle.route,
                                        onClick = { navController.navigate(NavRoutes.AddVehicle.route) },
                                        icon = { Icon(Icons.Default.AddBusiness, contentDescription = "Add Vehicle") },
                                        label = { Text("List Vehicle", fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = OrangeVibrant,
                                            selectedTextColor = OrangeVibrant,
                                            unselectedIconColor = Color.White.copy(0.7f),
                                            unselectedTextColor = Color.White.copy(0.7f),
                                            indicatorColor = Color.White.copy(0.12f)
                                        ),
                                        modifier = Modifier.testTag("nav_add_vehicle")
                                    )

                                    NavigationBarItem(
                                        selected = currentRoute == NavRoutes.Support.route,
                                        onClick = { navController.navigate(NavRoutes.Support.route) },
                                        icon = { Icon(Icons.Default.SupportAgent, contentDescription = "Support") },
                                        label = { Text("Support", fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = OrangeVibrant,
                                            selectedTextColor = OrangeVibrant,
                                            unselectedIconColor = Color.White.copy(0.7f),
                                            unselectedTextColor = Color.White.copy(0.7f),
                                            indicatorColor = Color.White.copy(0.12f)
                                        )
                                    )
                                }

                                UserRole.ADMIN -> {
                                    NavigationBarItem(
                                        selected = currentRoute == NavRoutes.AdminDashboard.route,
                                        onClick = { navController.navigate(NavRoutes.AdminDashboard.route) },
                                        icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                                        label = { Text("Admin Panel", fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = OrangeVibrant,
                                            selectedTextColor = OrangeVibrant,
                                            unselectedIconColor = Color.White.copy(0.7f),
                                            unselectedTextColor = Color.White.copy(0.7f),
                                            indicatorColor = Color.White.copy(0.12f)
                                        )
                                    )

                                    NavigationBarItem(
                                        selected = currentRoute == NavRoutes.SafetyInsurance.route,
                                        onClick = { navController.navigate(NavRoutes.SafetyInsurance.route) },
                                        icon = { Icon(Icons.Default.Gavel, contentDescription = "Compliance") },
                                        label = { Text("Legal Rules", fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = OrangeVibrant,
                                            selectedTextColor = OrangeVibrant,
                                            unselectedIconColor = Color.White.copy(0.7f),
                                            unselectedTextColor = Color.White.copy(0.7f),
                                            indicatorColor = Color.White.copy(0.12f)
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Home Screen
                        composable(NavRoutes.Home.route) {
                            HomeScreen(
                                featuredVehicles = verifiedVehicles,
                                onSearchClicked = { category ->
                                    if (category != null) {
                                        viewModel.updateFilters(category = category)
                                    }
                                    navController.navigate(NavRoutes.Search.route)
                                },
                                onListVehicleClicked = {
                                    viewModel.switchRole(UserRole.VEHICLE_OWNER)
                                    navController.navigate(NavRoutes.AddVehicle.route)
                                },
                                onVehicleClicked = { vehicleId ->
                                    navController.navigate(NavRoutes.VehicleDetails.createRoute(vehicleId))
                                },
                                onSafetyInfoClicked = {
                                    navController.navigate(NavRoutes.SafetyInsurance.route)
                                },
                                onSupportClicked = {
                                    navController.navigate(NavRoutes.Support.route)
                                }
                            )
                        }

                        // Search Screen
                        composable(NavRoutes.Search.route) {
                            SearchScreen(
                                vehicles = verifiedVehicles,
                                filterState = filterState,
                                onFilterChanged = { city, cat, query, maxPrice ->
                                    viewModel.updateFilters(city, cat, query, maxPrice)
                                },
                                onVehicleClicked = { vehicleId ->
                                    navController.navigate(NavRoutes.VehicleDetails.createRoute(vehicleId))
                                }
                            )
                        }

                        // Vehicle Details Screen
                        composable(
                            route = NavRoutes.VehicleDetails.route,
                            arguments = listOf(navArgument("vehicleId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val vehicleId = backStackEntry.arguments?.getLong("vehicleId") ?: 0L
                            val vehicle = allVehicles.find { it.id == vehicleId }
                            val insurance by viewModel.getInsuranceForVehicle(vehicleId).collectAsStateWithLifecycle(initialValue = null)
                            val reviews by viewModel.getVehicleReviews(vehicleId).collectAsStateWithLifecycle(initialValue = emptyList())

                            VehicleDetailsScreen(
                                vehicle = vehicle,
                                insurance = insurance,
                                reviews = reviews,
                                onBookClicked = { id ->
                                    navController.navigate(NavRoutes.Booking.createRoute(id))
                                },
                                onBackClicked = { navController.popBackStack() }
                            )
                        }

                        // Booking & Payment Screen
                        composable(
                            route = NavRoutes.Booking.route,
                            arguments = listOf(navArgument("vehicleId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val vehicleId = backStackEntry.arguments?.getLong("vehicleId") ?: 0L
                            val vehicle = allVehicles.find { it.id == vehicleId }

                            BookingPaymentScreen(
                                vehicle = vehicle,
                                onConfirmBooking = { name, phone, start, end, days ->
                                    vehicle?.let { v ->
                                        viewModel.createBooking(
                                            vehicle = v,
                                            customerName = name,
                                            customerPhone = phone,
                                            startDate = start,
                                            endDate = end,
                                            totalDays = days,
                                            onSuccess = { bookingId ->
                                                navController.navigate(NavRoutes.ActiveBooking.createRoute(bookingId)) {
                                                    popUpTo(NavRoutes.Home.route)
                                                }
                                            }
                                        )
                                    }
                                },
                                onBackClicked = { navController.popBackStack() }
                            )
                        }

                        // Active Booking Screen (Red Emergency Button & Inspection)
                        composable(
                            route = NavRoutes.ActiveBooking.route,
                            arguments = listOf(navArgument("bookingId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val bookingId = backStackEntry.arguments?.getLong("bookingId") ?: 0L
                            val booking = allBookings.find { it.bookingId == bookingId }

                            ActiveBookingScreen(
                                booking = booking,
                                onEmergencyClicked = { id ->
                                    navController.navigate(NavRoutes.AccidentReport.createRoute(id))
                                },
                                onRecordPickupInspection = { id, odo, fuel, custConf, ownerConf ->
                                    viewModel.recordPickupInspection(id, odo, fuel, custConf, ownerConf)
                                },
                                onRecordReturnInspection = { id, odo, fuel, custConf, ownerConf ->
                                    viewModel.recordReturnInspection(id, odo, fuel, custConf, ownerConf)
                                },
                                onCancelClicked = { id ->
                                    viewModel.cancelBooking(id)
                                    navController.popBackStack()
                                },
                                onBackClicked = { navController.popBackStack() }
                            )
                        }

                        // Accident Reporting Screen
                        composable(
                            route = NavRoutes.AccidentReport.route,
                            arguments = listOf(navArgument("bookingId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val bookingId = backStackEntry.arguments?.getLong("bookingId") ?: 0L
                            val booking = allBookings.find { it.bookingId == bookingId }

                            AccidentReportScreen(
                                booking = booking,
                                onSubmitAccidentReport = { desc, lat, lng, photo ->
                                    booking?.let { b ->
                                        viewModel.reportAccident(
                                            bookingId = b.bookingId,
                                            vehicleId = b.vehicleId,
                                            vehicleTitle = b.vehicleTitle,
                                            customerName = b.customerName,
                                            customerPhone = b.customerPhone,
                                            description = desc,
                                            latitude = lat,
                                            longitude = lng,
                                            photoUri = photo,
                                            onSuccess = {}
                                        )
                                    }
                                },
                                onBackClicked = { navController.popBackStack() }
                            )
                        }

                        // Customer Dashboard
                        composable(NavRoutes.CustomerDashboard.route) {
                            CustomerDashboardScreen(
                                bookings = allBookings,
                                onBookingClicked = { bookingId ->
                                    navController.navigate(NavRoutes.ActiveBooking.createRoute(bookingId))
                                },
                                onCancelBookingClicked = { bookingId ->
                                    viewModel.cancelBooking(bookingId)
                                },
                                onSearchMoreClicked = {
                                    navController.navigate(NavRoutes.Search.route)
                                }
                            )
                        }

                        // Owner Dashboard
                        composable(NavRoutes.OwnerDashboard.route) {
                            OwnerDashboardScreen(
                                ownerVehicles = allVehicles,
                                bookings = allBookings,
                                ownerCommissionPct = adminSettings?.ownerCommissionPercentage ?: 70.0,
                                onAddVehicleClicked = {
                                    navController.navigate(NavRoutes.AddVehicle.route)
                                },
                                onVehicleClicked = { vehicleId ->
                                    navController.navigate(NavRoutes.VehicleDetails.createRoute(vehicleId))
                                }
                            )
                        }

                        // Add Vehicle Form (Compliance Upload)
                        composable(NavRoutes.AddVehicle.route) {
                            AddVehicleScreen(
                                onSubmitVehicle = { title, brand, model, cat, city, ownerName, ownerPhone, price, deposit, reg, rcExp, permit, permitExp, fitNo, fitExp, photo, trans, fuel, seats, insComp, polNo, insStart, insExp, selfDrive ->
                                    viewModel.addNewVehicle(
                                        title, brand, model, cat, city, ownerName, ownerPhone, price, deposit, reg, rcExp, permit, permitExp, fitNo, fitExp, photo, trans, fuel, seats, insComp, polNo, insStart, insExp, selfDrive,
                                        onSuccess = {
                                            navController.navigate(NavRoutes.OwnerDashboard.route)
                                        }
                                    )
                                },
                                onBackClicked = { navController.popBackStack() }
                            )
                        }

                        // Admin Dashboard
                        composable(NavRoutes.AdminDashboard.route) {
                            AdminDashboardScreen(
                                adminSettings = adminSettings,
                                allVehicles = allVehicles,
                                allBookings = allBookings,
                                accidentCases = accidentCases,
                                onUpdateCommission = { ownerPct, platformPct ->
                                    viewModel.updateCommissionSettings(ownerPct, platformPct)
                                },
                                onVerifyVehicle = { vehicleId, approve ->
                                    viewModel.verifyVehicle(vehicleId, approve)
                                }
                            )
                        }

                        // Safety & Insurance Screen
                        composable(NavRoutes.SafetyInsurance.route) {
                            SafetyInsuranceScreen(onBackClicked = { navController.popBackStack() })
                        }

                        // Support Screen
                        composable(NavRoutes.Support.route) {
                            SupportScreen(onBackClicked = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
