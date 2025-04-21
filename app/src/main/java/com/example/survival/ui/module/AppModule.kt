package com.example.survival.ui.module

import com.example.survival.ui.home.HomeRepo
import com.example.survival.ui.second.SecondRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun homeRepo(): HomeRepo {
        return HomeRepo()
    }

    @Provides
    fun secondRepo(): SecondRepo {
        return SecondRepo()
    }
}