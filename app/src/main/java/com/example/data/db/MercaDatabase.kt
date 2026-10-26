package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CartItemEntity
import com.example.data.model.MarketEntity
import com.example.data.model.OrderEntity
import com.example.data.model.PasswordResetEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        MarketEntity::class,
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        PasswordResetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MercaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun marketDao(): MarketDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun passwordResetDao(): PasswordResetDao

    companion object {
        @Volatile
        private var INSTANCE: MercaDatabase? = null

        fun getDatabase(context: Context): MercaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MercaDatabase::class.java,
                    "merca_easy_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate initial seed on database creation
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    database.marketDao().insertMarkets(SeedData.INITIAL_MARKETS)
                                    database.productDao().insertProducts(SeedData.INITIAL_PRODUCTS)
                                    SeedData.INITIAL_USERS.forEach { user ->
                                        database.userDao().insertUser(user)
                                    }
                                    SeedData.INITIAL_ORDERS.forEach { order ->
                                        database.orderDao().insertOrder(order)
                                    }
                                    // Seed demo cart item
                                    database.cartDao().insertOrUpdate(
                                        CartItemEntity(productId = "mc-1", quantity = 1)
                                    )
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
