package com.tienda.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tienda.app.ui.screens.CartScreen
import com.tienda.app.ui.screens.CatalogScreen
import com.tienda.app.ui.screens.HomeScreen
import com.tienda.app.ui.screens.ProductDetailScreen
import com.tienda.app.ui.theme.TiendaAppTheme

sealed class Screen {
    object Home          : Screen()
    object Catalog       : Screen()
    object ProductDetail : Screen()
    object Cart          : Screen()
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
            onNavigateToCart = { currentScreen = Screen.Cart }
        )

        is Screen.Catalog -> CatalogScreen(
            onNavigateToHome = { currentScreen = Screen.Home },
            onNavigateToCatalog = { currentScreen = Screen.Catalog },
            onNavigateToCart = { currentScreen = Screen.Cart }
        )

        is Screen.ProductDetail -> ProductDetailScreen(
            onBack = { currentScreen = Screen.Catalog },
            onAddToCart = { currentScreen = Screen.Cart }
        )

        is Screen.Cart -> CartScreen(
            onNavigateToHome = { currentScreen = Screen.Home },
            onNavigateToCatalog = { currentScreen = Screen.Catalog },
            onNavigateToCart = { currentScreen = Screen.Cart },
            onCheckOut = { currentScreen = Screen.Home }
        )



        else -> {}
    }


}