package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VehicleVerificationStatus
import com.example.ui.theme.*

@Composable
fun VerifiedVehicleBadge(modifier: Modifier = Modifier) {
    Surface(
        color = VerifiedGreen.copy(alpha = 0.12f),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VerifiedGreen),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = "Verified Vehicle",
                tint = VerifiedGreen,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Verified Vehicle",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = VerifiedGreen,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun VerifiedOwnerBadge(modifier: Modifier = Modifier) {
    Surface(
        color = DarkBluePrimary.copy(alpha = 0.08f),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Verified Owner",
                tint = DarkBluePrimary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Verified Owner",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = DarkBluePrimary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun InsuranceVerifiedBadge(isSelfDrive: Boolean = true, modifier: Modifier = Modifier) {
    val bgColor = if (isSelfDrive) OrangeVibrant.copy(alpha = 0.12f) else EmergencyRed.copy(alpha = 0.12f)
    val textColor = if (isSelfDrive) OrangeAccent else EmergencyRed

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Insurance Verified",
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isSelfDrive) "Self-Drive Insurance" else "Insurance Ineligible",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun VehicleStatusChip(status: VehicleVerificationStatus) {
    val (bgColor, textColor, label) = when (status) {
        VehicleVerificationStatus.VERIFIED -> Triple(VerifiedGreen.copy(alpha = 0.15f), VerifiedGreen, "VERIFIED")
        VehicleVerificationStatus.PENDING -> Triple(WarningAmber.copy(alpha = 0.15f), WarningAmber, "PENDING")
        VehicleVerificationStatus.UNDER_REVIEW -> Triple(DarkBluePrimary.copy(alpha = 0.15f), DarkBluePrimary, "UNDER REVIEW")
        VehicleVerificationStatus.REJECTED -> Triple(EmergencyRed.copy(alpha = 0.15f), EmergencyRed, "REJECTED")
        VehicleVerificationStatus.SUSPENDED -> Triple(EmergencyRed.copy(alpha = 0.15f), EmergencyRed, "SUSPENDED")
        VehicleVerificationStatus.EXPIRED -> Triple(WarningAmber.copy(alpha = 0.15f), WarningAmber, "EXPIRED")
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
