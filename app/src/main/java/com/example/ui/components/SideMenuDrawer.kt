package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.FarmGreen
import com.example.ui.theme.MercaOrange
import com.example.ui.theme.MercaOrangeDark
import com.example.ui.theme.MercaOrangeLight
import com.example.ui.viewmodel.AuthScreenMode
import com.example.ui.viewmodel.MercaViewModel
import com.example.ui.viewmodel.ScreenTab

@Composable
fun SideMenuDrawer(
    viewModel: MercaViewModel,
    onClose: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    val cities = listOf(
        "Madrid, Cundinamarca (Centro / La Prosperidad)",
        "Madrid - Hacienda Los Alcaparros",
        "Madrid - Barrio San José / Loreto",
        "Madrid - Sosiego / El Cortijo",
        "Madrid - Vereda Puente de Piedra",
        "Madrid - Vereda Chueca / El Corzo"
    )

    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier.width(310.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Surface(
                color = MercaOrange,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
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
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = MercaOrange,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "MERCA EASY!",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }

                    IconButton(onClick = onClose, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                    }
                }
            }

            // User Profile Section
            Surface(
                color = Color(0xFFFFF7ED),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    if (currentUser != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AsyncImage(
                                model = currentUser!!.avatar ?: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120",
                                contentDescription = currentUser!!.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(MercaOrangeLight)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentUser!!.name,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = Color(0xFF1F2937)
                                )
                                Text(
                                    text = currentUser!!.emailOrPhone,
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Text(
                                        text = "Cuenta Guardada en BD",
                                        color = FarmGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = {
                                viewModel.logout()
                                onClose()
                            },
                            modifier = Modifier.fillMaxWidth().height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE11D48)),
                            border = BorderStroke(1.dp, Color(0xFFFECDD3))
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Cerrar Sesión", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Bienvenido a Merca Easy",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = "Inicia sesión o regístrate para sincronizar compras",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        viewModel.setAuthMode(AuthScreenMode.LOGIN)
                                        onClose()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MercaOrange),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).height(36.dp)
                                ) {
                                    Text(text = "Ingresar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = {
                                        viewModel.setAuthMode(AuthScreenMode.REGISTER)
                                        onClose()
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).height(36.dp)
                                ) {
                                    Text(text = "Registro", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Delivery Zone Selector
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "ZONA DE ENTREGA EN MADRID",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                cities.forEach { city ->
                    val isSelected = selectedCity == city
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectCity(city) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = if (isSelected) MercaOrange else Color.LightGray,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = city,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MercaOrangeDark else Color(0xFF374151)
                        )
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFF3F4F6))

            // Navigation Menu Options
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                DrawerMenuItem(
                    icon = Icons.Default.Store,
                    title = "Explorar Plazas de Mercado",
                    onClick = {
                        viewModel.selectMarket("todos")
                        viewModel.setTab(ScreenTab.PRODUCTOS)
                        onClose()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.AutoAwesome,
                    title = "Asistente de Compras con IA",
                    tint = MercaOrangeDark,
                    onClick = {
                        viewModel.setTab(ScreenTab.IA)
                        onClose()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.ShoppingBag,
                    title = "Feria Campesina Directa",
                    tint = FarmGreen,
                    onClick = {
                        viewModel.selectMarket("feria-campesina")
                        viewModel.setTab(ScreenTab.PRODUCTOS)
                        onClose()
                    }
                )

                // Database Inspector trigger
                Surface(
                    onClick = {
                        onClose()
                        viewModel.setDatabaseModalOpen(true)
                    },
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF9FAFB),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.fillMaxWidth().testTag("drawer_database_button")
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = null,
                                tint = MercaOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Base de Datos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF1F2937)
                            )
                        }
                        Surface(
                            color = FarmGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Persistente",
                                color = FarmGreen,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                DrawerMenuItem(
                    icon = Icons.Default.Security,
                    title = "Garantía de Frescura & Pesaje",
                    onClick = onClose
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9FAFB))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "MERCA EASY! Android v1.2",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "Plaza & Campo con SQLite/Room e IA",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    tint: Color = Color(0xFF4B5563),
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F2937)
        )
    }
}
