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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MarketEntity
import com.example.data.model.ProductEntity
import com.example.data.model.QuickListPreset
import com.example.ui.theme.FarmGreen
import com.example.ui.theme.FarmGreenLight
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.MercaOrange
import com.example.ui.theme.MercaOrangeDark
import com.example.ui.theme.MercaOrangeLight
import com.example.ui.theme.WarmBackground
import com.example.ui.viewmodel.MercaViewModel
import com.example.ui.viewmodel.ScreenTab

@Composable
fun HomeScreen(
    viewModel: MercaViewModel,
    modifier: Modifier = Modifier
) {
    val markets by viewModel.markets.collectAsState()
    val products by viewModel.allProducts.collectAsState()
    val selectedMarketId by viewModel.selectedMarketId.collectAsState()
    val quickLists = viewModel.quickLists

    val currentMarket = markets.find { it.id == selectedMarketId }
    val displayedProducts = if (selectedMarketId == "todos") {
        products
    } else {
        products.filter { it.marketId == selectedMarketId }
    }

    Box(modifier = modifier.fillMaxSize().background(WarmBackground)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Mercados Disponibles en Madrid, Cundinamarca
            item {
                MarketsSection(
                    markets = markets,
                    selectedMarketId = selectedMarketId,
                    onSelectMarket = { viewModel.selectMarket(it) },
                    onExploreProducts = { viewModel.setTab(ScreenTab.PRODUCTOS) }
                )
            }

            // 2. Próximas Compras / Combos Frecuentes
            item {
                QuickListsSection(
                    quickLists = quickLists,
                    onAddPreset = { viewModel.addQuickList(it) },
                    onCustomWithAi = { viewModel.setTab(ScreenTab.IA) }
                )
            }

            // 3. ¿Qué es MERCA EASY?
            item {
                InfoBannerSection(
                    onPedirYa = { viewModel.setTab(ScreenTab.PRODUCTOS) },
                    onOpenAi = { viewModel.setTab(ScreenTab.IA) }
                )
            }

            // 4. Catálogo Directo de Productos Frescos del Mercado Seleccionado
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = currentMarket?.name ?: "Productos de la Plaza",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = "Cosecha fresca del día directo de los puestos",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Surface(
                        onClick = { viewModel.setTab(ScreenTab.PRODUCTOS) },
                        color = MercaOrangeLight,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Ver todos",
                            color = MercaOrangeDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Products Grid in Home (First 6 items)
            items(displayedProducts.chunked(2)) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pair.forEach { product ->
                        Box(modifier = Modifier.weight(1f)) {
                            ProductMiniCard(
                                product = product,
                                onAddToCart = { viewModel.updateCart(product.id, 1) }
                            )
                        }
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Floating Action Button "PEDIR YA" with IA Sparkles
        FloatingActionButton(
            onClick = { viewModel.setTab(ScreenTab.IA) },
            containerColor = MercaOrange,
            contentColor = Color.White,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("floating_pedir_ya_button")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFFFDE68A),
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = "PEDIR YA",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    letterSpacing = 0.5.sp
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun MarketsSection(
    markets: List<MarketEntity>,
    selectedMarketId: String,
    onSelectMarket: (String) -> Unit,
    onExploreProducts: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFFED7AA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "🏪", fontSize = 16.sp)
                    Text(
                        text = "Mercados Disponibles",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = Color(0xFF1F2937)
                    )
                }

                Surface(
                    onClick = { onSelectMarket("todos") },
                    color = if (selectedMarketId == "todos") MercaOrange else Color(0xFFFFF7ED),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Ver Todos",
                        color = if (selectedMarketId == "todos") Color.White else MercaOrangeDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            markets.forEach { market ->
                val isSelected = selectedMarketId == market.id
                val isOpen = market.status == "Abierto"

                Surface(
                    onClick = { onSelectMarket(market.id) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color(0xFFFFF7ED) else Color(0xFFFAFAFA),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) MercaOrange else Color(0xFFE5E7EB)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("market_card_${market.id}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = market.name,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = Color(0xFF111827),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MercaOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            // Status badge
                            Surface(
                                color = if (isOpen) Color(0xFFD1FAE5) else Color(0xFFFFE4E6),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(
                                    0.5.dp,
                                    if (isOpen) Color(0xFFA7F3D0) else Color(0xFFFECDD3)
                                )
                            ) {
                                Text(
                                    text = market.status,
                                    color = if (isOpen) Color(0xFF065F46) else Color(0xFF9F1239),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📍 ${market.distance}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4B5563)
                            )
                            Text(text = "•", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                text = "👥 ${market.vendorsCount} vendedores",
                                fontSize = 11.sp,
                                color = Color(0xFF4B5563)
                            )
                            Text(text = "•", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                text = "⭐ ${market.rating}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD97706)
                            )
                        }

                        if (isSelected) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = market.description,
                                fontSize = 11.sp,
                                color = Color(0xFF6B7280),
                                lineHeight = 15.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⏰ ${market.hours}",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                                TextButton(
                                    onClick = onExploreProducts,
                                    colors = ButtonDefaults.textButtonColors(contentColor = MercaOrangeDark)
                                ) {
                                    Text(text = "Ver puestos →", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickListsSection(
    quickLists: List<QuickListPreset>,
    onAddPreset: (QuickListPreset) -> Unit,
    onCustomWithAi: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEDD5)),
        border = BorderStroke(1.5.dp, Color(0xFFFDBA74)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "🛒", fontSize = 16.sp)
                    Text(
                        text = "Próximas Compras",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = Color(0xFF1F2937)
                    )
                }

                Surface(
                    color = Color(0xFFFED7AA),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Combos Frecuentes",
                        color = Color(0xFF7C2D12),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            quickLists.forEach { list ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFFED7AA)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = list.title,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = Color(0xFF111827)
                                )
                                Surface(
                                    color = Color(0xFFFFEDD5),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = list.badge,
                                        color = Color(0xFF9A3412),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = list.subtitle,
                                fontSize = 11.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Text(
                                    text = "${list.itemCount} productos",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                                Text(text = "•", fontSize = 10.sp, color = Color.Gray)
                                Text(
                                    text = "~$${list.estimatedPrice} COP",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MercaOrangeDark
                                )
                            }
                        }

                        IconButton(
                            onClick = { onAddPreset(list) },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MercaOrange)
                                .testTag("add_quick_list_${list.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Añadir combo",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Create custom list with AI
            Surface(
                onClick = onCustomWithAi,
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, MercaOrangeDark),
                modifier = Modifier.fillMaxWidth().testTag("custom_ai_list_button")
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MercaOrangeDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Crear lista personalizada con IA",
                            color = MercaOrangeDark,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MercaOrangeDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoBannerSection(
    onPedirYa: () -> Unit,
    onOpenAi: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, MercaOrange),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MercaOrangeLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "?", fontWeight = FontWeight.Black, color = MercaOrangeDark)
                }
                Text(
                    text = "¿Qué es MERCA EASY?",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = MercaOrangeDark
                )
            }

            Text(
                text = "Tu mercado tradicional llevado a tu puerta con Inteligencia Artificial. ¡Pide lo más fresco directo del campo y de la plaza a precio justo!",
                fontSize = 12.sp,
                color = Color(0xFF374151),
                lineHeight = 17.sp,
                fontWeight = FontWeight.Medium
            )

            // Features chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    color = Color(0xFFFFF7ED),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "⚡ Precios Plaza", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF9A3412))
                        Text(text = "-40% vs cadenas", fontSize = 10.sp, color = Color.Gray)
                    }
                }
                Surface(
                    color = Color(0xFFF0FDF4),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "🌿 Frescura Real", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = FarmGreen)
                        Text(text = "Cosecha del día", fontSize = 10.sp, color = Color.Gray)
                    }
                }
                Surface(
                    color = Color(0xFFFEFCE8),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "✨ Merca IA", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF854D0E))
                        Text(text = "Recetas y ahorro", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPedirYa,
                    colors = ButtonDefaults.buttonColors(containerColor = MercaOrange),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Text(text = "PEDIR YA", fontWeight = FontWeight.Black, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onOpenAi,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Text(text = "Consultar IA", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ProductMiniCard(
    product: ProductEntity,
    onAddToCart: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().testTag("product_card_${product.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
                    .background(Color(0xFFF3F4F6))
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                if (product.freshBadge != null) {
                    Surface(
                        color = FarmGreen,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Text(
                            text = product.freshBadge,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.stallName,
                    fontSize = 10.sp,
                    color = MercaOrangeDark,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF1F2937),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "$${product.price}",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = "/${product.unit}",
                            fontSize = 9.sp,
                            color = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = onAddToCart,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MercaOrange)
                            .testTag("add_product_${product.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Añadir",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
