package com.tienda.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tienda.app.R
import com.tienda.app.ui.components.BottomNavItem
import com.tienda.app.ui.components.StoreNavigationBar


@Composable
fun HomeScreen(
    onNavigateToHome: () -> Unit  = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToProductRegister: () -> Unit = {}
) {

    val currentRoute = BottomNavItem.Home.route

    Scaffold(
        bottomBar = {
            StoreNavigationBar(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    when (route) {
                        BottomNavItem.Home.route -> onNavigateToHome()
                        BottomNavItem.Catalog.route -> onNavigateToCatalog()
                        BottomNavItem.Cart.route -> onNavigateToCart()
                    }
                }
            )
        }
    ) {
        paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_principal),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "Home Screen",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Esta es la página de inicio",
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = onNavigateToHome
                ) {
                    Text("Ir A Home")
                }

                Button(
                    onClick = onNavigateToCatalog
                ) {
                    Text("Ir A Catálogo")
                }

                Button(
                    onClick = onNavigateToCart
                ) {
                    Text("Ir A Carrito")
                }

                Button(
                    onClick = onNavigateToProductRegister
                ) {
                    Text("Registrar Producto")
                }



            }
        }
    }



}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}
