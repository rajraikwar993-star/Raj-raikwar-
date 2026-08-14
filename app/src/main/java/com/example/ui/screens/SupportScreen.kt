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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(onBackClicked: () -> Unit) {
    var ticketSubject by remember { mutableStateOf("") }
    var ticketDetails by remember { mutableStateOf("") }
    var ticketSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customer Support & Helpline", fontWeight = FontWeight.Bold, color = Color.White) },
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
            // Helpline Call Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkBluePrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SupportAgent, contentDescription = null, tint = OrangeVibrant, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("24/7 GaadiRent Helpline", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                                Text("+91 1800 890 4223 (Toll Free)", fontWeight = FontWeight.Black, fontSize = 18.sp, color = OrangeAccent)
                            }
                        }
                    }
                }
            }

            // Ticket Creation Form
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Create Support Ticket", fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                        Spacer(modifier = Modifier.height(10.dp))

                        if (ticketSubmitted) {
                            Surface(
                                color = VerifiedGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Ticket #TKT-2026-881 Submitted Successfully!", fontWeight = FontWeight.Bold, color = VerifiedGreen)
                                    Text("Our support team will call you back within 15 minutes.", fontSize = 12.sp, color = DarkBluePrimary)
                                }
                            }
                        } else {
                            OutlinedTextField(
                                value = ticketSubject,
                                onValueChange = { ticketSubject = it },
                                label = { Text("Subject / Booking Issue") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = ticketDetails,
                                onValueChange = { ticketDetails = it },
                                label = { Text("Describe your query or issue...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { if (ticketSubject.isNotBlank()) ticketSubmitted = true },
                                colors = ButtonDefaults.buttonColors(containerColor = OrangeVibrant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Submit Ticket", fontWeight = FontWeight.Bold)
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
