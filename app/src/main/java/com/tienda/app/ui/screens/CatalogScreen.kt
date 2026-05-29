package com.tienda.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.tienda.app.backend.models.Product
import com.tienda.app.backend.viewmodels.ProductViewModel
import com.tienda.app.ui.components.BottomNavItem
import com.tienda.app.ui.components.StoreNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onViewDetail: (Int) -> Unit = {}
) {

    val currentRoute = BottomNavItem.Catalog.route

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("Catálogo") }
            )
        },
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

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val viewModel: ProductViewModel = viewModel()
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewModel.listaProductos) { product ->
                        ProductItem(
                            producto = product,
                            onClickProducto = { onViewDetail(product.codigo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    producto: Product,
    onClickProducto: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable{
                onClickProducto()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column {
            AsyncImage(
                model = producto.imagen,
                contentDescription = producto.nombre,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = producto.categoria,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = producto.nombre,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "S/. ${producto.precio}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatalogScreenPreview() {
    MaterialTheme {
        CatalogScreen()
    }
}


