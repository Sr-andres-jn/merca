package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.db.SeedData
import com.example.data.model.MarketEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.QuickListPreset
import com.example.data.model.UserEntity
import com.example.data.repository.CartItemWithProduct
import com.example.data.repository.DatabaseStats
import com.example.data.repository.MercaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val suggestedProductIds: List<String> = emptyList(),
    val timestamp: String = "Ahora"
)

enum class ScreenTab {
    INICIO,
    PRODUCTOS,
    IA,
    CANASTA,
    PEDIDOS
}

enum class AuthScreenMode {
    REGISTER,
    LOGIN,
    FORGOT_PASSWORD,
    VERIFY_CODE,
    RESET_SUCCESS
}

class MercaViewModel(private val repository: MercaRepository) : ViewModel() {

    val currentUser: StateFlow<UserEntity?> = repository.currentUser

    private val _isGuest = MutableStateFlow(false)
    val isGuest: StateFlow<Boolean> = _isGuest.asStateFlow()

    private val _activeTab = MutableStateFlow(ScreenTab.INICIO)
    val activeTab: StateFlow<ScreenTab> = _activeTab.asStateFlow()

    private val _selectedMarketId = MutableStateFlow("mercado-central")
    val selectedMarketId: StateFlow<String> = _selectedMarketId.asStateFlow()

    private val _selectedCity = MutableStateFlow("Madrid, Cundinamarca (Centro / La Prosperidad)")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("todos")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Modals
    private val _isCheckoutOpen = MutableStateFlow(false)
    val isCheckoutOpen: StateFlow<Boolean> = _isCheckoutOpen.asStateFlow()

    private val _isOrderSuccessOpen = MutableStateFlow(false)
    val isOrderSuccessOpen: StateFlow<Boolean> = _isOrderSuccessOpen.asStateFlow()

    private val _isDatabaseModalOpen = MutableStateFlow(false)
    val isDatabaseModalOpen: StateFlow<Boolean> = _isDatabaseModalOpen.asStateFlow()

    private val _isSideMenuOpen = MutableStateFlow(false)
    val isSideMenuOpen: StateFlow<Boolean> = _isSideMenuOpen.asStateFlow()

    private val _latestOrder = MutableStateFlow<OrderEntity?>(null)
    val latestOrder: StateFlow<OrderEntity?> = _latestOrder.asStateFlow()

    // Auth state inside AuthScreen
    private val _authMode = MutableStateFlow(AuthScreenMode.REGISTER)
    val authMode: StateFlow<AuthScreenMode> = _authMode.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _authLoading = MutableStateFlow(false)
    val authLoading: StateFlow<Boolean> = _authLoading.asStateFlow()

    private val _pendingResetEmail = MutableStateFlow("")
    val pendingResetEmail: StateFlow<String> = _pendingResetEmail.asStateFlow()

    private val _pendingResetCode = MutableStateFlow("849216")
    val pendingResetCode: StateFlow<String> = _pendingResetCode.asStateFlow()

    // DB stats for inspector
    private val _dbStats = MutableStateFlow<DatabaseStats?>(null)
    val dbStats: StateFlow<DatabaseStats?> = _dbStats.asStateFlow()

    // Repository streams
    val markets: StateFlow<List<MarketEntity>> = repository.allMarkets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cart: StateFlow<List<CartItemWithProduct>> = repository.cartWithProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val users: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quickLists: List<QuickListPreset> = SeedData.QUICK_LISTS

    // AI Assistant Chat Messages
    private val _aiMessages = MutableStateFlow(
        listOf(
            ChatMessage(
                id = "ai-1",
                isUser = false,
                text = "¡Hola vecino de Madrid! 🥕 Soy el Asistente IA de Merca Easy. Te ayudo a encontrar lo más fresco de la plaza, armar recetas tradicionales de la sabana, comparar precios en COP o ajustar tu presupuesto. ¿Qué deseas comprar o cocinar hoy?"
            )
        )
    )
    val aiMessages: StateFlow<List<ChatMessage>> = _aiMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Actions
    fun setTab(tab: ScreenTab) {
        _activeTab.value = tab
    }

