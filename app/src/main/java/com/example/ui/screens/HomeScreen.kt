package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VehicleEntity
import com.example.ui.components.InsuranceVerifiedBadge
import com.example.ui.components.VerifiedOwnerBadge
import com.example.ui.components.VerifiedVehicleBadge
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    featuredVehicles: List<VehicleEntity>,
    onSearchClicked: (category: String?) -> Unit,
    onListVehicleClicked: () -> Unit,
    onVehicleClicked: (Long) -> Unit,
    onSafetyInfoClicked: () -> Unit,
    onSupportClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
    ) {
        // Hero Section
        item {
            HeroSection(
                onSearchClicked = { onSearchClicked(null) },
                onListVehicleClicked = onListVehicleClicked
            )
        }

        // Key Value Props
        item {
            ValuePropsBar()
        }

        // Popular Vehicle Categories
        item {
            CategoriesSection(onCategorySelected = { cat -> onSearchClicked(cat) })
        }

        // Featured Verified Vehicles
        item {
            FeaturedVehiclesSection(
                vehicles = featuredVehicles,
                onVehicleClicked = onVehicleClicked,
                onViewAllClicked = { onSearchClicked(null) }
            )
        }

        // Compliance First Safety Banner
        item {
            ComplianceSafetyBanner(onLearnMoreClicked = onSafetyInfoClicked)
        }

        // How It Works
        item {
            HowItWorksSection()
        }

        // Customer Reviews & FAQ
        item {
            CustomerReviewsSection()
        }

        item {
            FaqSection(onSupportClicked = onSupportClicked)
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HeroSection(
    onSearchClicked: () -> Unit,
    onListVehicleClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBluePrimary, DarkNavySecondary)
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = OrangeAccent.copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, OrangeVibrant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Verified",
                        tint = OrangeVibrant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Compliance-First Vehicle Rental Marketplace",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = OrangeVibrant,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "GaadiRent",
                style = MaterialTheme.typography.displayMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 36.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "अपनी गाड़ी, अपनी कीमत",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = OrangeVibrant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Connect with verified vehicle owners across India. Every listed vehicle is checked for commercial permit, self-drive insurance & RC validity.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.85f)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons requested by user
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onSearchClicked,
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeVibrant),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("btn_search_vehicles")
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "गाड़ी खोजें",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                OutlinedButton(
                    onClick = onListVehicleClicked,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("btn_list_vehicle")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddBusiness,
                        contentDescription = "Rent",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "अपनी गाड़ी किराए पर दें",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ValuePropsBar() {
    Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ValueItem(icon = Icons.Default.Verified, title = "Verified Vehicles")
            ValueItem(icon = Icons.Default.Shield, title = "Insurance Verified")
            ValueItem(icon = Icons.Default.Lock, title = "Safe Booking")
        }
    }
}

@Composable
private fun ValueItem(icon: ImageVector, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(OrangeVibrant.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = OrangeAccent,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = DarkBluePrimary
            )
        )
    }
}

@Composable
private fun CategoriesSection(onCategorySelected: (String) -> Unit) {
    val categories = listOf(
        Pair("SUV", Icons.Default.DirectionsCar),
        Pair("EV", Icons.Default.ElectricCar),
        Pair("Hatchback", Icons.Default.DirectionsCar),
        Pair("Sedan", Icons.Default.DirectionsCar),
        Pair("Bike/Scooter", Icons.Default.TwoWheeler),
        Pair("Luxury", Icons.Default.Star)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Popular Vehicle Categories",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkBluePrimary
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { cat ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .clickable { onCategorySelected(cat.first) }
                        .testTag("cat_${cat.first}")
                ) {
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(DarkBluePrimary.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = cat.second,
                                contentDescription = cat.first,
                                tint = DarkBluePrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = cat.first,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkBluePrimary
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturedVehiclesSection(
    vehicles: List<VehicleEntity>,
    onVehicleClicked: (Long) -> Unit,
    onViewAllClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Verified Vehicles Near You",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBluePrimary
                )
            )
            TextTextButton(text = "View All", onClick = onViewAllClicked)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (vehicles.isEmpty()) {
            Text("No vehicles currently available.", color = Color.Gray)
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(vehicles) { vehicle ->
                    VehicleHomeCard(vehicle = vehicle, onClick = { onVehicleClicked(vehicle.id) })
                }
            }
        }
    }
}

