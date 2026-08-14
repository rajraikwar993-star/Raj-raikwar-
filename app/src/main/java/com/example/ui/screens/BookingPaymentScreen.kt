package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VehicleEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingPaymentScreen(
    vehicle: VehicleEntity?,
    onConfirmBooking: (
        customerName: String,
        customerPhone: String,
        startDate: String,
        endDate: String,
        days: Int
    ) -> Unit,
    onBackClicked: () -> Unit
) {
    if (vehicle == null) return

    var customerName by remember { mutableStateOf("Aarav Gupta") }
    var customerPhone by remember { mutableStateOf("+91 99999 88888") }
    var drivingLicenceNo by remember { mutableStateOf("DL-1420230099881") }
    var licenceExpiryDate by remember { mutableStateOf("2035-12-31") }

    var durationDays by remember { mutableStateOf(3) }
    var pickupDate by remember { mutableStateOf("14 Aug 2026") }
    var pickupTime by remember { mutableStateOf("10:00 AM") }
    var returnDate by remember { mutableStateOf("17 Aug 2026") }
    var returnTime by remember { mutableStateOf("10:00 AM") }

    var agreementAccepted by remember { mutableStateOf(true) }
    var selectedPaymentMode by remember { mutableStateOf("UPI (Razorpay / GPay / PhonePe)") }

    val rentalAmount = vehicle.pricePerDay * durationDays
    val deposit = vehicle.securityDeposit
    val ownerShare = rentalAmount * 0.70
    val platformCommission = rentalAmount * 0.30
    val gstAmount = rentalAmount * 0.18
    val totalPayable = rentalAmount + deposit + gstAmount

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complete Your Booking", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBluePrimary)
            )
        },
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
                        Text(
                            text = "₹${totalPayable.toInt()}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = OrangeAccent
                            )
                        )
                        Text(
                            text = "Includes ₹${deposit.toInt()} refundable deposit",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Button(
                        onClick = {
                            if (agreementAccepted && customerName.isNotBlank() && customerPhone.isNotBlank() && drivingLicenceNo.isNotBlank()) {
                                onConfirmBooking(customerName, customerPhone, "$pickupDate $pickupTime", "$returnDate $returnTime", durationDays)
                            }
                        },
                        enabled = agreementAccepted && customerName.isNotBlank() && customerPhone.isNotBlank() && drivingLicenceNo.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangeVibrant),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("btn_pay_confirm_booking")
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pay & Confirm", fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Vehicle Summary Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(DarkBluePrimary, shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(vehicle.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkBluePrimary)
                            Text("${vehicle.city} • ${vehicle.category}", fontSize = 12.sp, color = Color.Gray)
                            Text("Reg: ${vehicle.registrationNumber} (Commercial Permit)", fontSize = 11.sp, color = VerifiedGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Dates & Duration Selector
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Rental Duration & Dates", fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("$durationDays Days Rental", fontWeight = FontWeight.Bold, color = OrangeAccent, fontSize = 16.sp)

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedButton(
                                    onClick = { if (durationDays > 1) durationDays-- },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("$durationDays", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedButton(
                                    onClick = { durationDays++ },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = CardBorderGray)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Pickup Date & Time", fontSize = 11.sp, color = Color.Gray)
                                Text("$pickupDate @ $pickupTime", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkBluePrimary)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Return Date & Time", fontSize = 11.sp, color = Color.Gray)
                                Text("$returnDate @ $returnTime", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkBluePrimary)
                            }
                        }
                    }
                }
            }

            // Customer KYC / DL Verification Input
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = DarkBluePrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Customer Renter Info & KYC Details", fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text("Full Name (as per Driving Licence)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = customerPhone,
                            onValueChange = { customerPhone = it },
                            label = { Text("Mobile Number (+91 Verified)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = drivingLicenceNo,
                                onValueChange = { drivingLicenceNo = it },
                                label = { Text("Driving Licence No.") },
                                modifier = Modifier.weight(1.2f),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = licenceExpiryDate,
                                onValueChange = { licenceExpiryDate = it },
                                label = { Text("DL Expiry") },
                                modifier = Modifier.weight(0.8f),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("DL Status: Active & Eligible for Commercial Self-Drive", fontSize = 11.sp, color = VerifiedGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Transparent Price Breakdown (With Owner/Platform split)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Price Breakdown", fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        Spacer(modifier = Modifier.height(10.dp))

                        PriceRow("Rental Fee (${durationDays} days @ ₹${vehicle.pricePerDay.toInt()}/day)", "₹${rentalAmount.toInt()}")
                        PriceRow("Refundable Security Deposit", "₹${deposit.toInt()}")
                        PriceRow("Taxes & GST (18%)", "₹${gstAmount.toInt()}")

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = CardBorderGray)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Business Model Split Transparency
                        Surface(
                            color = DarkBluePrimary.copy(alpha = 0.06f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Platform Revenue Split (Transparent)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DarkBluePrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                PriceRow("• Owner Share (70%)", "₹${ownerShare.toInt()}", isSubtitle = true)
                                PriceRow("• GaadiRent Platform Fee (30%)", "₹${platformCommission.toInt()}", isSubtitle = true)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        PriceRow("Total Amount Payable", "₹${totalPayable.toInt()}", isBold = true)
                    }
                }
            }

            // Payment Mode Selection
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Payment Options (Simulated Gateway)", fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        Spacer(modifier = Modifier.height(10.dp))

                        val modes = listOf(
                            "UPI (Razorpay / GPay / PhonePe / Paytm)",
                            "Netbanking (HDFC / ICICI / SBI / Axis)",
                            "Credit / Debit Cards (Visa / Mastercard / RuPay)"
                        )

                        modes.forEach { mode ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedPaymentMode = mode }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPaymentMode == mode,
                                    onClick = { selectedPaymentMode = mode },
                                    colors = RadioButtonDefaults.colors(selectedColor = OrangeVibrant)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(mode, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = DarkNavySecondary)
                            }
                        }
                    }
                }
            }

            // Terms & Digital Agreement Acceptance
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = agreementAccepted,
                                onCheckedChange = { agreementAccepted = it },
                                colors = CheckboxDefaults.colors(checkedColor = OrangeVibrant)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "I accept the Digital Rental Agreement, Insurance Liability terms, Speed limit (100km/h) & Pickup inspection terms.",
                                fontSize = 12.sp,
                                color = DarkNavySecondary
                            )
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
private fun PriceRow(label: String, value: String, isBold: Boolean = false, isSubtitle: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = if (isSubtitle) 11.sp else 13.sp,
            color = if (isBold) DarkBluePrimary else Color.Gray,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            value,
            fontSize = if (isSubtitle) 11.sp else 13.sp,
            color = if (isBold) OrangeAccent else DarkBluePrimary,
            fontWeight = if (isBold) FontWeight.Black else FontWeight.Bold
        )
    }
}
