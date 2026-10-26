package com.example.data.db

import com.example.data.model.MarketEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.QuickListPreset
import com.example.data.model.UserEntity

object SeedData {
    val INITIAL_MARKETS = listOf(
        MarketEntity(
            id = "mercado-central",
            name = "Plaza Central de Madrid",
            status = "Abierto",
            distance = "0.8 km",
            vendorsCount = 24,
            description = "Plaza de Mercado municipal de Madrid (Cundinamarca), ubicada junto al Parque Pedro Fernández Madrid. Especialidad en frutas frescas, hierbas aromáticas y hortalizas cosechadas de madrugada.",
            hours = "5:00 AM - 4:30 PM",
            rating = 4.8f,
            reviewsCount = 380,
            deliveryTime = "20 - 35 min",
            accentColor = "#F97316",
            badgeText = "Centro histórico"
        ),
        MarketEntity(
            id = "plaza-norte",
            name = "Mercado San José - La Prosperidad",
            status = "Abierto",
            distance = "1.5 km",
            vendorsCount = 35,
            description = "Abastecimiento fresco para La Prosperidad y el norte de Madrid, Cundinamarca. Cortes de carne seleccionados, truchas de la región y hortalizas hidropónicas.",
            hours = "6:00 AM - 5:00 PM",
            rating = 4.9f,
            reviewsCount = 495,
            deliveryTime = "25 - 40 min",
            accentColor = "#10B981",
            badgeText = "Variedad fresca"
        ),
        MarketEntity(
            id = "mercado-sur",
            name = "Mercado El Sosiego & El Cortijo",
            status = "Cerrado",
            distance = "3.2 km",
            vendorsCount = 18,
            description = "Punto tradicional del sector El Sosiego en Madrid, Cundinamarca. Especialistas en venta mayorista de papa sabanera, tubérculos y bultos de granos secos.",
            hours = "Cerrado hoy • Abre 5:00 AM",
            rating = 4.6f,
            reviewsCount = 210,
            deliveryTime = "Programar para mañana",
            accentColor = "#EF4444",
            badgeText = "Precios mayoristas"
        ),
        MarketEntity(
            id = "feria-campesina",
            name = "Feria Campesina de Madrid & Sabana",
            status = "Abierto",
            distance = "4.1 km",
            vendorsCount = 50,
            description = "Directo del productor campesino de veredas Puente de Piedra, El Corzo, Chueca y Bojacá. Cosecha del día: fresas sabaneras, café de finca, huevos de pastoreo y panela.",
            hours = "6:00 AM - 3:00 PM",
            rating = 4.95f,
            reviewsCount = 620,
            deliveryTime = "30 - 45 min",
            accentColor = "#16A34A",
            badgeText = "100% Campesino"
        )
    )

