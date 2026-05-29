package com.tienda.app.backend.services

import com.tienda.app.backend.models.Product
import com.tienda.app.backend.models.ProductResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductService {
    @GET("productos")
    suspend fun getProductos(): ProductResponse

    @POST("productos")
    suspend fun registrarProducto(@Body producto: Product): ProductResponse
}

object RetrofitClient {

    private const val BASE_URL = "https://a7u78zdrfd.execute-api.us-east-1.amazonaws.com/v1/"
    val apiService: ProductService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductService::class.java)
    }
}