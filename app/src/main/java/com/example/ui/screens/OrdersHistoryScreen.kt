package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderEntity
import com.example.ui.theme.FarmGreen
import com.example.ui.theme.MercaOrange
import com.example.ui.theme.MercaOrangeDark
import com.example.ui.theme.MercaOrangeLight
import com.example.ui.theme.WarmBackground
import com.example.ui.viewmodel.MercaViewModel
import com.example.ui.viewmodel.ScreenTab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersHistoryScreen(
    viewModel: MercaViewModel,
    onTrackOrder: (OrderEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale("es", "CO"))

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
    ) {
        if (orders.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MercaOrangeLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = MercaOrangeDark,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Aún no tienes pedidos registrados",
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = "Cuando realices compras en las plazas de mercado de Madrid, quedarán guardadas permanentemente en tu base de datos.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.setTab(ScreenTab.INICIO) },
                    colors = ButtonDefaults.buttonColors(containerColor = MercaOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Explorar Plazas de Mercado", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Mis Pedidos de Plaza",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = "Historial sincronizado en SQLite / Room",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Surface(
                            color = MercaOrangeLight,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "${orders.size} guardados",
                                color = MercaOrangeDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                items(orders) { order ->
                    OrderHistoryCard(
                        order = order,
                        formattedDate = dateFormat.format(Date(order.createdAt)),
                        onTrackClick = { onTrackOrder(order) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(
    order: OrderEntity,
    formattedDate: String,
    onTrackClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFFED7AA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTrackClick() }
            .testTag("order_item_${order.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFFFF7ED),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(0.5.dp, Color(0xFFFED7AA))
                ) {
                    Text(
                        text = "#${order.id}",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = MercaOrangeDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Status pill
                val isDone = order.status == "entregado"
                Surface(
                    color = if (isDone) Color(0xFFD1FAE5) else Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = if (isDone) FarmGreen else Color(0xFFB45309),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = when (order.status) {
                                "preparando" -> "En preparación"
                                "en_camino" -> "En camino"
                                else -> "Entregado"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDone) FarmGreen else Color(0xFFB45309)
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.Store, contentDescription = null, tint = MercaOrange, modifier = Modifier.size(14.dp))
                Text(text = order.marketName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF1F2937))
            }

            Text(
                text = order.itemsSummary,
                fontSize = 11.sp,
                color = Color(0xFF4B5563),
                maxLines = 2
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = formattedDate, fontSize = 10.sp, color = Color.Gray)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "$${order.total} COP",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = MercaOrangeDark
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Rastrear",
                        tint = MercaOrangeDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
