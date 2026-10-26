package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.repository.CartItemWithProduct
import com.example.ui.theme.FarmGreen
import com.example.ui.theme.MercaOrange
import com.example.ui.theme.MercaOrangeDark
import com.example.ui.theme.MercaOrangeLight
import com.example.ui.theme.WarmBackground
import com.example.ui.viewmodel.MercaViewModel
import com.example.ui.viewmodel.ScreenTab

@Composable
fun CartScreen(
    viewModel: MercaViewModel,
    modifier: Modifier = Modifier
) {
    val cart by viewModel.cart.collectAsState()

    val subtotal = cart.sumOf { it.product.price * it.quantity }
    val deliveryFee = if (subtotal > 0) 4500 else 0
    val discount = if (subtotal >= 30000) 3000 else 0
    val total = (subtotal + deliveryFee - discount).coerceAtLeast(0)

    val itemsByMarket = cart.groupBy { it.product.marketName }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
    ) {
        if (cart.isEmpty()) {
            // Empty Basket
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
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = MercaOrangeDark,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Tu canasta de mercado está vacía",
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = "Agrega frutas, verduras frescas y carnes de la plaza o pídele al asistente de inteligencia artificial que arme tu mercado.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.setTab(ScreenTab.PRODUCTOS) },
                    colors = ButtonDefaults.buttonColors(containerColor = MercaOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("explore_markets_empty_cart_button")
                ) {
                    Text(text = "Ver Productos de Plaza", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // Populated Basket
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header with clear cart option
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Mi Canasta de Mercado",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = "${cart.sumOf { it.quantity }} productos seleccionados",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        TextButton(
                            onClick = { viewModel.clearCart() },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFE11D48))
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(text = "Vaciar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Grouped items by market
                itemsByMarket.forEach { (marketName, items) ->
                    item {
                        MarketCartCard(
                            marketName = marketName,
                            items = items,
                            onUpdateQty = { id, delta -> viewModel.updateCart(id, delta) },
                            onRemove = { id -> viewModel.removeCartItem(id) }
                        )
                    }
                }

                // Pricing Summary Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFFED7AA)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Resumen de Compra",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = Color(0xFF1F2937)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Subtotal productos:", fontSize = 12.sp, color = Color.Gray)
                                Text(
                                    text = "$$subtotal COP",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Envío directo desde plaza:", fontSize = 12.sp, color = Color.Gray)
                                Text(
                                    text = "$$deliveryFee COP",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937)
                                )
                            }

                            if (discount > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = FarmGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(text = "Beneficio Merca Easy:", fontSize = 12.sp, color = FarmGreen, fontWeight = FontWeight.Bold)
                                    }
                                    Text(
                                        text = "-$$discount COP",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = FarmGreen
                                    )
                                }
                            }

                            HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(vertical = 4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Total a pagar:", fontSize = 14.sp, fontWeight = FontWeight.Black)
                                Text(
                                    text = "$$total COP",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MercaOrangeDark
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Fixed Checkout Action Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(14.dp),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Button(
                    onClick = { viewModel.setCheckoutOpen(true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("checkout_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MercaOrange)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Confirmar y Finalizar Pedido",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$$total COP",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MarketCartCard(
    marketName: String,
    items: List<CartItemWithProduct>,
    onUpdateQty: (String, Int) -> Unit,
    onRemove: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Market header tag
            Surface(
                color = Color(0xFFFFF7ED),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(0.5.dp, Color(0xFFFED7AA))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        tint = MercaOrangeDark,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = marketName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color(0xFF7C2D12)
                    )
                }
            }

            // Products inside this market
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = item.product.image,
                        contentDescription = item.product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF3F4F6))
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.product.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = item.product.stallName,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "$${item.product.price * item.quantity} COP",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = MercaOrangeDark
                        )
                    }

                    // Counter
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF3F4F6))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        IconButton(
                            onClick = { onUpdateQty(item.product.id, -1) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Restar",
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Text(
                            text = item.quantity.toString(),
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                        IconButton(
                            onClick = { onUpdateQty(item.product.id, 1) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Sumar",
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
