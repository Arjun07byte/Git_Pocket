package com.example.daggerfirst.dependencyInjection

import com.example.daggerfirst.api.HomeApiRef
import com.example.daggerfirst.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    @Singleton
    fun provideHomeApiRef(): HomeApiRef {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .client(
                OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS).build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HomeApiRef::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(apiRef: HomeApiRef): HomeRepository {
        return HomeRepository(apiRef)
    }
}