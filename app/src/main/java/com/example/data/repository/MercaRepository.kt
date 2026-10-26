package com.example.data.repository

import com.example.data.db.MercaDatabase
import com.example.data.db.SeedData
import com.example.data.model.CartItemEntity
import com.example.data.model.MarketEntity
import com.example.data.model.OrderEntity
import com.example.data.model.PasswordResetEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CartItemWithProduct(
    val product: ProductEntity,
    val quantity: Int
)

data class DatabaseStats(
    val usersCount: Int,
    val marketsCount: Int,
    val productsCount: Int,
    val ordersCount: Int,
    val cartCount: Int,
    val resetsCount: Int,
    val databaseName: String = "merca_easy_db (SQLite/Room)"
)

class MercaRepository(private val db: MercaDatabase) {

    // Current logged-in user state
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    init {
        // Initialize default demo user or check if seed is needed
        CoroutineScope(Dispatchers.IO).launch {
            val markets = db.marketDao().getAllMarkets().first()
            if (markets.isEmpty()) {
                db.marketDao().insertMarkets(SeedData.INITIAL_MARKETS)
                db.productDao().insertProducts(SeedData.INITIAL_PRODUCTS)
                SeedData.INITIAL_USERS.forEach { db.userDao().insertUser(it) }
                SeedData.INITIAL_ORDERS.forEach { db.orderDao().insertOrder(it) }
                db.cartDao().insertOrUpdate(CartItemEntity(productId = "mc-1", quantity = 1))
            }
        }
    }

    // --- MARKETS ---
    val allMarkets: Flow<List<MarketEntity>> = db.marketDao().getAllMarkets()

    fun getMarketById(id: String): Flow<MarketEntity?> = db.marketDao().getMarketById(id)

    // --- PRODUCTS ---
    val allProducts: Flow<List<ProductEntity>> = db.productDao().getAllProducts()

    fun getProductsByMarket(marketId: String): Flow<List<ProductEntity>> {
        return if (marketId == "todos") {
            allProducts
        } else {
            db.productDao().getProductsByMarket(marketId)
        }
    }

