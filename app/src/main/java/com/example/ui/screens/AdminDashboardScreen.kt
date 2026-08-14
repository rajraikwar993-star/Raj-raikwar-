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
import com.example.data.local.AccidentCaseEntity
import com.example.data.local.AdminSettingsEntity
import com.example.data.local.BookingEntity
import com.example.data.local.VehicleEntity
import com.example.data.local.VehicleVerificationStatus
import com.example.ui.components.VehicleStatusChip
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    adminSettings: AdminSettingsEntity?,
    allVehicles: List<VehicleEntity>,
    allBookings: List<BookingEntity>,
    accidentCases: List<AccidentCaseEntity>,
    onUpdateCommission: (ownerPct: Double, platformPct: Double) -> Unit,
    onVerifyVehicle: (vehicleId: Long, approve: Boolean) -> Unit
) {
    val ownerPct = adminSettings?.ownerCommissionPercentage ?: 70.0
    val platformPct = adminSettings?.platformCommissionPercentage ?: 30.0

    var sliderOwnerValue by remember(ownerPct) { mutableStateOf(ownerPct.toFloat()) }

    val totalBookingsRevenue = allBookings.sumOf { it.rentalAmount }
    val platformRevenue = totalBookingsRevenue * (platformPct / 100.0)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkBluePrimary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("GaadiRent Admin Control Center", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                            Text("Compliance, Verification & Commission Management", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = OrangeVibrant, modifier = Modifier.size(32.dp))
                    }
                }
            }
        }

        // Overview Stat Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard("Vehicles", "${allVehicles.size}", DarkBluePrimary, Modifier.weight(1f))
                StatCard("Active Rentals", "${allBookings.size}", OrangeAccent, Modifier.weight(1f))
                StatCard("Platform Rev.", "₹${platformRevenue.toInt()}", VerifiedGreen, Modifier.weight(1f))
            }
        }

        // Configurable Revenue Split Settings (CORE REQUIREMENT)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = DarkBluePrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Revenue Split Controller",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkBluePrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Set marketplace revenue share per completed booking. Default is 70% Owner / 30% Platform.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Vehicle Owner Share: ${sliderOwnerValue.toInt()}%",
                            fontWeight = FontWeight.Bold,
                            color = OrangeAccent,
                            fontSize = 14.sp
                        )

                        Text(
                            text = "Platform Fee: ${(100 - sliderOwnerValue.toInt())}%",
                            fontWeight = FontWeight.Bold,
                            color = DarkBluePrimary,
                            fontSize = 14.sp
                        )
                    }

                    Slider(
                        value = sliderOwnerValue,
                        onValueChange = { sliderOwnerValue = it },
                        valueRange = 50f..90f,
                        steps = 39,
                        colors = SliderDefaults.colors(thumbColor = OrangeVibrant, activeTrackColor = OrangeVibrant),
                        modifier = Modifier.testTag("slider_commission_split")
                    )

                    Button(
                        onClick = {
                            val owner = sliderOwnerValue.toDouble()
                            val platform = 100.0 - owner
                            onUpdateCommission(owner, platform)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBluePrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_save_commission")
                    ) {
                        Text("Save Commission Settings")
                    }
                }
            }
        }

        // Vehicle Compliance & Verification Queue
        item {
            Text(
                text = "Vehicle Verification Queue (${allVehicles.size})",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBluePrimary
                )
            )
        }

        items(allVehicles) { vehicle ->
            AdminVehicleReviewCard(
                vehicle = vehicle,
                onApprove = { onVerifyVehicle(vehicle.id, true) },
                onReject = { onVerifyVehicle(vehicle.id, false) }
            )
        }

        // Accident Cases & Claims Section
        item {
            Text(
                text = "Accident Cases & Claims Center (${accidentCases.size})",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBluePrimary
                )
            )
        }

        if (accidentCases.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("No accident cases reported.", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(accidentCases) { accident ->
                AdminAccidentCard(accident = accident)
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AdminVehicleReviewCard(
    vehicle: VehicleEntity,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(vehicle.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkBluePrimary)
                VehicleStatusChip(status = vehicle.status)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text("Owner: ${vehicle.ownerName} (${vehicle.ownerPhone})", fontSize = 12.sp, color = Color.Gray)
            Text("Reg No: ${vehicle.registrationNumber} • Permit: ${vehicle.permitType}", fontSize = 12.sp, color = DarkNavySecondary)
            Text("Self-Drive Insurance Status: ${if (vehicle.isInsuranceVerified) "VERIFIED COMPLIANT" else "UNVERIFIED / PENDING"}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (vehicle.isInsuranceVerified) VerifiedGreen else EmergencyRed)

            Spacer(modifier = Modifier.height(10.dp))

            if (vehicle.status != VehicleVerificationStatus.VERIFIED) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Approve & Go Live", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onReject,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmergencyRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reject", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminAccidentCard(accident: AccidentCaseEntity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyRed),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(accident.caseNumber, fontWeight = FontWeight.Black, color = EmergencyRed, fontSize = 14.sp)
                Text(accident.dateTimeStamp, fontSize = 11.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text("Vehicle: ${accident.vehicleTitle}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkBluePrimary)
            Text("Renter: ${accident.customerName} (${accident.customerPhone})", fontSize = 12.sp, color = DarkNavySecondary)
            Text("Location: ${accident.locationAddress}", fontSize = 11.sp, color = Color.Gray)
            Text("Details: ${accident.description}", fontSize = 12.sp, color = DarkNavySecondary)
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Black, fontSize = 18.sp, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, fontSize = 10.sp, color = Color.Gray)
        }
    }
}
