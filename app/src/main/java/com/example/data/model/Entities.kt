package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val emailOrPhone: String,
    val passwordHash: String? = null,
    val avatar: String? = null,
    val provider: String = "email", // "email", "google", "apple"
    val defaultAddress: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "markets")
data class MarketEntity(
    @PrimaryKey val id: String,
    val name: String,
    val status: String, // "Abierto", "Cerrado"
    val distance: String,
    val vendorsCount: Int,
    val description: String,
    val hours: String,
    val rating: Float,
    val reviewsCount: Int,
    val deliveryTime: String,
    val accentColor: String,
    val badgeText: String
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // "frutas", "verduras", "carnes", "pescados", "campesino", "lacteos", "granos", "hierbas"
    val price: Int, // in COP
    val unit: String,
    val image: String,
    val stallName: String,
    val stallNumber: String,
    val marketId: String,
    val marketName: String,
    val freshBadge: String? = null,
    val description: String,
    val inStock: Boolean = true,
    val organic: Boolean = false
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: String,
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val userId: String? = null,
    val customerName: String,
    val address: String,
    val phone: String,
    val notes: String = "",
    val paymentMethod: String,
    val marketName: String,
    val status: String = "preparando", // "preparando", "en_camino", "entregado"
    val subtotal: Int,
    val deliveryFee: Int = 4500,
    val discount: Int = 0,
    val total: Int,
    val itemsSummary: String, // Human readable item list or JSON string
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "password_resets")
data class PasswordResetEntity(
    @PrimaryKey val id: String,
    val emailOrPhone: String,
    val code: String,
    val expiresAt: Long,
    val used: Boolean = false
)

data class QuickListPreset(
    val id: String,
    val title: String,
    val subtitle: String,
    val itemCount: Int,
    val estimatedPrice: Int,
    val badge: String,
    val presetProductIds: List<String>
)
