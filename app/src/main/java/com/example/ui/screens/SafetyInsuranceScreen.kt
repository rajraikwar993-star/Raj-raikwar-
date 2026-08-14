package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafetyInsuranceScreen(onBackClicked: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Safety, Insurance & Legal Policy", fontWeight = FontWeight.Bold, color = Color.White) },
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
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkBluePrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = OrangeVibrant, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Compliance-First Rental Guarantee", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "In India, Section 66 of the Motor Vehicles Act strictly mandates that private vehicles cannot be rented out for self-drive or commercial hire without appropriate commercial permits and rental insurance coverage.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item {
                PolicyCard(
                    title = "1. Commercial Self-Drive Insurance Verification",
                    description = "Standard private motor insurance policies explicitly exclude commercial hire/self-drive claims. GaadiRent verifies that listed vehicles have an active Commercial Self-Drive Insurance Policy with Zero-Depreciation coverage before making them bookable."
                )
            }

            item {
                PolicyCard(
                    title = "2. All-India / State Commercial Permit",
                    description = "Every vehicle listed on GaadiRent is required to carry a valid Commercial Self-Drive License/Permit issued by the Regional Transport Office (RTO), alongside a valid Fitness Certificate."
                )
            }

            item {
                PolicyCard(
                    title = "3. Red Button Emergency & Accident Support",
                    description = "In the event of an accident or breakdown, renters can press the Red Emergency Button in the active booking screen. This logs GPS coordinates, creates a tracked accident case number, alerts platform support, and starts the insurance claims workflow."
                )
            }

            item {
                PolicyCard(
                    title = "4. Digital Pickup & Return Inspection",
                    description = "To prevent damage disputes, both renter and owner verify and sign off on odometer readings, fuel/battery percentages, and existing condition photos during both pickup and return."
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun PolicyCard(title: String, description: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = DarkBluePrimary, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(description, fontSize = 12.sp, color = DarkNavySecondary, lineHeight = 18.sp)
        }
    }
}
