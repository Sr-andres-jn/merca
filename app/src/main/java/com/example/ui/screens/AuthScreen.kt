package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FarmGreen
import com.example.ui.theme.MercaOrange
import com.example.ui.theme.MercaOrangeDark
import com.example.ui.theme.MercaOrangeLight
import com.example.ui.viewmodel.AuthScreenMode
import com.example.ui.viewmodel.MercaViewModel

@Composable
fun AuthScreen(
    viewModel: MercaViewModel,
    modifier: Modifier = Modifier
) {
    val authMode by viewModel.authMode.collectAsState()
    val authError by viewModel.authError.collectAsState()
    val authLoading by viewModel.authLoading.collectAsState()
    val pendingEmail by viewModel.pendingResetEmail.collectAsState()
    val pendingCode by viewModel.pendingResetCode.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var agreeTerms by remember { mutableStateOf(true) }

    // Password reset fields
    var resetInputCode by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Top Brand Header Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MercaOrange, MercaOrangeDark)
                    ),
                    shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                )
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = MercaOrange,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "MERCA EASY!",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFFDE68A),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Con IA",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = when (authMode) {
                        AuthScreenMode.REGISTER -> "Crea tu Cuenta"
                        AuthScreenMode.LOGIN -> "Inicia Sesión"
                        AuthScreenMode.FORGOT_PASSWORD -> "Recuperar Contraseña"
                        AuthScreenMode.VERIFY_CODE -> "Restablecer Clave"
                        AuthScreenMode.RESET_SUCCESS -> "¡Clave Lista!"
                    },
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp
                )

                Text(
                    text = when (authMode) {
                        AuthScreenMode.REGISTER -> "Regístrate para comprar directo del campo y plazas de Madrid a tu puerta con IA."
                        AuthScreenMode.LOGIN -> "Accede a tus mercados favoritos y pedidos guardados en la base de datos."
                        AuthScreenMode.FORGOT_PASSWORD -> "Te generaremos un código de confirmación para crear tu nueva clave."
                        AuthScreenMode.VERIFY_CODE -> "Verificando cuenta: $pendingEmail"
                        AuthScreenMode.RESET_SUCCESS -> "Tu contraseña ha sido actualizada. Ya puedes iniciar sesión."
                    },
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Form Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Error banner if any
            if (authError != null) {
                Surface(
                    color = Color(0xFFFFF1F2),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECDD3)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Color(0xFFE11D48),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = authError ?: "",
                            color = Color(0xFF9F1239),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // 1. REGISTER MODE (DEFAULT FIRST SCREEN)
            if (authMode == AuthScreenMode.REGISTER) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nombre completo", fontSize = 12.sp) },
                    placeholder = { Text("Ej. Carlos Martínez") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = MercaOrange)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MercaOrange,
                        focusedLabelColor = MercaOrange
                    )
                )

                OutlinedTextField(
                    value = emailOrPhone,
                    onValueChange = { emailOrPhone = it },
                    label = { Text("Correo o número celular", fontSize = 12.sp) },
                    placeholder = { Text("ejemplo@correo.com o 310 123 4567") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = MercaOrange)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_email_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MercaOrange,
                        focusedLabelColor = MercaOrange
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña (mínimo 6 caracteres)", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = MercaOrange)
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_password_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MercaOrange,
                        focusedLabelColor = MercaOrange
                    )
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Security, contentDescription = null, tint = MercaOrange)
                    },
                    trailingIcon = {
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(
                                imageVector = if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_confirm_password_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MercaOrange,
                        focusedLabelColor = MercaOrange
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = agreeTerms,
                        onCheckedChange = { agreeTerms = it },
                        colors = CheckboxDefaults.colors(checkedColor = MercaOrange)
                    )
                    Text(
                        text = "Acepto los términos de servicio, política de privacidad y entrega de productos frescos de plaza.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        lineHeight = 14.sp
                    )
                }

                Button(
                    onClick = {
                        viewModel.register(fullName, emailOrPhone, password, confirmPassword)
                    },
                    enabled = !authLoading && agreeTerms,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("register_submit_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MercaOrange)
                ) {
                    if (authLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Registrarse y Entrar a Merca Easy",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }

                // Social registration
                SocialAuthButtons(
                    onGoogleClick = { viewModel.socialLogin("google") },
                    onAppleClick = { viewModel.socialLogin("apple") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "¿Ya tienes cuenta? ", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = "Inicia sesión aquí",
                        fontSize = 12.sp,
                        color = MercaOrangeDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { viewModel.setAuthMode(AuthScreenMode.LOGIN) }
                            .testTag("switch_to_login_button")
                    )
                }
            }

            // 2. LOGIN MODE
            if (authMode == AuthScreenMode.LOGIN) {
                OutlinedTextField(
                    value = emailOrPhone,
                    onValueChange = { emailOrPhone = it },
                    label = { Text("Correo o número celular", fontSize = 12.sp) },
                    placeholder = { Text("andres@mercaeasy.co o 3101234567") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = MercaOrange)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_email_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MercaOrange,
                        focusedLabelColor = MercaOrange
                    )
                )

                Column {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = MercaOrange)
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_password_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MercaOrange,
                            focusedLabelColor = MercaOrange
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "¿Olvidó su contraseña?",
                            color = MercaOrangeDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { viewModel.setAuthMode(AuthScreenMode.FORGOT_PASSWORD) }
                                .testTag("forgot_password_button")
                        )
                    }
                }

                Button(
                    onClick = { viewModel.login(emailOrPhone, password) },
                    enabled = !authLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("login_submit_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MercaOrange)
                ) {
                    if (authLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Iniciar Sesión",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }

                // Social Login
                SocialAuthButtons(
                    onGoogleClick = { viewModel.socialLogin("google") },
                    onAppleClick = { viewModel.socialLogin("apple") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "¿No estás registrado? ", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = "Regístrate aquí",
                        fontSize = 12.sp,
                        color = MercaOrangeDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { viewModel.setAuthMode(AuthScreenMode.REGISTER) }
                            .testTag("switch_to_register_button")
                    )
                }
            }

            // 3. FORGOT PASSWORD MODE
            if (authMode == AuthScreenMode.FORGOT_PASSWORD) {
                Surface(
                    color = Color(0xFFFFF7ED),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = MercaOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Ingresa tu correo o teléfono registrado. Generaremos un código de confirmación para restablecer tu contraseña en la base de datos.",
                            fontSize = 12.sp,
                            color = Color(0xFF7C2D12),
                            lineHeight = 16.sp
                        )
                    }
                }

                OutlinedTextField(
                    value = emailOrPhone,
                    onValueChange = { emailOrPhone = it },
                    label = { Text("Correo o teléfono registrado", fontSize = 12.sp) },
                    placeholder = { Text("andres@mercaeasy.co") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = MercaOrange)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MercaOrange,
                        focusedLabelColor = MercaOrange
                    )
                )

                Button(
                    onClick = { viewModel.requestForgotPassword(emailOrPhone) },
                    enabled = !authLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MercaOrange)
                ) {
                    Text(text = "Enviar Código de Confirmación", fontWeight = FontWeight.Bold)
                }

                TextButton(
                    onClick = { viewModel.setAuthMode(AuthScreenMode.LOGIN) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "← Volver a Iniciar Sesión", color = Color.Gray, fontSize = 12.sp)
                }
            }

            // 4. VERIFY CODE & RESET PASSWORD
            if (authMode == AuthScreenMode.VERIFY_CODE) {
                Surface(
                    color = Color(0xFFF0FDF4),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = FarmGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Código generado para: $pendingEmail",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF14532D)
                            )
                            Text(
                                text = "Usa el código: $pendingCode",
                                fontSize = 12.sp,
                                color = FarmGreen,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = resetInputCode,
                    onValueChange = { resetInputCode = it },
                    label = { Text("Código de confirmación", fontSize = 12.sp) },
                    placeholder = { Text("849216") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nueva contraseña", fontSize = 12.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    label = { Text("Confirmar nueva contraseña", fontSize = 12.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = {
                        viewModel.resetPassword(resetInputCode, newPassword, confirmNewPassword)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MercaOrange)
                ) {
                    Text(text = "Realizar Nueva Contraseña", fontWeight = FontWeight.Bold)
                }
            }

            // 5. RESET SUCCESS
            if (authMode == AuthScreenMode.RESET_SUCCESS) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = FarmGreen,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = "¡Contraseña Actualizada con Éxito!",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Tu contraseña ha sido actualizada en la base de datos SQLite/Room. Ya puedes iniciar sesión.",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Button(
                        onClick = { viewModel.setAuthMode(AuthScreenMode.LOGIN) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MercaOrange)
                    ) {
                        Text(text = "Iniciar Sesión con mi Nueva Clave", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Guest Exploration
            HorizontalDivider(color = Color(0xFFF3F4F6))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.exploreAsGuest() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Explorar catálogo de plazas como invitado →",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("explore_as_guest_button")
                )
            }
        }
    }
}

@Composable
fun SocialAuthButtons(
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
            Text(
                text = " O CONTINUAR CON ",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onGoogleClick,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("social_google_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "🌐 Google", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = onAppleClick,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("social_apple_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "🍏 Apple", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}
