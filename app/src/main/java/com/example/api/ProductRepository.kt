package com.example.api

class ProductRepository(private val api: FakeStoreApi) {
    suspend fun fetchProducts(): List<Product> = api.getProducts()
}
