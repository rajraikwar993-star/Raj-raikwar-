package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBluePrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.OrangeAccent
import com.example.ui.viewmodel.UserRole

@Composable
fun RoleSwitcherHeader(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onEmergencyClicked: () -> Unit
) {
    Surface(
        color = DarkBluePrimary,
        contentColor = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "GaadiRent",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    )
                    Text(
                        text = "अपनी गाड़ी, अपनी कीमत",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OrangeAccent,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    )
                }

                // Role selector chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoleChip(
                        label = "Customer",
                        isSelected = currentRole == UserRole.CUSTOMER,
                        onClick = { onRoleSelected(UserRole.CUSTOMER) },
                        testTag = "role_customer_chip"
                    )
                    RoleChip(
                        label = "Owner",
                        isSelected = currentRole == UserRole.VEHICLE_OWNER,
                        onClick = { onRoleSelected(UserRole.VEHICLE_OWNER) },
                        testTag = "role_owner_chip"
                    )
                    RoleChip(
                        label = "Admin",
                        isSelected = currentRole == UserRole.ADMIN,
                        onClick = { onRoleSelected(UserRole.ADMIN) },
                        testTag = "role_admin_chip"
                    )
                }
            }
        }
    }
}

@Composable
private fun RoleChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        color = if (isSelected) OrangeAccent else Color.White.copy(alpha = 0.15f),
        contentColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}
