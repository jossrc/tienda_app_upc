package com.tienda.app.backend.models

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val data: List<Product>,
    val isSuccessful: Boolean,
    val code: Error
)

data class Product(
    @SerializedName("id")
    val codigo: Int,
    val idcategoria: Int,
    val categoria: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val importancia: Int,
    val imagen: String
)