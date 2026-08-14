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
import com.example.data.local.BookingEntity
import com.example.data.local.BookingStatus
import com.example.ui.theme.*

@Composable
fun CustomerDashboardScreen(
    bookings: List<BookingEntity>,
    onBookingClicked: (Long) -> Unit,
    onCancelBookingClicked: (Long) -> Unit,
    onSearchMoreClicked: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("All") }
    val tabs = listOf("All", "Active", "Upcoming", "Completed", "Cancelled")

    var agreementDialogBooking by remember { mutableStateOf<BookingEntity?>(null) }

    val filteredBookings = remember(bookings, selectedTab) {
        when (selectedTab) {
            "Active" -> bookings.filter { it.bookingStatus == BookingStatus.ACTIVE }
            "Upcoming" -> bookings.filter { it.bookingStatus == BookingStatus.REQUESTED || it.bookingStatus == BookingStatus.CONFIRMED }
            "Completed" -> bookings.filter { it.bookingStatus == BookingStatus.COMPLETED }
            "Cancelled" -> bookings.filter { it.bookingStatus == BookingStatus.CANCELLED }
            else -> bookings
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
    ) {
        // Customer Header Card
        Surface(
            color = DarkBluePrimary,
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = OrangeVibrant,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Aarav Gupta", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                            Text("+91 99999 88888 • Driving Licence Verified", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }

                    Button(
                        onClick = onSearchMoreClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = OrangeVibrant),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Search Vehicles", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tab Filter Row
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tabs) { tab ->
                        FilterChip(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            label = { Text(tab, fontSize = 12.sp) },
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
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Bookings ($selectedTab: ${filteredBookings.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkBluePrimary
                        )
                    )
                }
            }

            if (filteredBookings.isEmpty()) {
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
                            Icon(Icons.Default.EventBusy, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No $selectedTab bookings found.", color = Color.Gray, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onSearchMoreClicked,
                                colors = ButtonDefaults.buttonColors(containerColor = DarkBluePrimary)
                            ) {
                                Text("Find a Vehicle Now")
                            }
                        }
                    }
                }
            } else {
                items(filteredBookings) { booking ->
                    BookingHistoryCard(
                        booking = booking,
                        onClick = { onBookingClicked(booking.bookingId) },
                        onViewAgreement = { agreementDialogBooking = booking },
                        onCancelClick = { onCancelBookingClicked(booking.bookingId) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Digital Agreement Dialog Modal
    agreementDialogBooking?.let { booking ->
        AlertDialog(
            onDismissRequest = { agreementDialogBooking = null },
            title = { Text("Digital Rental Agreement", fontWeight = FontWeight.Bold, color = DarkBluePrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Agreement ID: AGREEMENT-#${booking.bookingId}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Vehicle: ${booking.vehicleTitle}", fontSize = 12.sp)
                    Text("Renter: ${booking.customerName} (${booking.customerPhone})", fontSize = 12.sp)
                    Text("Rental Dates: ${booking.startDate} to ${booking.endDate}", fontSize = 12.sp)
                    Divider()
                    Text(
                        "• Commercial Self-Drive License terms apply.\n" +
                                "• Speed limit capped at 100 km/h.\n" +
                                "• Zero-tolerance policy for drink-and-drive.\n" +
                                "• Emergency 1-tap accident reporting enabled for active trip.",
                        fontSize = 11.sp,
                        color = DarkNavySecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { agreementDialogBooking = null },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBluePrimary)
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun BookingHistoryCard(
    booking: BookingEntity,
    onClick: () -> Unit,
    onViewAgreement: () -> Unit,
    onCancelClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("booking_card_${booking.bookingId}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Booking #${booking.bookingId}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DarkBluePrimary
                )

                Surface(
                    color = when (booking.bookingStatus) {
                        BookingStatus.ACTIVE -> VerifiedGreen.copy(alpha = 0.15f)
                        BookingStatus.COMPLETED -> DarkBluePrimary.copy(alpha = 0.15f)
                        BookingStatus.CANCELLED -> EmergencyRed.copy(alpha = 0.15f)
                        else -> WarningAmber.copy(alpha = 0.15f)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = booking.bookingStatus.name,
                        color = when (booking.bookingStatus) {
                            BookingStatus.ACTIVE -> VerifiedGreen
                            BookingStatus.COMPLETED -> DarkBluePrimary
                            BookingStatus.CANCELLED -> EmergencyRed
                            else -> WarningAmber
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(booking.vehicleTitle, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkBluePrimary)
            Text("Dates: ${booking.startDate} to ${booking.endDate} (${booking.totalDays} Days)", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = CardBorderGray)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Paid: ₹${booking.totalAmount.toInt()}",
                        fontWeight = FontWeight.Bold,
                        color = OrangeAccent,
                        fontSize = 14.sp
                    )
                    Text("Owner Share (70%): ₹${(booking.totalAmount * 0.70).toInt()}", fontSize = 10.sp, color = Color.Gray)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TextButton(onClick = onViewAgreement) {
                        Text("Agreement", fontSize = 11.sp, color = DarkBluePrimary)
                    }

                    if (booking.bookingStatus == BookingStatus.ACTIVE) {
                        Button(
                            onClick = onClick,
                            colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Emergency / Trip", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        OutlinedButton(
                            onClick = onClick,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Details", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
