package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.data.local.BookingEntity
import com.example.data.local.VehicleEntity
import com.example.data.local.VehicleVerificationStatus
import com.example.ui.components.VehicleStatusChip
import com.example.ui.theme.*

@Composable
fun OwnerDashboardScreen(
    ownerVehicles: List<VehicleEntity>,
    bookings: List<BookingEntity>,
    ownerCommissionPct: Double = 70.0,
    onAddVehicleClicked: () -> Unit,
    onVehicleClicked: (Long) -> Unit
) {
    // Calculate 70% Owner Share Earnings
    val totalGrossRevenue = bookings.sumOf { it.rentalAmount }
    val totalOwnerShare = totalGrossRevenue * (ownerCommissionPct / 100.0)
    val settledPayouts = totalOwnerShare * 0.80
    val pendingPayouts = totalOwnerShare * 0.20

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Owner Profile & KYC Status
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
                            Text("Rajesh Sharma", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                            Text("+91 98765 43210 • Aadhaar/PAN KYC Verified", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                        }

                        Surface(
                            color = VerifiedGreen,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("KYC VERIFIED", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }
        }

        // Owner Earnings Dashboard (70% Owner Share)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Owner Earnings Dashboard (${ownerCommissionPct.toInt()}% Share)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkBluePrimary
                            )
                        )
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = OrangeAccent)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatBox("Total Net Share", "₹${totalOwnerShare.toInt()}", OrangeAccent, Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(8.dp))
                        StatBox("Settled to Bank", "₹${settledPayouts.toInt()}", VerifiedGreen, Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(8.dp))
                        StatBox("Pending Payout", "₹${pendingPayouts.toInt()}", DarkBluePrimary, Modifier.weight(1f))
                    }
                }
            }
        }

        // Document Expiry Alerts
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = WarningAmber.copy(alpha = 0.12f)),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarningAmber),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = WarningAmber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Compliance & Document Expiry Alerts", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkBluePrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Insurance Policy POL-ICICI-9988221 valid until Jan 2027.", fontSize = 11.sp, color = DarkNavySecondary)
                    Text("• Commercial Permit (All-India) valid until May 2028.", fontSize = 11.sp, color = DarkNavySecondary)
                }
            }
        }

        // Fleet Management Header & Add Vehicle Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Vehicle Fleet (${ownerVehicles.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkBluePrimary
                    )
                )

                Button(
                    onClick = onAddVehicleClicked,
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeVibrant),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_add_vehicle_owner")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Vehicle", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // Owner Vehicles List
        if (ownerVehicles.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No vehicles added yet. Add your vehicle to start earning!", color = Color.Gray)
                    }
                }
            }
        } else {
            items(ownerVehicles) { vehicle ->
                OwnerVehicleCard(vehicle = vehicle, onClick = { onVehicleClicked(vehicle.id) })
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun OwnerVehicleCard(vehicle: VehicleEntity, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
            Text("Reg: ${vehicle.registrationNumber} • ${vehicle.city}", fontSize = 12.sp, color = Color.Gray)
            Text("Price: ₹${vehicle.pricePerDay.toInt()}/day • Deposit: ₹${vehicle.securityDeposit.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OrangeAccent)

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = CardBorderGray)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (vehicle.isInsuranceVerified) "Self-Drive Insurance Verified" else "Self-Drive Insurance Pending",
                    fontSize = 11.sp,
                    color = if (vehicle.isInsuranceVerified) VerifiedGreen else EmergencyRed,
                    fontWeight = FontWeight.Bold
                )

                TextTextButton("Manage Fleet", onClick = onClick)
            }
        }
    }
}

@Composable
fun StatBox(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Surface(
        color = SoftBackground,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGray),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Black, fontSize = 16.sp, color = valueColor)
            Spacer(modifier = Modifier.height(2.dp))
            Text(label, fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun TextTextButton(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text, fontWeight = FontWeight.Bold, color = OrangeAccent, fontSize = 12.sp)
    }
}
