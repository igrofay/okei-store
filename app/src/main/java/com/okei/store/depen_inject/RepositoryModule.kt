package com.okei.store.depen_inject

import com.okei.store.data.repos.CartRepositoryImpl
import com.okei.store.data.repos.ProductRepositoryImpl
import com.okei.store.data.repos.UserRepositoryImpl
import com.okei.store.domain.repos.CartRepository
import com.okei.store.domain.repos.ProductRepository
import com.okei.store.domain.repos.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
    @Binds
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository
    @Binds
    abstract fun bindCartRepository(
        cartReposImpl: CartRepositoryImpl
    ) : CartRepository
}