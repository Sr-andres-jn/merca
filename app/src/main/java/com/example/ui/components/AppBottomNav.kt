package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MercaOrange
import com.example.ui.theme.MercaOrangeLight
import com.example.ui.viewmodel.ScreenTab

@Composable
fun AppBottomNav(
    activeTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit,
    cartCount: Int,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        // Tab 1: Inicio
        NavigationBarItem(
            selected = activeTab == ScreenTab.INICIO,
            onClick = { onTabSelected(ScreenTab.INICIO) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = "Mercados"
                )
            },
            label = {
                Text(
                    text = "Mercados",
                    fontSize = 11.sp,
                    fontWeight = if (activeTab == ScreenTab.INICIO) FontWeight.ExtraBold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MercaOrange,
                selectedTextColor = MercaOrange,
                indicatorColor = MercaOrangeLight
            ),
            modifier = Modifier.testTag("nav_tab_inicio")
        )

        // Tab 2: Productos
        NavigationBarItem(
            selected = activeTab == ScreenTab.PRODUCTOS,
            onClick = { onTabSelected(ScreenTab.PRODUCTOS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Catálogo"
                )
            },
            label = {
                Text(
                    text = "Catálogo",
                    fontSize = 11.sp,
                    fontWeight = if (activeTab == ScreenTab.PRODUCTOS) FontWeight.ExtraBold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MercaOrange,
                selectedTextColor = MercaOrange,
                indicatorColor = MercaOrangeLight
            ),
            modifier = Modifier.testTag("nav_tab_productos")
        )

        // Tab 3: IA
        NavigationBarItem(
            selected = activeTab == ScreenTab.IA,
            onClick = { onTabSelected(ScreenTab.IA) },
            icon = {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Merca IA"
                )
            },
            label = {
                Text(
                    text = "Merca IA",
                    fontSize = 11.sp,
                    fontWeight = if (activeTab == ScreenTab.IA) FontWeight.ExtraBold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MercaOrange,
                selectedTextColor = MercaOrange,
                indicatorColor = MercaOrangeLight
            ),
            modifier = Modifier.testTag("nav_tab_ia")
        )

        // Tab 4: Canasta
        NavigationBarItem(
            selected = activeTab == ScreenTab.CANASTA,
            onClick = { onTabSelected(ScreenTab.CANASTA) },
            icon = {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(
                                containerColor = MercaOrange,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = cartCount.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Canasta"
                    )
                }
            },
            label = {
                Text(
                    text = "Canasta",
                    fontSize = 11.sp,
                    fontWeight = if (activeTab == ScreenTab.CANASTA) FontWeight.ExtraBold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MercaOrange,
                selectedTextColor = MercaOrange,
                indicatorColor = MercaOrangeLight
            ),
            modifier = Modifier.testTag("nav_tab_canasta")
        )

        // Tab 5: Pedidos
        NavigationBarItem(
            selected = activeTab == ScreenTab.PEDIDOS,
            onClick = { onTabSelected(ScreenTab.PEDIDOS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Pedidos"
                )
            },
            label = {
                Text(
                    text = "Pedidos",
                    fontSize = 11.sp,
                    fontWeight = if (activeTab == ScreenTab.PEDIDOS) FontWeight.ExtraBold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MercaOrange,
                selectedTextColor = MercaOrange,
                indicatorColor = MercaOrangeLight
            ),
            modifier = Modifier.testTag("nav_tab_pedidos")
        )
    }
}
