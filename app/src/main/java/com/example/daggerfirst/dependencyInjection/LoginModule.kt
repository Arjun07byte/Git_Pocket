package com.example.daggerfirst.dependencyInjection

import com.example.daggerfirst.api.LoginApiRef
import com.example.daggerfirst.repository.LoginRepositoryInst
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {
    @Provides
    @Singleton
    fun provideLoginApiRef(): LoginApiRef {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginApiRef::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(apiRef: LoginApiRef): LoginRepositoryInst {
        return LoginRepositoryInst(apiRef)
    }
}