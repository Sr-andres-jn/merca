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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ProductEntity
import com.example.ui.theme.FarmGreen
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.MercaOrange
import com.example.ui.theme.MercaOrangeDark
import com.example.ui.theme.MercaOrangeLight
import com.example.ui.theme.WarmBackground
import com.example.ui.viewmodel.MercaViewModel

@Composable
fun ProductsScreen(
    viewModel: MercaViewModel,
    modifier: Modifier = Modifier
) {
    val markets by viewModel.markets.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val selectedMarketId by viewModel.selectedMarketId.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val cart by viewModel.cart.collectAsState()

    val categories = listOf(
        "todos" to "Todos",
        "frutas" to "Frutas",
        "verduras" to "Verduras",
        "carnes" to "Carnes",
        "pescados" to "Pescados",
        "campesino" to "Campesino",
        "lacteos" to "Lácteos",
        "granos" to "Granos",
        "hierbas" to "Hierbas"
    )

    // Filter logic
    val filteredProducts = allProducts.filter { product ->
        val matchesMarket = selectedMarketId == "todos" || product.marketId == selectedMarketId
        val matchesCategory = selectedCategory == "todos" || product.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                product.name.contains(searchQuery, ignoreCase = true) ||
                product.description.contains(searchQuery, ignoreCase = true) ||
                product.stallName.contains(searchQuery, ignoreCase = true)
        matchesMarket && matchesCategory && matchesSearch
    }

    val cartQuantityMap = cart.associate { it.product.id to it.quantity }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
    ) {
        // Search & Filter header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Buscar aguacate, papa, carne, puesto...", fontSize = 12.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar", modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("products_search_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MercaOrange,
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                )
            )

            // Market filter chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    val isSelected = selectedMarketId == "todos"
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectMarket("todos") },
                        label = { Text("Todas las Plazas", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MercaOrange,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(markets) { market ->
                    val isSelected = selectedMarketId == market.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectMarket(market.id) },
                        label = {
                            Text(
                                text = market.name.replace("Mercado ", "").replace("Plaza ", ""),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MercaOrange,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Category filter chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { (id, label) ->
                    val isSelected = selectedCategory == id
                    Surface(
                        onClick = { viewModel.setCategory(id) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) MercaOrangeDark else Color(0xFFF3F4F6),
                        modifier = Modifier.clickable { viewModel.setCategory(id) }
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else Color(0xFF374151),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // Product Catalog Results
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "🧺", fontSize = 36.sp)
                    Text(
                        text = "No se encontraron productos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF374151)
                    )
                    Text(
                        text = "Prueba buscando otra palabra o selecciona otra plaza.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "${filteredProducts.size} productos frescos disponibles",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                items(filteredProducts) { product ->
                    val qtyInCart = cartQuantityMap[product.id] ?: 0
                    ProductDetailCard(
                        product = product,
                        quantityInCart = qtyInCart,
                        onAddToCart = { viewModel.updateCart(product.id, 1) },
                        onMinusCart = { viewModel.updateCart(product.id, -1) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetailCard(
    product: ProductEntity,
    quantityInCart: Int,
    onAddToCart: () -> Unit,
    onMinusCart: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().testTag("product_row_${product.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image with fresh tag
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
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
                        shape = RoundedCornerShape(bottomEnd = 6.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = product.freshBadge,
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Info & Price
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.stallName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MercaOrangeDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = product.stallNumber,
                        fontSize = 9.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = product.name,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = Color(0xFF111827),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.description,
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$${product.price}",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = " / ${product.unit}",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }

                    // Cart Buttons
                    if (quantityInCart > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFF7ED))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            IconButton(
                                onClick = onMinusCart,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Restar",
                                    tint = MercaOrangeDark,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = quantityInCart.toString(),
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = MercaOrangeDark
                            )
                            IconButton(
                                onClick = onAddToCart,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Sumar",
                                    tint = MercaOrangeDark,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    } else {
                        Surface(
                            onClick = onAddToCart,
                            shape = RoundedCornerShape(8.dp),
                            color = MercaOrange,
                            modifier = Modifier.testTag("add_catalog_${product.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Añadir",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
