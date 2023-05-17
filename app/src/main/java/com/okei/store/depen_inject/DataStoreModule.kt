package com.okei.store.depen_inject

import android.content.Context
import androidx.datastore.core.ExperimentalMultiProcessDataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import com.okei.store.data.data_source.serializer.CartSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CartDataStore

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    @CartDataStore
    @OptIn(ExperimentalMultiProcessDataStore::class)
    fun provideCartDataStore(@ApplicationContext context: Context) = MultiProcessDataStoreFactory.create(
        serializer = CartSerializer,
        produceFile = {
            File("${context.dataDir.path}/${CartSerializer.cartFileName}")
        }
    )

}