    val INITIAL_PRODUCTS = listOf(
        // Plaza Central de Madrid
        ProductEntity(
            id = "mc-1",
            name = "Aguacate Hass Maduro",
            category = "frutas",
            price = 4500,
            unit = "kg (aprox. 3 und)",
            image = "https://images.unsplash.com/photo-1523049673857-eb18f1d7b578?w=500&auto=format&fit=crop&q=80",
            stallName = "Frutas Doña María",
            stallNumber = "Puesto 12",
            marketId = "mercado-central",
            marketName = "Plaza Central de Madrid",
            freshBadge = "Punto perfecto",
            description = "Cremoso, cultivado en altura. Listo para guacamole o ensalada.",
            inStock = true
        ),
        ProductEntity(
            id = "mc-2",
            name = "Tomate Chonto Seleccionado",
            category = "verduras",
            price = 3200,
            unit = "kg",
            image = "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=500&auto=format&fit=crop&q=80",
            stallName = "Verduras Don Chepe",
            stallNumber = "Puesto 04",
            marketId = "mercado-central",
            marketName = "Plaza Central de Madrid",
            freshBadge = "Cosecha de hoy",
            description = "Firme, rojo intenso, ideal para guisos, hogao o ensaladas frescas.",
            inStock = true
        ),
        ProductEntity(
            id = "mc-3",
            name = "Mango Tommy Dulce",
            category = "frutas",
            price = 3800,
            unit = "kg",
            image = "https://images.unsplash.com/photo-1553279768-865429fa0078?w=500&auto=format&fit=crop&q=80",
            stallName = "Frutas Doña María",
            stallNumber = "Puesto 12",
            marketId = "mercado-central",
            marketName = "Plaza Central de Madrid",
            freshBadge = "Muy dulce",
            description = "Pulpa jugosa sin hilacha, perfecto para postres o jugo natural.",
            inStock = true
        ),
        ProductEntity(
            id = "mc-4",
            name = "Cilantro & Cebollín Fresco",
            category = "hierbas",
            price = 1500,
            unit = "atado grande",
            image = "https://images.unsplash.com/photo-1588879462529-27083fb86e3f?w=500&auto=format&fit=crop&q=80",
            stallName = "Hierbas Doña Rosa",
            stallNumber = "Puesto 19",
            marketId = "mercado-central",
            marketName = "Plaza Central de Madrid",
            freshBadge = "Recién cortado",
            description = "Aroma fresco inconfundible, lavado y listo para dar sazón.",
            inStock = true
        ),
        ProductEntity(
            id = "mc-5",
            name = "Pechuga Campesina de Pollo",
            category = "carnes",
            price = 13500,
            unit = "kg (limpia)",
            image = "https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=500&auto=format&fit=crop&q=80",
            stallName = "Carnicería La Fama",
            stallNumber = "Puesto 31",
            marketId = "mercado-central",
            marketName = "Plaza Central de Madrid",
            freshBadge = "Corte del día",
            description = "Pollo fresco alimentado con maíz, tierno y sin hormonas añadidas.",
            inStock = true
        ),
        ProductEntity(
            id = "mc-6",
            name = "Papaya Maradol Mediana",
            category = "frutas",
            price = 4200,
            unit = "unidad entera",
            image = "https://images.unsplash.com/photo-1617112848923-cc2234396a8d?w=500&auto=format&fit=crop&q=80",
            stallName = "Frutas Doña María",
            stallNumber = "Puesto 12",
            marketId = "mercado-central",
            marketName = "Plaza Central de Madrid",
            freshBadge = "Madura",
            description = "Sabor dulce y digestivo, cáscara brillante y pulpa firme.",
            inStock = true
        ),
        // Mercado San José - La Prosperidad
        ProductEntity(
            id = "pn-1",
            name = "Filete de Trucha Fresca",
            category = "pescados",
            price = 24000,
            unit = "kg",
            image = "https://images.unsplash.com/photo-1534939561126-855b8675edd7?w=500&auto=format&fit=crop&q=80",
            stallName = "Pescadería San José",
            stallNumber = "Puesto 08",
            marketId = "plaza-norte",
            marketName = "Mercado San José - La Prosperidad",
            freshBadge = "Pescado fresco",
            description = "Llegado en hielo al amanecer. Textura suave, bajo en grasa.",
            inStock = true
        ),
        ProductEntity(
            id = "pn-2",
            name = "Lomo Fino de Res Seleccionado",
            category = "carnes",
            price = 28000,
            unit = "kg",
            image = "https://images.unsplash.com/photo-1544025162-d76694265947?w=500&auto=format&fit=crop&q=80",
            stallName = "Carnes San José",
            stallNumber = "Puesto 14",
            marketId = "plaza-norte",
            marketName = "Mercado San José - La Prosperidad",
            freshBadge = "Maduración 14 días",
            description = "El corte más suave y magro, perfecto para medallones a la plancha.",
            inStock = true
        ),
        ProductEntity(
            id = "pn-3",
            name = "Lechuga Romana & Espinaca Baby",
            category = "verduras",
            price = 3500,
            unit = "paquete 500g",
            image = "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=500&auto=format&fit=crop&q=80",
            stallName = "Hortalizas La Huerta Verde",
            stallNumber = "Puesto 15",
            marketId = "plaza-norte",
            marketName = "Mercado San José - La Prosperidad",
            freshBadge = "Hidropónica",
            description = "Hojas crujientes y limpias, cultivadas en invernaderos de Madrid, Cundinamarca.",
            inStock = true,
            organic = true
        ),
        ProductEntity(
            id = "pn-4",
            name = "Queso Campesino de la Sabana",
            category = "lacteos",
            price = 9800,
            unit = "bloque 500g",
            image = "https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=500&auto=format&fit=crop&q=80",
            stallName = "Lácteos de la Sabana",
            stallNumber = "Puesto 22",
            marketId = "plaza-norte",
            marketName = "Mercado San José - La Prosperidad",
            freshBadge = "Bajo en sal",
            description = "Elaborado artesanalmente con leche fresca de hatos lecheros de la sabana.",
            inStock = true
        ),
        // Feria Campesina de Madrid & Sabana
        ProductEntity(
            id = "fc-1",
            name = "Fresas Sabaneras Orgánicas",
            category = "campesino",
            price = 6000,
            unit = "canastilla 500g",
            image = "https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=500&auto=format&fit=crop&q=80",
            stallName = "Finca Vereda Puente de Piedra",
            stallNumber = "Puesto 03",
            marketId = "feria-campesina",
            marketName = "Feria Campesina de Madrid & Sabana",
            freshBadge = "Directo de mata",
            description = "Cultivadas sin pesticidas en huertas de Puente de Piedra (Madrid). Muy dulces.",
            inStock = true,
            organic = true
        ),
        ProductEntity(
            id = "fc-2",
            name = "Huevos Campesinos de Pastoreo",
            category = "campesino",
            price = 19500,
            unit = "panal 30 unidades",
            image = "https://images.unsplash.com/photo-1506976785307-8732e854ad03?w=500&auto=format&fit=crop&q=80",
            stallName = "Granja El Corzo",
            stallNumber = "Puesto 11",
            marketId = "feria-campesina",
            marketName = "Feria Campesina de Madrid & Sabana",
            freshBadge = "Yema dorada",
            description = "Gallinas libres de vereda El Corzo alimentadas con maíz y forraje verde.",
            inStock = true
        ),
        ProductEntity(
            id = "fc-3",
            name = "Café Especial de Finca (Molido)",
            category = "campesino",
            price = 18000,
            unit = "bolsa 500g",
            image = "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=500&auto=format&fit=crop&q=80",
            stallName = "Café Sabana de Occidente",
            stallNumber = "Puesto 27",
            marketId = "feria-campesina",
            marketName = "Feria Campesina de Madrid & Sabana",
            freshBadge = "Tostión media",
            description = "Notas a panela, chocolate y cítricos. 100% Arábica de altura.",
            inStock = true
        ),
        ProductEntity(
            id = "fc-4",
            name = "Papa Criolla Amarilla Lavada",
            category = "verduras",
            price = 4000,
            unit = "kg",
            image = "https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=500&auto=format&fit=crop&q=80",
            stallName = "Tubérculos Vereda Chueca",
            stallNumber = "Puesto 07",
            marketId = "feria-campesina",
            marketName = "Feria Campesina de Madrid & Sabana",
            freshBadge = "Suave y arenosa",
            description = "Especial para ajiaco sabanero o sopas. Cosechada en Madrid y Bojacá.",
            inStock = true
        ),
        ProductEntity(
            id = "fc-5",
            name = "Panela Orgánica Pulverizada",
            category = "campesino",
            price = 5500,
            unit = "bolsa 1kg",
            image = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=500&auto=format&fit=crop&q=80",
            stallName = "Trapiche Artesanal Campesino",
            stallNumber = "Puesto 18",
            marketId = "feria-campesina",
            marketName = "Feria Campesina de Madrid & Sabana",
            freshBadge = "100% Caña",
            description = "Endulzante natural sin aditivos químicos, disolución instantánea.",
            inStock = true,
            organic = true
        ),
        // Mercado El Sosiego & El Cortijo
        ProductEntity(
            id = "ms-1",
            name = "Bulto de Fríjol Bola Roja",
            category = "granos",
            price = 11000,
            unit = "bolsa 2kg",
            image = "https://images.unsplash.com/photo-1551462147-ff29053bfc14?w=500&auto=format&fit=crop&q=80",
            stallName = "Granos El Sosiego",
            stallNumber = "Puesto 02",
            marketId = "mercado-sur",
            marketName = "Mercado El Sosiego & El Cortijo",
            freshBadge = "Grano nuevo",
            description = "Fácil cocción, caldo espeso y suave al paladar.",
            inStock = true
        ),
        ProductEntity(
            id = "ms-2",
            name = "Plátano Hartón Verde & Maduro",
            category = "frutas",
            price = 4500,
            unit = "racimo 5 unidades",
            image = "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=500&auto=format&fit=crop&q=80",
            stallName = "Platanera El Cortijo",
            stallNumber = "Puesto 09",
            marketId = "mercado-sur",
            marketName = "Mercado El Sosiego & El Cortijo",
            freshBadge = "Grande",
            description = "Ideal para patacones crocantes o tajadas dulces al horno.",
            inStock = true
        )
    )