    fun selectMarket(marketId: String) {
        _selectedMarketId.value = marketId
    }

    fun selectCity(city: String) {
        _selectedCity.value = city
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSideMenuOpen(open: Boolean) {
        _isSideMenuOpen.value = open
    }

    fun setCheckoutOpen(open: Boolean) {
        _isCheckoutOpen.value = open
    }

    fun setOrderSuccessOpen(open: Boolean) {
        _isOrderSuccessOpen.value = open
    }

    fun setDatabaseModalOpen(open: Boolean) {
        _isDatabaseModalOpen.value = open
        if (open) {
            refreshDbStats()
        }
    }

    fun refreshDbStats() {
        viewModelScope.launch {
            _dbStats.value = repository.getStats()
        }
    }

    fun exploreAsGuest() {
        _isGuest.value = true
    }

    // --- CART ACTIONS ---
    fun updateCart(productId: String, delta: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(productId, delta)
        }
    }

    fun removeCartItem(productId: String) {
        viewModelScope.launch {
            repository.removeFromCart(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun addQuickList(preset: QuickListPreset) {
        viewModelScope.launch {
            repository.addPresetToCart(preset.presetProductIds)
            _activeTab.value = ScreenTab.CANASTA
        }
    }

    // --- ORDER ACTIONS ---
    fun confirmOrder(
        customerName: String,
        address: String,
        phone: String,
        notes: String,
        paymentMethod: String
    ) {
        viewModelScope.launch {
            val currentCart = cart.value
            if (currentCart.isEmpty()) return@launch
            val subtotal = currentCart.sumOf { it.product.price * it.quantity }
            val deliveryFee = 4500
            val discount = if (subtotal >= 30000) 3000 else 0
            val total = subtotal + deliveryFee - discount
            val marketName = currentCart.firstOrNull()?.product?.marketName ?: "Plaza Central de Madrid"

            val order = repository.createOrder(
                customerName = customerName,
                address = address,
                phone = phone,
                notes = notes,
                paymentMethod = paymentMethod,
                marketName = marketName,
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                discount = discount,
                total = total,
                items = currentCart
            )
            _latestOrder.value = order
            _isCheckoutOpen.value = false
            _isOrderSuccessOpen.value = true
            _activeTab.value = ScreenTab.PEDIDOS
            refreshDbStats()
        }
    }

    // --- AUTH ACTIONS ---
    fun setAuthMode(mode: AuthScreenMode) {
        _authMode.value = mode
        _authError.value = null
    }

    fun register(name: String, emailOrPhone: String, password: String, confirmPassword: String) {
        if (name.isBlank() || emailOrPhone.isBlank() || password.isBlank()) {
            _authError.value = "Todos los campos son obligatorios"
            return
        }
        if (password.length < 6) {
            _authError.value = "La contraseña debe tener mínimo 6 caracteres"
            return
        }
        if (password != confirmPassword) {
            _authError.value = "Las contraseñas no coinciden"
            return
        }

        viewModelScope.launch {
            _authLoading.value = true
            _authError.value = null
            delay(400) // realistic UX feedback
            val result = repository.registerUser(name, emailOrPhone, password)
            _authLoading.value = false
            result.onSuccess {
                _isGuest.value = false
                refreshDbStats()
            }.onFailure {
                _authError.value = it.message ?: "Error al registrar usuario en la base de datos"
            }
        }
    }

    fun login(emailOrPhone: String, password: String) {
        if (emailOrPhone.isBlank() || password.isBlank()) {
            _authError.value = "Ingresa tu correo/teléfono y contraseña"
            return
        }
        viewModelScope.launch {
            _authLoading.value = true
            _authError.value = null
            delay(400)
            val result = repository.loginUser(emailOrPhone, password)
            _authLoading.value = false
            result.onSuccess {
                _isGuest.value = false
            }.onFailure {
                _authError.value = it.message ?: "Credenciales inválidas"
            }
        }
    }

    fun socialLogin(provider: String) {
        viewModelScope.launch {
            _authLoading.value = true
            delay(300)
            repository.socialLogin(provider)
            _authLoading.value = false
            _isGuest.value = false
            refreshDbStats()
        }
    }

    fun requestForgotPassword(emailOrPhone: String) {
        if (emailOrPhone.isBlank()) {
            _authError.value = "Ingresa tu correo o teléfono registrado"
            return
        }
        viewModelScope.launch {
            _authLoading.value = true
            _authError.value = null
            val code = repository.requestPasswordReset(emailOrPhone)
            _pendingResetEmail.value = emailOrPhone
            _pendingResetCode.value = code
            _authLoading.value = false
            _authMode.value = AuthScreenMode.VERIFY_CODE
            refreshDbStats()
        }
    }

    fun resetPassword(code: String, newPassword: String, confirmPassword: String) {
        if (code.isBlank() || newPassword.isBlank()) {
            _authError.value = "Ingresa el código y tu nueva contraseña"
            return
        }
        if (newPassword.length < 6) {
            _authError.value = "Mínimo 6 caracteres"
            return
        }
        if (newPassword != confirmPassword) {
            _authError.value = "Las contraseñas no coinciden"
            return
        }
        viewModelScope.launch {
            _authLoading.value = true
            val result = repository.verifyAndResetPassword(_pendingResetEmail.value, code, newPassword)
            _authLoading.value = false
            result.onSuccess {
                _authMode.value = AuthScreenMode.RESET_SUCCESS
            }.onFailure {
                _authError.value = it.message ?: "Código inválido"
            }
        }
    }

    fun logout() {
        repository.logout()
        _authMode.value = AuthScreenMode.LOGIN
    }

    // --- AI ASSISTANT ---
    fun sendAiMessage(prompt: String) {
        if (prompt.isBlank() || _isAiLoading.value) return
        val userMsg = ChatMessage(
            id = "usr-${System.currentTimeMillis()}",
            isUser = true,
            text = prompt
        )
        _aiMessages.value = _aiMessages.value + userMsg

        viewModelScope.launch {
            _isAiLoading.value = true
            delay(700)
            val lower = prompt.lowercase()
            val (reply, suggestedIds) = when {
                lower.contains("ajiaco") || lower.contains("sopa") -> {
                    Pair(
                        "¡Delicioso ajiaco sabanero, vecino! Para que quede en su punto necesitas papa criolla arenosa de vereda Chueca, pechuga campesina fresca y cilantro de Doña Rosa. He seleccionado los ingredientes directamente de la plaza:",
                        listOf("fc-4", "mc-5", "mc-4", "mc-1")
                    )
                }
                lower.contains("fruta") || lower.contains("jugo") || lower.contains("desayuno") -> {
                    Pair(
                        "Para un desayuno lleno de energía campesina, te recomiendo mango tommy dulce de Doña María, fresas sabaneras de Puente de Piedra y huevos de pastoreo de Granja El Corzo:",
                        listOf("mc-3", "fc-1", "fc-2", "mc-6")
                    )
                }
                lower.contains("carne") || lower.contains("proteina") || lower.contains("pescado") -> {
                    Pair(
                        "Las mejores proteínas frescas de hoy están en la Plaza Central y San José: pechuga campesina limpia, filete de trucha fresca o lomo fino con maduración artesanal:",
                        listOf("mc-5", "pn-1", "pn-2")
                    )
                }
                else -> {
                    Pair(
                        "Te seleccioné los productos más frescos y económicos de hoy en la Plaza Central de Madrid y la Feria Campesina, ideales para tu mercado semanal:",
                        listOf("mc-1", "mc-2", "fc-2", "fc-1")
                    )
                }
            }

            val assistantMsg = ChatMessage(
                id = "ai-${System.currentTimeMillis()}",
                isUser = false,
                text = reply,
                suggestedProductIds = suggestedIds
            )
            _aiMessages.value = _aiMessages.value + assistantMsg
            _isAiLoading.value = false
        }
    }

    fun resetDatabase() {
        viewModelScope.launch {
            repository.resetDatabaseToSeed()
            refreshDbStats()
        }
    }
}

class MercaViewModelFactory(private val repository: MercaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MercaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MercaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
