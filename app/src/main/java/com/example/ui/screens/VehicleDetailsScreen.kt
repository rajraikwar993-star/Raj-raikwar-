package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.local.InsuranceEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.VehicleEntity
import com.example.ui.components.InsuranceVerifiedBadge
import com.example.ui.components.VerifiedOwnerBadge
import com.example.ui.components.VerifiedVehicleBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailsScreen(
    vehicle: VehicleEntity?,
    insurance: InsuranceEntity?,
    reviews: List<ReviewEntity>,
    onBookClicked: (Long) -> Unit,
    onBackClicked: () -> Unit
) {
    if (vehicle == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = DarkBluePrimary)
        }
        return
    }

    Scaffold(
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                        Text(
                            text = "Security Deposit: ₹${vehicle.securityDeposit.toInt()} (Refundable)",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Button(
                        onClick = { onBookClicked(vehicle.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = OrangeVibrant),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("btn_proceed_to_booking")
                    ) {
                        Icon(Icons.Default.FlashOn, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Book Now", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(SoftBackground)
        ) {
            // Header Image Banner Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(DarkBluePrimary),
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
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = vehicle.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${vehicle.brand} ${vehicle.modelName} • ${vehicle.category}",
                            color = OrangeVibrant,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IconButton(
                        onClick = onBackClicked,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            }

            // Overview & Verification Badges
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = vehicle.title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DarkBluePrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "📍 ${vehicle.city} • Commercial Reg: ${vehicle.registrationNumber}",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                            )
                        }

                        Surface(
                            color = DarkBluePrimary.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${vehicle.rating} (${vehicle.reviewCount} Reviews)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkBluePrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VerifiedVehicleBadge()
                        VerifiedOwnerBadge()
                        InsuranceVerifiedBadge(isSelfDrive = vehicle.isInsuranceVerified)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Vehicle Specifications Grid
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            SpecItem(Icons.Default.Tune, "Transmission", vehicle.transmission)
                            SpecItem(Icons.Default.LocalGasStation, "Fuel / Power", vehicle.fuelType)
                            SpecItem(Icons.Default.EventSeat, "Seating", "${vehicle.seatingCapacity} Seater")
                            SpecItem(Icons.Default.Security, "Permit", "Self-Drive")
                        }
                    }
                }
            }

            // Available Dates Indicator
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Text(
                        text = "Availability & Pickup Location",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkBluePrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Available for Instant Booking in ${vehicle.city}", fontWeight = FontWeight.Bold, color = DarkBluePrimary, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Pickup Hub: GAADIRENT Central Station Hub, ${vehicle.city}", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }

            // Vehicle Features List
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Vehicle Features & Amenities",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkBluePrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            val features = listOf(
                                "Air Conditioning (AC) & Heater",
                                "Touchscreen Infotainment with Bluetooth & GPS",
                                "Power Windows & Central Locking",
                                "Dual Airbags & Anti-lock Braking System (ABS)",
                                "Fastag pre-installed (Tolls auto-deducted)",
                                "Spare Tire & Emergency Tool Kit included"
                            )
                            features.forEach { feature ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = OrangeVibrant, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(feature, fontSize = 12.sp, color = DarkNavySecondary)
                                }
                            }
                        }
                    }
                }
            }

            // Insurance Verification Details
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Insurance Verification Record",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkBluePrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VerifiedGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = VerifiedGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = insurance?.insuranceCompany ?: "Verified Self-Drive Motor Insurance",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = DarkBluePrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = CardBorderGray)
                            Spacer(modifier = Modifier.height(8.dp))

                            InfoRow("Policy Number", insurance?.policyNumber ?: "POL-VERIFIED-9988")
                            InfoRow("Coverage Type", insurance?.coverageType ?: "Commercial Self-Drive Comprehensive Cover")
                            InfoRow("Self-Drive Applicability", if (insurance?.isSelfDriveApplicable == true || vehicle.isInsuranceVerified) "YES - Verified for Self-Drive Rental" else "NO - Private Policy Only")
                            InfoRow("Valid Period", "${insurance?.startDate ?: "2025-01-01"} to ${insurance?.expiryDate ?: "2027-01-01"}")
                        }
                    }
                }
            }

            // Required Permits & Fitness Documents
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Required Permits & Fitness Documents",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkBluePrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            InfoRow("RC Expiry", vehicle.rcExpiryDate)
                            InfoRow("Permit Type", vehicle.permitType)
                            InfoRow("Permit Expiry", vehicle.permitExpiryDate)
                            InfoRow("Fitness Cert No.", vehicle.fitnessCertificateNumber)
                            InfoRow("Fitness Expiry", vehicle.fitnessExpiryDate)
                        }
                    }
                }
            }

            // Rental & Cancellation Rules
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Rental & Cancellation Rules",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkBluePrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("📜 Rental Rules:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkBluePrimary)
                            Text("• Valid Indian Driving Licence (LMV/MCWG) required at pickup.\n" +
                                    "• Maximum speed limit: 100 km/h (Violations subject to traffic fines).\n" +
                                    "• Fuel policy: Return vehicle at same fuel level as pickup.\n" +
                                    "• Two-wheeler rentals: Helmets mandatory for driver & pillion.",
                                fontSize = 11.sp, color = DarkNavySecondary, lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = CardBorderGray)
                            Spacer(modifier = Modifier.height(8.dp))

                            Text("❌ Cancellation Policy:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkBluePrimary)
                            Text("• Free cancellation up to 24 hours prior to scheduled pickup time.\n" +
                                    "• 50% refund for cancellations within 24 hours of pickup time.\n" +
                                    "• Refundable security deposit returned within 24h post return inspection.",
                                fontSize = 11.sp, color = DarkNavySecondary, lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Vehicle Owner Info
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Vehicle Owner",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkBluePrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Owner",
                                tint = DarkBluePrimary,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = vehicle.ownerName,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkBluePrimary,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Mobile: ${vehicle.ownerPhone} (Verified Owner)",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SpecItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = DarkBluePrimary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkBluePrimary)
        Text(label, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkBluePrimary)
    }
}
