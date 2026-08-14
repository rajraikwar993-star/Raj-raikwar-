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
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccidentReportScreen(
    booking: BookingEntity?,
    onSubmitAccidentReport: (
        description: String,
        lat: Double,
        lng: Double,
        photoUri: String
    ) -> Unit,
    onBackClicked: () -> Unit
) {
    var accidentDescription by remember { mutableStateOf("Minor collision on highway side bumper. All occupants safe.") }
    var locationCaptured by remember { mutableStateOf("GPS: 28.6139° N, 77.2090° E (Delhi NCR Expressway)") }
    var photoAdded by remember { mutableStateOf(true) }
    var generatedCaseNo by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency / Accident Case", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EmergencyRed)
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
            // Local Emergency Services Disclaimer Notice (MANDATORY REQUIREMENT)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkBluePrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalPhone, contentDescription = null, tint = OrangeVibrant)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Local Emergency Services", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "NOTICE: In case of physical injury or serious collision, please call Police (112 / 100) or Medical Ambulance (108) immediately.\n\n" +
                                    "GaadiRent app assists in logging accident data, notifying support & owner, and filing insurance claims, but does NOT dispatch local emergency services directly.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                color = OrangeVibrant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Call 112 / Police",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Surface(
                                color = EmergencyRed,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Call 108 / Ambulance",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Generated Case Banner
            if (generatedCaseNo != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = VerifiedGreen.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, VerifiedGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VerifiedGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("ACCIDENT CASE GENERATED", fontWeight = FontWeight.Black, color = VerifiedGreen)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Case Number: $generatedCaseNo", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkBluePrimary)
                            Text("Platform support team & vehicle owner have been alerted. Insurance claim workflow initiated.", fontSize = 12.sp, color = DarkNavySecondary)
                        }
                    }
                }
            }

            // GPS & Location Status Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Accident Location & GPS Status", fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MyLocation, contentDescription = null, tint = OrangeAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(locationCaptured, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = DarkNavySecondary)
                        }
                    }
                }
            }

            // Accident Form
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Accident Details & Evidence Upload", fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = accidentDescription,
                            onValueChange = { accidentDescription = it },
                            label = { Text("Describe accident / damage details") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            color = SoftBackground,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGray),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = DarkBluePrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Photo Evidence Attached (1 File)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Text("Attached", color = VerifiedGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onSubmitAccidentReport(
                                    accidentDescription,
                                    28.6139,
                                    77.2090,
                                    "demo_accident_photo.jpg"
                                )
                                generatedCaseNo = "ACC-2026-9812"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_submit_accident_case")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submit Accident Case & Alert Support", fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
