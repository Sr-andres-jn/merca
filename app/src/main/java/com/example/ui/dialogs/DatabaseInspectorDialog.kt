package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.FarmGreen
import com.example.ui.theme.MercaOrange
import com.example.ui.theme.MercaOrangeDark
import com.example.ui.viewmodel.MercaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class InspectorTable {
    USUARIOS,
    PEDIDOS,
    PRODUCTOS,
    MERCADOS
}

@Composable
fun DatabaseInspectorDialog(
    viewModel: MercaViewModel,
    onDismiss: () -> Unit
) {
    var activeTable by remember { mutableStateOf(InspectorTable.USUARIOS) }

    val users by viewModel.users.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val products by viewModel.allProducts.collectAsState()
    val markets by viewModel.markets.collectAsState()
    val stats by viewModel.dbStats.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshDbStats()
    }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CO"))

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Dark Admin Header
                Surface(
                    color = Color(0xFF1F2937),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MercaOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storage,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Base de Datos SQLite/Room",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Surface(
                                        color = FarmGreen.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(0.5.dp, FarmGreen)
                                    ) {
                                        Text(
                                            text = "Persistente",
                                            color = Color(0xFF86EFAC),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Archivo: merca_easy_db (Android SQLite)",
                                    fontSize = 10.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                        }

                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    }
                }

                // Table Selector Tabs
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item {
                        TableTabChip(
                            label = "Usuarios (${users.size})",
                            isSelected = activeTable == InspectorTable.USUARIOS,
                            onClick = { activeTable = InspectorTable.USUARIOS }
                        )
                    }
                    item {
                        TableTabChip(
                            label = "Pedidos (${orders.size})",
                            isSelected = activeTable == InspectorTable.PEDIDOS,
                            onClick = { activeTable = InspectorTable.PEDIDOS }
                        )
                    }
                    item {
                        TableTabChip(
                            label = "Productos (${products.size})",
                            isSelected = activeTable == InspectorTable.PRODUCTOS,
                            onClick = { activeTable = InspectorTable.PRODUCTOS }
                        )
                    }
                    item {
                        TableTabChip(
                            label = "Mercados (${markets.size})",
                            isSelected = activeTable == InspectorTable.MERCADOS,
                            onClick = { activeTable = InspectorTable.MERCADOS }
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFE5E7EB))

                // Table Content View
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (activeTable) {
                        InspectorTable.USUARIOS -> {
                            items(users) { u ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = "ID: ${u.id}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MercaOrangeDark)
                                            Text(text = "Proveedor: ${u.provider}", fontSize = 10.sp, color = Color.Gray)
                                        }
                                        Text(text = "Nombre: ${u.name}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                        Text(text = "Contacto: ${u.emailOrPhone}", fontSize = 11.sp, color = Color(0xFF4B5563))
                                        if (u.defaultAddress != null) {
                                            Text(text = "Dirección: ${u.defaultAddress}", fontSize = 10.sp, color = Color.Gray)
                                        }
                                        Text(
                                            text = "Registrado: ${dateFormat.format(Date(u.createdAt))}",
                                            fontSize = 9.sp,
                                            color = Color.LightGray
                                        )
                                    }
                                }
                            }
                        }
                        InspectorTable.PEDIDOS -> {
                            items(orders) { o ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = "#${o.id}", fontWeight = FontWeight.Black, fontSize = 12.sp, color = MercaOrangeDark)
                                            Text(text = o.status.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FarmGreen)
                                        }
                                        Text(text = "Cliente: ${o.customerName} (${o.phone})", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(text = "Plaza: ${o.marketName}", fontSize = 11.sp, color = Color(0xFF4B5563))
                                        Text(text = "Items: ${o.itemsSummary}", fontSize = 10.sp, color = Color.Gray)
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = "Pago: ${o.paymentMethod}", fontSize = 10.sp, color = Color.Gray)
                                            Text(text = "Total: $${o.total} COP", fontSize = 12.sp, fontWeight = FontWeight.Black, color = MercaOrangeDark)
                                        }
                                    }
                                }
                            }
                        }
                        InspectorTable.PRODUCTOS -> {
                            items(products) { p ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = p.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF1F2937))
                                            Text(text = "${p.stallName} • ${p.marketName}", fontSize = 10.sp, color = Color.Gray)
                                            Text(text = "Cat: ${p.category} | ${p.unit}", fontSize = 9.sp, color = Color.LightGray)
                                        }
                                        Text(text = "$${p.price} COP", fontWeight = FontWeight.Black, fontSize = 12.sp, color = MercaOrangeDark)
                                    }
                                }
                            }
                        }
                        InspectorTable.MERCADOS -> {
                            items(markets) { m ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = m.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF1F2937))
                                            Text(text = m.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (m.status == "Abierto") FarmGreen else Color.Red)
                                        }
                                        Text(text = "${m.vendorsCount} vendedores • Distancia: ${m.distance}", fontSize = 10.sp, color = Color.Gray)
                                        Text(text = "Horario: ${m.hours}", fontSize = 10.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFFE5E7EB))

                // Bottom Action Bar: Reset seed & Close
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { viewModel.resetDatabase() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE11D48)),
                        border = BorderStroke(1.dp, Color(0xFFFECDD3)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(text = "Restablecer Semilla", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F2937)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Cerrar Inspector", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun TableTabChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) MercaOrange else Color.White,
        border = BorderStroke(1.dp, if (isSelected) MercaOrange else Color(0xFFD1D5DB))
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF374151),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}
