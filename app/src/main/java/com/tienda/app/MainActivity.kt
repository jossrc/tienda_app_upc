package com.tienda.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tienda.app.ui.screens.CartScreen
import com.tienda.app.ui.screens.CatalogScreen
import com.tienda.app.ui.screens.HomeScreen
import com.tienda.app.ui.screens.ProductDetailScreen
import com.tienda.app.ui.screens.RegisterProductScreen

sealed class Screen {
    object Home          : Screen()
    object Catalog       : Screen()
    data class ProductDetail(val codigo: Int) : Screen()
    object Cart          : Screen()
    object RegisterProduct : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                TiendaApp()
            }
        }
    }
}

@Composable
fun TiendaApp(

) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    when(currentScreen){
        is Screen.Home -> HomeScreen(
            onNavigateToHome = { currentScreen = Screen.Home },
            onNavigateToCatalog = { currentScreen = Screen.Catalog },
            onNavigateToCart = { currentScreen = Screen.Cart },
            onNavigateToProductRegister = { currentScreen = Screen.RegisterProduct }
        )

        is Screen.Catalog -> CatalogScreen(
            onNavigateToHome = { currentScreen = Screen.Home },
            onNavigateToCatalog = { currentScreen = Screen.Catalog },
            onNavigateToCart = { currentScreen = Screen.Cart },
            onViewDetail = { codigo -> currentScreen = Screen.ProductDetail(codigo) }
        )

        is Screen.ProductDetail -> {
            val pantalla = currentScreen as Screen.ProductDetail
            ProductDetailScreen(
                idProducto = pantalla.codigo,
                onBack = { currentScreen = Screen.Catalog },
                onNavigateToCart = { currentScreen = Screen.Cart }
            )
        }

        is Screen.Cart -> CartScreen(
            onNavigateToHome = { currentScreen = Screen.Home },
            onNavigateToCatalog = { currentScreen = Screen.Catalog },
            onNavigateToCart = { currentScreen = Screen.Cart },
            onCheckOut = { currentScreen = Screen.Home }
        )

        is Screen.RegisterProduct -> RegisterProductScreen(
            onBack = { currentScreen = Screen.Home },
            onProductRegistered = { currentScreen = Screen.Catalog }
        )


        else -> {}
    }


}