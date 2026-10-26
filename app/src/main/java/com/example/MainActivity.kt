package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.db.MercaDatabase
import com.example.data.model.OrderEntity
import com.example.data.repository.MercaRepository
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppHeader
import com.example.ui.components.SideMenuDrawer
import com.example.ui.dialogs.CheckoutDialog
import com.example.ui.dialogs.DatabaseInspectorDialog
import com.example.ui.dialogs.OrderSuccessDialog
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrdersHistoryScreen
import com.example.ui.screens.ProductsScreen
import com.example.ui.theme.MercaEasyTheme
import com.example.ui.theme.WarmBackground
import com.example.ui.viewmodel.MercaViewModel
import com.example.ui.viewmodel.MercaViewModelFactory
import com.example.ui.viewmodel.ScreenTab
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = MercaDatabase.getDatabase(applicationContext)
        val repository = MercaRepository(database)
        val factory = MercaViewModelFactory(repository)

        setContent {
            val viewModel: MercaViewModel = viewModel(factory = factory)

            MercaEasyTheme {
                MercaEasyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MercaEasyApp(viewModel: MercaViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val isGuest by viewModel.isGuest.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    val isCheckoutOpen by viewModel.isCheckoutOpen.collectAsState()
    val isOrderSuccessOpen by viewModel.isOrderSuccessOpen.collectAsState()
    val isDatabaseModalOpen by viewModel.isDatabaseModalOpen.collectAsState()
    val latestOrder by viewModel.latestOrder.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val totalCartCount = cart.sumOf { it.quantity }

    // First conditional screen: Mandatory registration/login unless guest
    if (currentUser == null && !isGuest) {
        AuthScreen(viewModel = viewModel)
        return
    }

    // Back button handling
    BackHandler(enabled = drawerState.isOpen || activeTab != ScreenTab.INICIO) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else if (activeTab != ScreenTab.INICIO) {
            viewModel.setTab(ScreenTab.INICIO)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideMenuDrawer(
                viewModel = viewModel,
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppHeader(
                    cartCount = totalCartCount,
                    currentUser = currentUser,
                    selectedCity = selectedCity,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onCartClick = { viewModel.setTab(ScreenTab.CANASTA) },
                    onAiClick = { viewModel.setTab(ScreenTab.IA) },
                    onDatabaseClick = { viewModel.setDatabaseModalOpen(true) }
                )
            },
            bottomBar = {
                AppBottomNav(
                    activeTab = activeTab,
                    onTabSelected = { viewModel.setTab(it) },
                    cartCount = totalCartCount
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(WarmBackground)
            ) {
                when (activeTab) {
                    ScreenTab.INICIO -> HomeScreen(viewModel = viewModel)
                    ScreenTab.PRODUCTOS -> ProductsScreen(viewModel = viewModel)
                    ScreenTab.IA -> AiAssistantScreen(viewModel = viewModel)
                    ScreenTab.CANASTA -> CartScreen(viewModel = viewModel)
                    ScreenTab.PEDIDOS -> OrdersHistoryScreen(
                        viewModel = viewModel,
                        onTrackOrder = { order ->
                            viewModel.setOrderSuccessOpen(true)
                        }
                    )
                }
            }
        }
    }

    // Modal Dialogs
    if (isCheckoutOpen) {
        CheckoutDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setCheckoutOpen(false) }
        )
    }

    if (isOrderSuccessOpen) {
        OrderSuccessDialog(
            order = latestOrder,
            onDismiss = { viewModel.setOrderSuccessOpen(false) }
        )
    }

    if (isDatabaseModalOpen) {
        DatabaseInspectorDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setDatabaseModalOpen(false) }
        )
    }
}
