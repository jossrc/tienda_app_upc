package com.tienda.app.backend.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tienda.app.backend.models.Product
import com.tienda.app.backend.services.RetrofitClient
import kotlinx.coroutines.launch

class ProductViewModel: ViewModel() {

    var listaProductos by mutableStateOf<List<Product>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        fetchProductos()
    }

    private fun fetchProductos() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getProductos()

                listaProductos = response.data

            } catch (e: Exception) {
                Log.i("======>", e.toString())
            }
        }
    }

    fun registrarProducto(
        idCategoria: Int,
        nombre: String,
        precio: Double,
        descripcion: String,
        stock: Int,
        importancia: Int,
        imagen: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){

        viewModelScope.launch {
            isLoading = true

            try {
              val nuevoProducto = Product(
                  codigo = 0,
                  idcategoria = idCategoria,
                  categoria = "",
                  nombre = nombre,
                  descripcion = descripcion,
                  precio = precio,
                  stock = stock,
                  importancia = importancia,
                  imagen = imagen
              )

              val response = RetrofitClient.apiService.registrarProducto(nuevoProducto)
              fetchProductos()

              if (response.isSuccessful) {
                  onSuccess()
              } else {
                  val codigoError = response.code
                  onError("El servicio devolvió: $codigoError")
              }


            } catch (e: Exception){
                Log.i("======>", e.toString())
                onError("Error al registrar el producto: ${e.message}")
            } finally {
                isLoading = false
            }


        }

    }

}