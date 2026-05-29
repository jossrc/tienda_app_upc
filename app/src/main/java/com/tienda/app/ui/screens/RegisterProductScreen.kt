package com.tienda.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.tienda.app.backend.viewmodels.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterProductScreen(
    onBack: () -> Unit = {},
    onProductRegistered: () -> Unit = {}
) {

    val viewModel: ProductViewModel = viewModel()

    var nombre       by remember { mutableStateOf("") }
    var descripcion  by remember { mutableStateOf("") }
    var precio       by remember { mutableStateOf("") }
    var stock        by remember { mutableStateOf("") }
    var imagenUrl    by remember { mutableStateOf("") }

    val categorias = listOf("Lácteos", "Limpieza", "Abarrotes")
    var categoriaSeleccionada by remember { mutableStateOf(categorias[0]) }
    var dropdownExpanded     by remember { mutableStateOf(false) }
    var idcategoria by remember { mutableIntStateOf(1) }

    var nombreError      by remember { mutableStateOf(false) }
    var descripcionError by remember { mutableStateOf(false) }
    var precioError      by remember { mutableStateOf(false) }
    var stockError       by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            SeccionTitulo("Información básica")

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = false   // limpiar error al escribir
                },
                label = { Text("Nombre del producto *") },
                placeholder = { Text("Ej: Leche descremada") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = nombreError,
                supportingText = {
                    if (nombreError) Text("El nombre es obligatorio", color = MaterialTheme.colorScheme.error)
                }
            )

            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
            ) {
                OutlinedTextField(
                    value = categoriaSeleccionada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    categorias.forEachIndexed  { index, categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria) },
                            onClick = {
                                categoriaSeleccionada = categoria
                                idcategoria = index + 1
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }


            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    descripcionError = false
                },
                label = { Text("Descripción *") },
                placeholder = { Text("Ej: Leche de vaca descremada, baja en grasa") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 4,
                isError = descripcionError,
                supportingText = {
                    if (descripcionError) Text("La descripción es obligatoria", color = MaterialTheme.colorScheme.error)
                }
            )


            SeccionTitulo("Precio y Stock")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = precio,
                    onValueChange = {

                        if (it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            precio = it
                            precioError = false
                        }
                    },
                    label = { Text("Precio (S/.) *") },
                    placeholder = { Text("0.00") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = precioError,
                    supportingText = {
                        if (precioError) Text("Ingresa un precio válido", color = MaterialTheme.colorScheme.error)
                    }
                )


                OutlinedTextField(
                    value = stock,
                    onValueChange = {
                        if (it.matches(Regex("^\\d*$"))) {
                            stock = it
                            stockError = false
                        }
                    },
                    label = { Text("Stock *") },
                    placeholder = { Text("0") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = stockError,
                    supportingText = {
                        if (stockError) Text("Ingresa el stock", color = MaterialTheme.colorScheme.error)
                    }
                )
            }


            SeccionTitulo("Imagen del producto")


            OutlinedTextField(
                value = imagenUrl,
                onValueChange = { imagenUrl = it },
                label = { Text("URL de la imagen") },
                placeholder = { Text("https://ejemplo.com/imagen.jpg") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Image, contentDescription = null)
                }
            )


            if (imagenUrl.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imagenUrl,
                        contentDescription = "Previsualización",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))


            Button(
                onClick = {
                    nombreError      = nombre.isBlank()
                    descripcionError = descripcion.isBlank()
                    precioError      = precio.isBlank() || precio.toDoubleOrNull() == null
                    stockError       = stock.isBlank()  || stock.toIntOrNull() == null

                    if (nombreError || descripcionError || precioError || stockError) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor completa todos los campos obligatorios (*)")
                        }
                        return@Button
                    }

                    viewModel.registrarProducto(
                        idCategoria = idcategoria,
                        nombre      = nombre.trim(),
                        precio      = precio.toDouble(),
                        descripcion = descripcion.trim(),
                        stock       = stock.toInt(),
                        importancia = 1,
                        imagen      = imagenUrl.trim(),
                        onSuccess   = {
                            scope.launch {
                                snackbarHostState.showSnackbar("✓ Producto registrado correctamente")
                            }
                            onProductRegistered()
                        },
                        onError     = { mensajeError ->
                            scope.launch {
                                snackbarHostState.showSnackbar("Error: $mensajeError")
                            }
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Registrando...")
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Registrar Producto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = { onProductRegistered() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver al catálogo"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Ir al Catálogo")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SeccionTitulo(titulo: String) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterProductScreenPreview() {
    MaterialTheme { RegisterProductScreen() }
}