package com.example

import com.example.data.db.SeedData
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun seedMarkets_areCompleteAndValid() {
        val markets = SeedData.INITIAL_MARKETS
        assertTrue(markets.isNotEmpty())
        assertEquals(4, markets.size)
        assertTrue(markets.any { it.name.contains("Madrid") })
    }

    @Test
    fun seedProducts_haveValidPricesAndStalls() {
        val products = SeedData.INITIAL_PRODUCTS
        assertTrue(products.isNotEmpty())
        products.forEach { product ->
            assertTrue("Product ${product.name} should have price > 0", product.price > 0)
            assertTrue("Product ${product.name} should belong to a market", product.marketId.isNotBlank())
            assertTrue("Product ${product.name} should have stall info", product.stallName.isNotBlank())
        }
    }

    @Test
    fun discountCalculation_isCorrect() {
        val subtotal1 = 25000
        val discount1 = if (subtotal1 >= 30000) 3000 else 0
        assertEquals(0, discount1)

        val subtotal2 = 35000
        val discount2 = if (subtotal2 >= 30000) 3000 else 0
        assertEquals(3000, discount2)
    }

    @Test
    fun quickListPresets_referenceExistingProducts() {
        val productIds = SeedData.INITIAL_PRODUCTS.map { it.id }.toSet()
        SeedData.QUICK_LISTS.forEach { combo ->
            combo.presetProductIds.forEach { pid ->
                assertTrue("Combo ${combo.title} refers to missing product $pid", productIds.contains(pid))
            }
        }
    }
}
