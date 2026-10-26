package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.FarmGreen
import com.example.ui.theme.MercaOrange
import com.example.ui.theme.MercaOrangeDark
import com.example.ui.viewmodel.MercaViewModel

@Composable
fun CheckoutDialog(
    viewModel: MercaViewModel,
    onDismiss: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val cart by viewModel.cart.collectAsState()

    var customerName by remember {
        mutableStateOf(currentUser?.name ?: "Alejandro Rodríguez")
    }
    var address by remember {
        mutableStateOf(currentUser?.defaultAddress ?: "Calle 7 # 5-40, Barrio San Carlos, Madrid, Cundinamarca")
    }
    var phone by remember {
        mutableStateOf(
            if (currentUser != null && !currentUser!!.emailOrPhone.contains("@")) currentUser!!.emailOrPhone
            else "310 456 7890"
        )
    }
    var notes by remember {
        mutableStateOf("Por favor aguacates que estén listos para hoy y tomates pintones.")
    }
    var paymentMethod by remember { mutableStateOf("Nequi / QR") }

    val subtotal = cart.sumOf { it.product.price * it.quantity }
    val deliveryFee = 4500
    val discount = if (subtotal >= 30000) 3000 else 0
    val total = subtotal + deliveryFee - discount

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(20.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = MercaOrange,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "Finalizar Pedido de Plaza",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color(0xFF1F2937)
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                // Delivery information
                Text(
                    text = "DATOS DE ENTREGA EN MADRID",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 0.5.sp
                )

                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Nombre completo", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("checkout_name_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección exacta", fontSize = 11.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = MercaOrange, modifier = Modifier.size(16.dp))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("checkout_address_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono de contacto", fontSize = 11.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = MercaOrange, modifier = Modifier.size(16.dp))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("checkout_phone_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas para el puestero / pesador", fontSize = 11.sp) },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // Payment Method
                Text(
                    text = "MÉTODO DE PAGO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 0.5.sp
                )

                val methods = listOf(
                    "Nequi / QR" to "📱 Nequi al instante",
                    "Daviplata" to "🏦 Daviplata",
                    "Efectivo contra entrega" to "💵 Efectivo contra entrega",
                    "Tarjeta Débito/Crédito" to "💳 Tarjeta con datáfono"
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    methods.forEach { (id, label) ->
                        val isSelected = paymentMethod == id
                        Surface(
                            onClick = { paymentMethod = id },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(0xFFFFF7ED) else Color(0xFFF9FAFB),
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) MercaOrange else Color(0xFFE5E7EB)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { paymentMethod = id },
                                    colors = RadioButtonDefaults.colors(selectedColor = MercaOrange)
                                )
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFF9A3412) else Color(0xFF374151)
                                )
                            }
                        }
                    }
                }

                // Summary
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFFED7AA))
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Productos (${cart.sumOf { it.quantity }}):", fontSize = 11.sp, color = Color.Gray)
                            Text(text = "$$subtotal COP", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Envío motorizado local:", fontSize = 11.sp, color = Color.Gray)
                            Text(text = "$$deliveryFee COP", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        if (discount > 0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Beneficio Merca Easy:", fontSize = 11.sp, color = FarmGreen, fontWeight = FontWeight.Bold)
                                Text(text = "-$$discount COP", fontSize = 11.sp, fontWeight = FontWeight.Black, color = FarmGreen)
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Total a pagar:", fontWeight = FontWeight.Black, fontSize = 13.sp)
                            Text(text = "$$total COP", fontWeight = FontWeight.Black, fontSize = 16.sp, color = MercaOrangeDark)
                        }
                    }
                }

                Button(
                    onClick = {
                        viewModel.confirmOrder(
                            customerName = customerName,
                            address = address,
                            phone = phone,
                            notes = notes,
                            paymentMethod = paymentMethod
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_order_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MercaOrange)
                ) {
                    Text(text = "Confirmar Orden ($$total COP)", fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
            }
        }
    }
}
