package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VehicleEntity
import com.example.data.local.VehicleVerificationStatus
import com.example.ui.components.InsuranceVerifiedBadge
import com.example.ui.components.VerifiedOwnerBadge
import com.example.ui.components.VerifiedVehicleBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.VehicleFilterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    vehicles: List<VehicleEntity>,
    filterState: VehicleFilterState,
    onFilterChanged: (city: String?, category: String?, query: String?, maxPrice: Double?) -> Unit,
    onVehicleClicked: (Long) -> Unit
) {
    var searchInput by remember { mutableStateOf(filterState.searchQuery) }
    var selectedCity by remember { mutableStateOf(filterState.selectedCity) }
    var selectedCategory by remember { mutableStateOf(filterState.selectedCategory) }
    var maxPriceLimit by remember { mutableStateOf(filterState.maxPricePerDay) }

    // Date and time selection states
    var pickupDate by remember { mutableStateOf("14 Aug 2026") }
    var returnDate by remember { mutableStateOf("17 Aug 2026") }
    var pickupTime by remember { mutableStateOf("10:00 AM") }
    var returnTime by remember { mutableStateOf("10:00 AM") }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    val cities = listOf("Delhi NCR", "Bengaluru", "Mumbai", "Goa", "Pune", "Hyderabad", "Jaipur", "All Cities")
    val categories = listOf("All", "Hatchback", "Sedan", "SUV", "EV", "Bike", "Scooter", "Luxury")

    // Filter vehicles to show only VERIFIED and valid insurance & permits
    val compliantVehicles = remember(vehicles, selectedCity, selectedCategory, searchInput, maxPriceLimit) {
        vehicles.filter { vehicle ->
            val isCompliant = vehicle.status == VehicleVerificationStatus.VERIFIED &&
                    vehicle.isVerifiedVehicle &&
                    vehicle.isInsuranceVerified

            val matchesCity = if (selectedCity == "All Cities") true else vehicle.city.equals(selectedCity, ignoreCase = true)
            val matchesCategory = if (selectedCategory == "All") true else {
                if (selectedCategory == "Bike" || selectedCategory == "Scooter") {
                    vehicle.category.contains("Bike", ignoreCase = true) || vehicle.category.contains("Scooter", ignoreCase = true)
                } else {
                    vehicle.category.contains(selectedCategory, ignoreCase = true)
                }
            }
            val matchesSearch = searchInput.isBlank() ||
                    vehicle.title.contains(searchInput, ignoreCase = true) ||
                    vehicle.brand.contains(searchInput, ignoreCase = true) ||
                    vehicle.modelName.contains(searchInput, ignoreCase = true) ||
                    vehicle.city.contains(searchInput, ignoreCase = true)
            val matchesPrice = vehicle.pricePerDay <= maxPriceLimit

            isCompliant && matchesCity && matchesCategory && matchesSearch && matchesPrice
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
    ) {
        // Search & Filter Header Surface
        Surface(
            color = DarkBluePrimary,
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Location Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = OrangeVibrant, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("Current Location", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                            Text(selectedCity, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                        }
                    }

                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "GAADIRENT • Verified",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeVibrant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search input bar
                OutlinedTextField(
                    value = searchInput,
                    onValueChange = {
                        searchInput = it
                        onFilterChanged(selectedCity, selectedCategory, it, maxPriceLimit)
                    },
                    placeholder = { Text("Search brand, model or city...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = OrangeAccent) },
                    trailingIcon = {
                        if (searchInput.isNotBlank()) {
                            IconButton(onClick = {
                                searchInput = ""
                                onFilterChanged(selectedCity, selectedCategory, "", maxPriceLimit)
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.15f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                        focusedBorderColor = OrangeVibrant,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_text_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Date & Time Selector Panel
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.DateRange, contentDescription = null, tint = OrangeVibrant, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("PICKUP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.8f))
                                }
                                Text("$pickupDate @ $pickupTime", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                            }

                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White.copy(0.6f), modifier = Modifier.size(16.dp))

                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Event, contentDescription = null, tint = OrangeVibrant, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("RETURN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.8f))
                                }
                                Text("$returnDate @ $returnTime", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // City Selector Chips
                Text("Select City / शहर:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(cities) { city ->
                        FilterChip(
                            selected = selectedCity == city,
                            onClick = {
                                selectedCity = city
                                onFilterChanged(city, selectedCategory, searchInput, maxPriceLimit)
                            },
                            label = { Text(city, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = OrangeVibrant,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White.copy(alpha = 0.15f),
                                labelColor = Color.White
                            ),
                            border = null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Vehicle Category Chips
                Text("Category / श्रेणी:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = {
                                selectedCategory = cat
                                onFilterChanged(selectedCity, cat, searchInput, maxPriceLimit)
                            },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = OrangeAccent,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White.copy(alpha = 0.15f),
                                labelColor = Color.White
                            ),
                            border = null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price Range Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Max Price / Day: ₹${maxPriceLimit.toInt()}", fontSize = 11.sp, color = Color.White.copy(0.9f))
                    TextButton(onClick = {
                        selectedCity = "All Cities"
                        selectedCategory = "All"
                        maxPriceLimit = 10000.0
                        searchInput = ""
                        onFilterChanged("All Cities", "All", "", 10000.0)
                    }) {
                        Text("Reset All", fontSize = 11.sp, color = OrangeVibrant)
                    }
                }
                Slider(
                    value = maxPriceLimit.toFloat(),
                    onValueChange = {
                        maxPriceLimit = it.toDouble()
                        onFilterChanged(selectedCity, selectedCategory, searchInput, maxPriceLimit)
                    },
                    valueRange = 500f..10000f,
                    colors = SliderDefaults.colors(thumbColor = OrangeVibrant, activeTrackColor = OrangeVibrant)
                )
            }
        }

        // Vehicles Results
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "गाड़ी खोजें • ${compliantVehicles.size} Vehicles Found",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkBluePrimary
                    )
                )

                Surface(
                    color = VerifiedGreen.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "100% Commercial Permit Compliant",
                        color = VerifiedGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (compliantVehicles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "No vehicles",
                            tint = Color.Gray,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No verified vehicles matched your criteria in $selectedCity.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                selectedCity = "All Cities"
                                selectedCategory = "All"
                                maxPriceLimit = 10000.0
                                searchInput = ""
                                onFilterChanged("All Cities", "All", "", 10000.0)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkBluePrimary)
                        ) {
                            Text("View All Vehicles")
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(compliantVehicles) { vehicle ->
                        SearchVehicleResultCard(
                            vehicle = vehicle,
                            onClick = { onVehicleClicked(vehicle.id) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SearchVehicleResultCard(vehicle: VehicleEntity, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("search_card_${vehicle.id}")
    ) {
        Column {
            // Vehicle Header Visual Card Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(DarkBluePrimary.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = when {
                            vehicle.category.contains("Bike", true) -> Icons.Default.TwoWheeler
                            vehicle.category.contains("Scooter", true) -> Icons.Default.TwoWheeler
                            vehicle.category.contains("EV", true) -> Icons.Default.ElectricCar
                            else -> Icons.Default.DirectionsCar
                        },
                        contentDescription = vehicle.title,
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${vehicle.brand} • ${vehicle.category}",
                        color = OrangeVibrant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Top Floating Badge
                Surface(
                    color = VerifiedGreen,
                    shape = RoundedCornerShape(bottomStart = 10.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "SELF-DRIVE PERMIT",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = vehicle.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkBluePrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "📍 ${vehicle.city} • Reg: ${vehicle.registrationNumber}",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }

                    Surface(
                        color = DarkBluePrimary.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("${vehicle.rating} (${vehicle.reviewCount})", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DarkBluePrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Badges row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VerifiedVehicleBadge()
                    InsuranceVerifiedBadge(isSelfDrive = vehicle.isInsuranceVerified)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Divider(color = CardBorderGray, thickness = 0.8.dp)

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹${vehicle.pricePerDay.toInt()}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = OrangeAccent
                                )
                            )
                            Text(" / day", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                        }
                        Text("Deposit: ₹${vehicle.securityDeposit.toInt()} (Refundable)", fontSize = 10.sp, color = Color.Gray)
                    }

                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(containerColor = OrangeVibrant),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Book Now", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
