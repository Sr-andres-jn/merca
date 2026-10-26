package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.OrderEntity
import com.example.ui.theme.FarmGreen
import com.example.ui.theme.MercaOrange
import kotlinx.coroutines.delay

@Composable
fun OrderSuccessDialog(
    order: OrderEntity?,
    onDismiss: () -> Unit
) {
    if (order == null) return

    var currentStep by remember { mutableIntStateOf(1) }

    LaunchedEffect(order.id) {
        currentStep = 1
        delay(2500)
        currentStep = 2
        delay(3500)
        currentStep = 3
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().testTag("order_success_dialog")
        ) {
            Column {
                // Green Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FarmGreen)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "¡Pedido Guardado en la Base de Datos!",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Orden #${order.id} • ${order.marketName}",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                // Live Stepper Tracker
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        color = Color(0xFFFFF7ED),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Estado en vivo del pedido:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7C2D12)
                                )
                                Text(
                                    text = "Estimado: 25 - 35 min",
                                    fontSize = 10.sp,
                                    color = MercaOrange,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            // Step 1
                            TrackingStepRow(
                                stepNumber = 1,
                                title = "Puesto notificado en la plaza",
                                subtitle = "Seleccionando frutas y verduras del día",
                                isActive = currentStep >= 1
                            )

                            // Step 2
                            TrackingStepRow(
                                stepNumber = 2,
                                title = "En camino con el repartidor",
                                subtitle = "Transporte local con canastilla aislada",
                                isActive = currentStep >= 2
                            )

                            // Step 3
                            TrackingStepRow(
                                stepNumber = 3,
                                title = "Entrega en tu puerta",
                                subtitle = order.address,
                                isActive = currentStep >= 3
                            )
                        }
                    }

                    // Order Summary Details
                    Surface(
                        color = Color(0xFFF9FAFB),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Cliente:", fontSize = 11.sp, color = Color.Gray)
                                Text(text = order.customerName, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Método:", fontSize = 11.sp, color = Color.Gray)
                                Text(text = order.paymentMethod, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Total registrado:", fontSize = 11.sp, color = Color.Gray)
                                Text(
                                    text = "$${order.total} COP",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MercaOrange
                                )
                            }
                        }
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MercaOrange)
                    ) {
                        Text(text = "Continuar Explorando Plazas", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TrackingStepRow(
    stepNumber: Int,
    title: String,
    subtitle: String,
    isActive: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isActive) FarmGreen else Color(0xFFE5E7EB)),
            contentAlignment = Alignment.Center
        ) {
            if (isActive) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            } else {
                Text(text = stepNumber.toString(), color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Column {
            Text(
                text = "$stepNumber. $title",
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp,
                color = if (isActive) Color(0xFF111827) else Color.Gray
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}