@Composable
fun VehicleHomeCard(vehicle: VehicleEntity, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier
            .width(260.dp)
            .clickable { onClick() }
            .testTag("vehicle_card_${vehicle.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.LightGray)
            ) {
                // Photo placeholder / image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(DarkNavySecondary, DarkBluePrimary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (vehicle.category == "Bike/Scooter") Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                        contentDescription = vehicle.title,
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(54.dp)
                    )
                }

                // Verified Badges overlay
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    VerifiedVehicleBadge()
                }

                Surface(
                    color = DarkBluePrimary.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = vehicle.city,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = vehicle.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkBluePrimary
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = WarningAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${vehicle.rating} (${vehicle.reviewCount}) • ${vehicle.transmission} • ${vehicle.fuelType}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                InsuranceVerifiedBadge(isSelfDrive = vehicle.isInsuranceVerified)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "₹${vehicle.pricePerDay.toInt()}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = OrangeAccent
                            )
                        )
                        Text(
                            text = "per day",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                        )
                    }

                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBluePrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Book", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ComplianceSafetyBanner(onLearnMoreClicked: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkBluePrimary),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Gavel,
                    contentDescription = "Compliance",
                    tint = OrangeVibrant,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Compliance-First Marketplace",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "In India, private vehicles cannot be legally rented without proper commercial self-drive permits and comprehensive rental insurance. GaadiRent verifies RC, Insurance Policy, and Fitness before listing any vehicle.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.85f))
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onLearnMoreClicked,
                colors = ButtonDefaults.textButtonColors(contentColor = OrangeVibrant)
            ) {
                Text("Read Safety & Legal Compliance Policy →", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun HowItWorksSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "How It Works",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkBluePrimary
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StepCard("1", "Search & Filter", "Find verified vehicles in your city", Modifier.weight(1f))
            StepCard("2", "Book & Agree", "Sign digital agreement & pay deposit", Modifier.weight(1f))
            StepCard("3", "Inspect & Drive", "Verify odometer & fuel level", Modifier.weight(1f))
        }
    }
}

@Composable
private fun StepCard(step: String, title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(OrangeVibrant),
                contentAlignment = Alignment.Center
            ) {
                Text(step, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkBluePrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun CustomerReviewsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Customer Reviews",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkBluePrimary
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = WarningAmber)
                    Icon(Icons.Default.Star, contentDescription = null, tint = WarningAmber)
                    Icon(Icons.Default.Star, contentDescription = null, tint = WarningAmber)
                    Icon(Icons.Default.Star, contentDescription = null, tint = WarningAmber)
                    Icon(Icons.Default.Star, contentDescription = null, tint = WarningAmber)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("5.0", fontWeight = FontWeight.Bold, color = DarkBluePrimary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "\"The insurance verified badge gave me total peace of mind! The digital pickup inspection took 2 minutes. Transparent deposit refund after return.\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkNavySecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text("— Vikram A., Delhi NCR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun FaqSection(onSupportClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Frequently Asked Questions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBluePrimary
                )
            )
            TextTextButton("Support", onClick = onSupportClicked)
        }

        Spacer(modifier = Modifier.height(8.dp))

        FaqItem(
            q = "Can any private car owner list their vehicle on GaadiRent?",
            a = "No. Vehicles must possess valid Commercial Self-Drive Permits and active Insurance policies covering self-drive rental use."
        )
        Spacer(modifier = Modifier.height(8.dp))
        FaqItem(
            q = "How does the owner earnings split work?",
            a = "70% of the net rental amount goes directly to the vehicle owner. 30% platform commission covers insurance verification, marketing & customer support."
        )
        Spacer(modifier = Modifier.height(8.dp))
        FaqItem(
            q = "What happens in case of an emergency or accident?",
            a = "During an active booking, press the RED EMERGENCY / ACCIDENT button. It logs your GPS location, alerts owner and support, generates an accident case number, and initiates insurance workflow."
        )
    }
}

@Composable
private fun FaqItem(q: String, a: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGray),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(q, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkBluePrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(a, fontSize = 12.sp, color = DarkNavySecondary)
        }
    }
}

@Composable
private fun TextTextButton(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text, fontWeight = FontWeight.Bold, color = OrangeAccent, fontSize = 13.sp)
    }
}
