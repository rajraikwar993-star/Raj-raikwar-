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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleScreen(
    onSubmitVehicle: (
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
        isSelfDriveApplicable: Boolean
    ) -> Unit,
    onBackClicked: () -> Unit
) {
    var title by remember { mutableStateOf("Maruti Swift ZXi Plus") }
    var brand by remember { mutableStateOf("Maruti Suzuki") }
    var modelName by remember { mutableStateOf("Swift ZXi") }
    var category by remember { mutableStateOf("Hatchback") }
    var city by remember { mutableStateOf("Delhi NCR") }
    var pricePerDay by remember { mutableStateOf("1800") }
    var securityDeposit by remember { mutableStateOf("3000") }

    var regNumber by remember { mutableStateOf("DL 03 CB 4411") }
    var rcExpiry by remember { mutableStateOf("2033-06-15") }
    var permitType by remember { mutableStateOf("Commercial Self-Drive Permit (All-India)") }
    var permitExpiry by remember { mutableStateOf("2028-06-15") }
    var fitnessNumber by remember { mutableStateOf("FIT-DL-2024-4411") }
    var fitnessExpiry by remember { mutableStateOf("2027-06-15") }

    var insuranceCompany by remember { mutableStateOf("National Insurance Company Ltd") }
    var policyNumber by remember { mutableStateOf("POL-NAT-441122") }
    var insuranceStart by remember { mutableStateOf("2025-01-01") }
    var insuranceExpiry by remember { mutableStateOf("2027-01-01") }
    var isSelfDriveApplicable by remember { mutableStateOf(true) }

    var transmission by remember { mutableStateOf("Manual") }
    var fuelType by remember { mutableStateOf("Petrol") }
    var seatingCapacity by remember { mutableStateOf(5) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("List Vehicle for Rent", fontWeight = FontWeight.Bold, color = Color.White) },
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
            // Notice Card regarding Indian Rental Laws
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkBluePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = OrangeVibrant)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Compliance Notice", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Standard private motor insurance DOES NOT cover self-drive rental use in India. Please upload valid Commercial Self-Drive Insurance Policy and Permit details.",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Basic Vehicle Info Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("1. Basic Vehicle Info", fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Vehicle Display Title") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = brand,
                                onValueChange = { brand = it },
                                label = { Text("Brand") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = modelName,
                                onValueChange = { modelName = it },
                                label = { Text("Model") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = category,
                                onValueChange = { category = it },
                                label = { Text("Category (SUV/Hatchback/EV...)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = city,
                                onValueChange = { city = it },
                                label = { Text("City") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = pricePerDay,
                                onValueChange = { pricePerDay = it },
                                label = { Text("Price / Day (₹)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = securityDeposit,
                                onValueChange = { securityDeposit = it },
                                label = { Text("Security Deposit (₹)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            // Registration & Permits
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("2. RC & Permit Documents", fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = regNumber,
                            onValueChange = { regNumber = it },
                            label = { Text("RC Registration Number (e.g. DL 01 AB 1234)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = permitType,
                            onValueChange = { permitType = it },
                            label = { Text("Permit Type (Self-Drive Commercial)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = rcExpiry,
                                onValueChange = { rcExpiry = it },
                                label = { Text("RC Expiry (YYYY-MM-DD)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = permitExpiry,
                                onValueChange = { permitExpiry = it },
                                label = { Text("Permit Expiry") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            // Insurance Compliance Check Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isSelfDriveApplicable) VerifiedGreen else EmergencyRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = if (isSelfDriveApplicable) VerifiedGreen else EmergencyRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("3. Insurance Policy Verification", fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = insuranceCompany,
                            onValueChange = { insuranceCompany = it },
                            label = { Text("Insurance Company") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = policyNumber,
                            onValueChange = { policyNumber = it },
                            label = { Text("Policy Number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Crucial Compliance Toggle
                        Surface(
                            color = if (isSelfDriveApplicable) VerifiedGreen.copy(alpha = 0.1f) else EmergencyRed.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isSelfDriveApplicable,
                                    onCheckedChange = { isSelfDriveApplicable = it },
                                    colors = CheckboxDefaults.colors(checkedColor = VerifiedGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        "Policy explicitly covers Commercial Self-Drive Rental",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = DarkBluePrimary
                                    )
                                    Text(
                                        if (isSelfDriveApplicable) "Eligible for Marketplace Verification" else "INELIGIBLE: Standard private policy",
                                        fontSize = 11.sp,
                                        color = if (isSelfDriveApplicable) VerifiedGreen else EmergencyRed
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = {
                        onSubmitVehicle(
                            title,
                            brand,
                            modelName,
                            category,
                            city,
                            "Rajesh Sharma",
                            "+91 98765 43210",
                            pricePerDay.toDoubleOrNull() ?: 1800.0,
                            securityDeposit.toDoubleOrNull() ?: 3000.0,
                            regNumber,
                            rcExpiry,
                            permitType,
                            permitExpiry,
                            fitnessNumber,
                            fitnessExpiry,
                            "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=800&q=80",
                            transmission,
                            fuelType,
                            seatingCapacity,
                            insuranceCompany,
                            policyNumber,
                            insuranceStart,
                            insuranceExpiry,
                            isSelfDriveApplicable
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeVibrant),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_submit_vehicle_compliance")
                ) {
                    Text("Submit Vehicle for Compliance Review", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