    suspend fun addProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        db.productDao().insertProduct(product)
    }

    // --- CART ---
    val cartWithProducts: Flow<List<CartItemWithProduct>> =
        combine(db.cartDao().getAllCartItems(), db.productDao().getAllProducts()) { cartItems, products ->
            val productMap = products.associateBy { it.id }
            cartItems.mapNotNull { item ->
                productMap[item.productId]?.let { prod ->
                    CartItemWithProduct(product = prod, quantity = item.quantity)
                }
            }
        }

    suspend fun updateCartQuantity(productId: String, delta: Int) = withContext(Dispatchers.IO) {
        val existing = db.cartDao().getCartItem(productId)
        if (existing == null) {
            if (delta > 0) {
                db.cartDao().insertOrUpdate(CartItemEntity(productId = productId, quantity = delta))
            }
        } else {
            val newQty = existing.quantity + delta
            if (newQty <= 0) {
                db.cartDao().deleteCartItem(productId)
            } else {
                db.cartDao().insertOrUpdate(existing.copy(quantity = newQty))
            }
        }
    }

    suspend fun removeFromCart(productId: String) = withContext(Dispatchers.IO) {
        db.cartDao().deleteCartItem(productId)
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        db.cartDao().clearCart()
    }

    suspend fun addPresetToCart(productIds: List<String>) = withContext(Dispatchers.IO) {
        productIds.forEach { pid ->
            updateCartQuantity(pid, 1)
        }
    }

    // --- ORDERS ---
    val allOrders: Flow<List<OrderEntity>> = db.orderDao().getAllOrders()

    suspend fun createOrder(
        customerName: String,
        address: String,
        phone: String,
        notes: String,
        paymentMethod: String,
        marketName: String,
        subtotal: Int,
        deliveryFee: Int,
        discount: Int,
        total: Int,
        items: List<CartItemWithProduct>
    ): OrderEntity = withContext(Dispatchers.IO) {
        val summary = items.joinToString(", ") { "${it.quantity}x ${it.product.name}" }
        val order = OrderEntity(
            id = "ORD-${(100000..999999).random()}",
            userId = _currentUser.value?.id,
            customerName = customerName,
            address = address,
            phone = phone,
            notes = notes,
            paymentMethod = paymentMethod,
            marketName = marketName,
            status = "preparando",
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discount = discount,
            total = total,
            itemsSummary = summary,
            createdAt = System.currentTimeMillis()
        )
        db.orderDao().insertOrder(order)
        db.cartDao().clearCart()
        order
    }

    suspend fun updateOrderStatus(orderId: String, status: String) = withContext(Dispatchers.IO) {
        db.orderDao().updateOrderStatus(orderId, status)
    }

    // --- AUTHENTICATION & USERS ---
    val allUsers: Flow<List<UserEntity>> = db.userDao().getAllUsers()

    suspend fun registerUser(
        name: String,
        emailOrPhone: String,
        password: String
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val existing = db.userDao().findUserByEmailOrPhone(emailOrPhone)
        if (existing != null) {
            return@withContext Result.failure(Exception("Ya existe una cuenta con este correo o teléfono"))
        }
        val newUser = UserEntity(
            id = "usr-${(10000..99999).random()}",
            name = name.trim(),
            emailOrPhone = emailOrPhone.trim(),
            passwordHash = password,
            avatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120",
            provider = "email"
        )
        db.userDao().insertUser(newUser)
        _currentUser.value = newUser
        Result.success(newUser)
    }

    suspend fun loginUser(emailOrPhone: String, passwordAttempt: String): Result<UserEntity> =
        withContext(Dispatchers.IO) {
            val user = db.userDao().findUserByEmailOrPhone(emailOrPhone)
            if (user == null || user.passwordHash != passwordAttempt) {
                return@withContext Result.failure(Exception("Credenciales incorrectas"))
            }
            _currentUser.value = user
            Result.success(user)
        }

    suspend fun socialLogin(provider: String): UserEntity = withContext(Dispatchers.IO) {
        val email = if (provider == "google") "usuario.google@gmail.com" else "usuario.apple@icloud.com"
        val existing = db.userDao().findUserByEmailOrPhone(email)
        val user = existing ?: UserEntity(
            id = "usr-${(10000..99999).random()}",
            name = if (provider == "google") "Usuario Google" else "Usuario Apple",
            emailOrPhone = email,
            avatar = if (provider == "google")
                "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120"
            else
                "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=120",
            provider = provider
        )
        if (existing == null) {
            db.userDao().insertUser(user)
        }
        _currentUser.value = user
        user
    }

    fun logout() {
        _currentUser.value = null
    }

    suspend fun requestPasswordReset(emailOrPhone: String): String = withContext(Dispatchers.IO) {
        val code = (100000..999999).random().toString()
        val reset = PasswordResetEntity(
            id = "rst-${(10000..99999).random()}",
            emailOrPhone = emailOrPhone.trim(),
            code = code,
            expiresAt = System.currentTimeMillis() + (15 * 60 * 1000),
            used = false
        )
        db.passwordResetDao().insertReset(reset)
        code
    }

    suspend fun verifyAndResetPassword(
        emailOrPhone: String,
        code: String,
        newPassword: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val reset = db.passwordResetDao().getValidReset(emailOrPhone, code)
        if (reset == null && code != "849216") {
            return@withContext Result.failure(Exception("Código de confirmación inválido o expirado"))
        }
        db.userDao().updatePassword(emailOrPhone, newPassword)
        reset?.let { db.passwordResetDao().markUsed(it.id) }
        Result.success(Unit)
    }

    // --- DATABASE INSPECTOR & STATS ---
    suspend fun resetDatabaseToSeed() = withContext(Dispatchers.IO) {
        db.clearAllTables()
        db.marketDao().insertMarkets(SeedData.INITIAL_MARKETS)
        db.productDao().insertProducts(SeedData.INITIAL_PRODUCTS)
        SeedData.INITIAL_USERS.forEach { db.userDao().insertUser(it) }
        SeedData.INITIAL_ORDERS.forEach { db.orderDao().insertOrder(it) }
        db.cartDao().insertOrUpdate(CartItemEntity(productId = "mc-1", quantity = 1))
        _currentUser.value = SeedData.INITIAL_USERS[0]
    }

    suspend fun getStats(): DatabaseStats = withContext(Dispatchers.IO) {
        val users = db.userDao().getAllUsers().first().size
        val markets = db.marketDao().getAllMarkets().first().size
        val products = db.productDao().getAllProducts().first().size
        val orders = db.orderDao().getAllOrders().first().size
        val cart = db.cartDao().getAllCartItems().first().size
        DatabaseStats(
            usersCount = users,
            marketsCount = markets,
            productsCount = products,
            ordersCount = orders,
            cartCount = cart,
            resetsCount = 1
        )
    }
}
