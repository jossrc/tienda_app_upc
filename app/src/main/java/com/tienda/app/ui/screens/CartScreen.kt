package com.tienda.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.ShoppingCartCheckout
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.tienda.app.backend.models.Product
import com.tienda.app.ui.components.BottomNavItem
import com.tienda.app.ui.components.StoreNavigationBar

data class CartItem(
    val product: Product,
    val quantity: Int
)

@Composable
fun CartScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onCheckOut: () -> Unit = {}
) {
    val currentRoute = BottomNavItem.Cart.route
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val dummyProducts = remember {
        listOf(
            Product(1, 1, "Lácteos", "Leche", "Leche descremada", 4.20, 100, 1, "https://upcmov2026.s3.us-east-1.amazonaws.com/imagenes/leche.png"),
            Product(2, 1, "Lácteos", "Queso", "Queso fresco", 25.00, 10, 1, "https://upcmov2026.s3.us-east-1.amazonaws.com/imagenes/queso.png"),
            Product(3, 2, "Limpieza", "Ayudin", "Detergente liquido", 18.50, 100, 1, "https://upcmov2026.s3.us-east-1.amazonaws.com/imagenes/ayudin.png"),
            Product(4, 2, "Limpieza", "Opal", "Detergente polvo", 22.10, 100, 1, "https://upcmov2026.s3.us-east-1.amazonaws.com/imagenes/opal.png"),
            Product(5, 3, "Abarrotes", "Arroz", "Arroz integral", 5.00, 100, 1, "https://upcmov2026.s3.us-east-1.amazonaws.com/imagenes/arroz.png"),
            Product(6, 3, "Abarrotes", "Fideos", "Pasta italiana", 11.30, 100, 1, "https://upcmov2026.s3.us-east-1.amazonaws.com/imagenes/fideos.png"),
            Product(7, 3, "Abarrotes", "Harina", "Harina libre de gluten", 5.10, 100, 1, "https://upcmov2026.s3.us-east-1.amazonaws.com/imagenes/harina.png"),
            Product(8, 3, "Abarrotes", "Arroz Integral", "libre de gluten", 3.50, 12, 1, "https://upcmov2026.s3.us-east-1.amazonaws.com/imagenes/arroz.png")
        )
    }

    var cartItems by remember {
        mutableStateOf(
            listOf(
                CartItem(dummyProducts[0], 2), // 2 de Leche
                CartItem(dummyProducts[1], 1), // 1 de Queso
                CartItem(dummyProducts[4], 5)  // 5 de Arroz
            )
        )
    }

    // Estados para los diálogos (Alertas)
    var itemToDelete by remember { mutableStateOf<CartItem?>(null) }
    var showCheckoutSuccess by remember { mutableStateOf(false) }

    val totalAmount = cartItems.sumOf { it.product.precio * it.quantity }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Cabecera
            Text(
                text = "Mi Carrito",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Rounded.ShoppingCartCheckout,
                            contentDescription = "Empty",
                            modifier = Modifier.size(64.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Tu carrito está vacío", color = Color.Gray, fontSize = 16.sp)
                    }
                }
            } else {
                // Lista de Productos
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cartItems) { cartItem ->
                        CartItemCard(
                            item = cartItem,
                            onIncrease = {
                                if (cartItem.quantity < cartItem.product.stock) {
                                    cartItems = cartItems.map {
                                        if (it.product.codigo == cartItem.product.codigo) it.copy(quantity = it.quantity + 1) else it
                                    }
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Stock máximo alcanzado para ${cartItem.product.nombre}")
                                    }
                                }
                            },
                            onDecrease = {
                                if (cartItem.quantity > 1) {
                                    cartItems = cartItems.map {
                                        if (it.product.codigo == cartItem.product.codigo) it.copy(quantity = it.quantity - 1) else it
                                    }
                                }
                            },
                            onRemove = {
                                itemToDelete = cartItem // Abre el diálogo de confirmación
                            }
                        )
                    }
                }
            }

            // Resumen de Compra
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Total a Pagar", fontSize = 18.sp, color = Color.Gray)
                        Text(
                            text = "S/. ${"%.2f".format(totalAmount)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showCheckoutSuccess = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        enabled = cartItems.isNotEmpty()
                    ) {
                        Text(text = "Proceder al Pago", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }


        if (itemToDelete != null) {
            AlertDialog(
                onDismissRequest = { itemToDelete = null },
                icon = { Icon(Icons.Rounded.Warning, contentDescription = "Advertencia") },
                title = { Text(text = "Eliminar Producto") },
                text = { Text("¿Estás seguro de que deseas eliminar '${itemToDelete?.product?.nombre}' de tu carrito?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            cartItems = cartItems.filter { it.product.codigo != itemToDelete?.product?.codigo }
                            itemToDelete = null
                        }
                    ) {
                        Text("Eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { itemToDelete = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // 2. Éxito al comprar
        if (showCheckoutSuccess) {
            AlertDialog(
                onDismissRequest = { showCheckoutSuccess = false },
                icon = { Icon(Icons.Rounded.ShoppingCartCheckout, contentDescription = "Éxito") },
                title = { Text(text = "¡Compra Exitosa!") },
                text = { Text("Tu pedido por S/. ${"%.2f".format(totalAmount)} ha sido procesado correctamente.") },
                confirmButton = {
                    Button(
                        onClick = {
                            showCheckoutSuccess = false
                            cartItems = emptyList() // Vaciamos el carrito
                            onCheckOut()
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.product.imagen,
                contentDescription = item.product.nombre,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información del Producto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.product.categoria,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "S/. ${"%.2f".format(item.product.precio)}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Controles de cantidad y eliminar
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(80.dp)
            ) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red.copy(alpha = 0.7f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
                ) {
                    IconButton(onClick = onDecrease, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Remove, contentDescription = "Menos", modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = item.quantity.toString(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(onClick = onIncrease, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Add, contentDescription = "Más", modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenPreview() {
    MaterialTheme {
        CartScreen()
    }
}