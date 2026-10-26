package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CartItemEntity
import com.example.data.model.MarketEntity
import com.example.data.model.OrderEntity
import com.example.data.model.PasswordResetEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE LOWER(TRIM(emailOrPhone)) = LOWER(TRIM(:query)) LIMIT 1")
    suspend fun findUserByEmailOrPhone(query: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("UPDATE users SET passwordHash = :passwordHash WHERE LOWER(TRIM(emailOrPhone)) = LOWER(TRIM(:emailOrPhone))")
    suspend fun updatePassword(emailOrPhone: String, passwordHash: String): Int

    @Query("DELETE FROM users")
    suspend fun clearUsers()
}

@Dao
interface MarketDao {
    @Query("SELECT * FROM markets")
    fun getAllMarkets(): Flow<List<MarketEntity>>

    @Query("SELECT * FROM markets WHERE id = :id")
    fun getMarketById(id: String): Flow<MarketEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkets(markets: List<MarketEntity>)
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE marketId = :marketId")
    fun getProductsByMarket(marketId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: String): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductByIdSync(id: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProduct(id: String)
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY addedAt ASC")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getCartItem(productId: String): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItem(productId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getOrdersByUser(userId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    fun getOrderById(orderId: String): Flow<OrderEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    @Query("DELETE FROM orders")
    suspend fun clearOrders()
}

@Dao
interface PasswordResetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReset(reset: PasswordResetEntity)

    @Query("SELECT * FROM password_resets WHERE LOWER(TRIM(emailOrPhone)) = LOWER(TRIM(:emailOrPhone)) AND code = :code AND used = 0 LIMIT 1")
    suspend fun getValidReset(emailOrPhone: String, code: String): PasswordResetEntity?

    @Query("UPDATE password_resets SET used = 1 WHERE id = :id")
    suspend fun markUsed(id: String)
}
