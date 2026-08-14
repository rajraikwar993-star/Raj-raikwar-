package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BookingEntity
import com.example.data.local.BookingStatus
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveBookingScreen(
    booking: BookingEntity?,
    onEmergencyClicked: (Long) -> Unit,
    onRecordPickupInspection: (bookingId: Long, odo: Double, fuel: Int, custConf: Boolean, ownerConf: Boolean) -> Unit,
    onRecordReturnInspection: (bookingId: Long, odo: Double, fuel: Int, custConf: Boolean, ownerConf: Boolean) -> Unit,
    onCancelClicked: (Long) -> Unit,
    onBackClicked: () -> Unit
) {
    if (booking == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Booking not found.")
        }
        return
    }

    var pickupOdometer by remember { mutableStateOf(if (booking.pickupOdometer > 0) booking.pickupOdometer.toString() else "14250") }
    var pickupFuel by remember { mutableStateOf(booking.pickupFuelLevelPercentage.toFloat()) }
    var pickupCustConf by remember { mutableStateOf(booking.pickupConfirmedByCustomer) }
    var pickupOwnerConf by remember { mutableStateOf(booking.pickupConfirmedByOwner) }

    var returnOdometer by remember { mutableStateOf(if (booking.returnOdometer > 0) booking.returnOdometer.toString() else "14580") }
    var returnFuel by remember { mutableStateOf(booking.returnFuelLevelPercentage.toFloat()) }
    var returnCustConf by remember { mutableStateOf(booking.returnConfirmedByCustomer) }
    var returnOwnerConf by remember { mutableStateOf(booking.returnConfirmedByOwner) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Active Rental & Inspection", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBluePrimary)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(SoftBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CRITICAL REQUIREMENT: Highly visible red "EMERGENCY / ACCIDENT" button during active booking
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = EmergencyRed),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Emergency",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "EMERGENCY / ACCIDENT",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "In case of accident or breakdown, tap to capture GPS location, notify owner & support, and file an accident case number.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.9f)),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onEmergencyClicked(booking.bookingId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = EmergencyRed
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_red_emergency_accident")
                        ) {
                            Icon(Icons.Default.Emergency, contentDescription = null, tint = EmergencyRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PRESS FOR ACCIDENT REPORTING",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Booking Details Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Booking #${booking.bookingId}",
                                fontWeight = FontWeight.Bold,
                                color = DarkBluePrimary,
                                fontSize = 16.sp
                            )

                            Surface(
                                color = if (booking.bookingStatus == BookingStatus.ACTIVE) VerifiedGreen.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = booking.bookingStatus.name,
                                    color = if (booking.bookingStatus == BookingStatus.ACTIVE) VerifiedGreen else Color.DarkGray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(booking.vehicleTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkBluePrimary)
                        Text("Renter: ${booking.customerName} (${booking.customerPhone})", fontSize = 12.sp, color = Color.Gray)
                        Text("Dates: ${booking.startDate} to ${booking.endDate} (${booking.totalDays} Days)", fontSize = 12.sp, color = DarkNavySecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Total Paid: ₹${booking.totalAmount.toInt()} (${booking.paymentStatus})", fontWeight = FontWeight.Bold, color = OrangeAccent, fontSize = 13.sp)
                    }
                }
            }

            // PICKUP INSPECTION RECORD
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Speed, contentDescription = null, tint = DarkBluePrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("1. Pickup Inspection Record", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkBluePrimary)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = pickupOdometer,
                            onValueChange = { pickupOdometer = it },
                            label = { Text("Odometer Reading (km)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Fuel / Battery Level: ${pickupFuel.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        Slider(
                            value = pickupFuel,
                            onValueChange = { pickupFuel = it },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(thumbColor = OrangeVibrant, activeTrackColor = OrangeVibrant)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = pickupCustConf, onCheckedChange = { pickupCustConf = it })
                            Text("Customer Verified Vehicle Condition", fontSize = 12.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = pickupOwnerConf, onCheckedChange = { pickupOwnerConf = it })
                            Text("Owner Handover Confirmed", fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                onRecordPickupInspection(
                                    booking.bookingId,
                                    pickupOdometer.toDoubleOrNull() ?: 14250.0,
                                    pickupFuel.toInt(),
                                    pickupCustConf,
                                    pickupOwnerConf
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkBluePrimary),
                            modifier = Modifier.fillMaxWidth().testTag("btn_save_pickup_inspection")
                        ) {
                            Text("Save Pickup Inspection Record")
                        }
                    }
                }
            }

            // RETURN INSPECTION RECORD
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VerifiedGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("2. Return Inspection & Deposit Settlement", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkBluePrimary)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = returnOdometer,
                            onValueChange = { returnOdometer = it },
                            label = { Text("Return Odometer Reading (km)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Return Fuel / Battery Level: ${returnFuel.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        Slider(
                            value = returnFuel,
                            onValueChange = { returnFuel = it },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(thumbColor = VerifiedGreen, activeTrackColor = VerifiedGreen)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = returnCustConf, onCheckedChange = { returnCustConf = it })
                            Text("Customer Return Handover Sign-off", fontSize = 12.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = returnOwnerConf, onCheckedChange = { returnOwnerConf = it })
                            Text("Owner Accept Return & Release Deposit (₹${booking.securityDeposit.toInt()})", fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                onRecordReturnInspection(
                                    booking.bookingId,
                                    returnOdometer.toDoubleOrNull() ?: 14580.0,
                                    returnFuel.toInt(),
                                    returnCustConf,
                                    returnOwnerConf
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen),
                            modifier = Modifier.fillMaxWidth().testTag("btn_complete_return_inspection")
                        ) {
                            Text("Complete Return & Release Security Deposit")
                        }
                    }
                }
            }

            // Cancellation option
            if (booking.bookingStatus == BookingStatus.ACTIVE || booking.bookingStatus == BookingStatus.REQUESTED) {
                item {
                    OutlinedButton(
                        onClick = { onCancelClicked(booking.bookingId) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmergencyRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyRed),
                        modifier = Modifier.fillMaxWidth().testTag("btn_cancel_booking")
                    ) {
                        Text("Cancel Booking & Refund", fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
