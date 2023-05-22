package com.okei.store.domain.use_case.product

import com.okei.store.domain.repos.ProductRepository
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend fun execute() = runCatching {
        productRepository.getProducts()
    }
}