    val INITIAL_USERS = listOf(
        UserEntity(
            id = "usr-admin",
            name = "Andrés Morales",
            emailOrPhone = "andres@mercaeasy.co",
            passwordHash = "123456",
            avatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120&auto=format&fit=crop&q=80",
            provider = "email",
            defaultAddress = "Calle 7 # 5-40, Barrio San Carlos, Madrid, Cundinamarca"
        ),
        UserEntity(
            id = "usr-demo",
            name = "María Gómez",
            emailOrPhone = "3101234567",
            passwordHash = "123456",
            avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=120&auto=format&fit=crop&q=80",
            provider = "email",
            defaultAddress = "Cra 4 # 8-20, La Prosperidad, Madrid, Cundinamarca"
        )
    )

    val INITIAL_ORDERS = listOf(
        OrderEntity(
            id = "ORD-892104",
            userId = "usr-admin",
            customerName = "Andrés Morales",
            address = "Calle 7 # 5-40, Barrio San Carlos, Madrid, Cundinamarca",
            phone = "310 456 7890",
            notes = "Aguacates maduros para hoy",
            paymentMethod = "Nequi / Daviplata",
            marketName = "Plaza Central de Madrid",
            status = "entregado",
            subtotal = 12200,
            deliveryFee = 4500,
            discount = 0,
            total = 16700,
            itemsSummary = "2x Aguacate Hass Maduro, 1x Tomate Chonto Seleccionado",
            createdAt = System.currentTimeMillis() - (1000 * 60 * 60 * 5)
        )
    )

    val QUICK_LISTS = listOf(
        QuickListPreset(
            id = "frutas-verduras",
            title = "Frutas y Verduras",
            subtitle = "Para el almuerzo de hoy (fresco de plaza)",
            itemCount = 4,
            estimatedPrice = 13000,
            badge = "Recomendado IA",
            presetProductIds = listOf("mc-1", "mc-2", "mc-3", "mc-4")
        ),
        QuickListPreset(
            id = "canasta-familiar",
            title = "Canasta Familiar",
            subtitle = "Ahorro semanal con precios de plaza mayorista",
            itemCount = 5,
            estimatedPrice = 48500,
            badge = "35% Ahorro vs Supermercado",
            presetProductIds = listOf("mc-5", "fc-2", "mc-2", "fc-4", "pn-4")
        ),
        QuickListPreset(
            id = "saludable-organico",
            title = "Feria Saludable & Orgánica",
            subtitle = "Directo de huertas campesinas y de altura",
            itemCount = 4,
            estimatedPrice = 38800,
            badge = "100% Natural",
            presetProductIds = listOf("fc-1", "fc-2", "fc-3", "pn-3")
        )
    )
}
