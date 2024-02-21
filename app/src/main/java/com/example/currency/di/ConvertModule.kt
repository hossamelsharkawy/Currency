package com.example.currency.di

import com.example.currency.data.ApiService
import com.example.currency.data.ConvertRepository
import com.example.currency.data.ConvertRepositoryImp
import com.example.currency.data.TestConvertRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent


@Module
@InstallIn(ActivityRetainedComponent::class)
object ConvertModule {

    @Provides
    fun provide(): ConvertRepository =
        TestConvertRepositoryImp()

/*    @Provides
    fun provide(apiService: ApiService): ConvertRepository =
       ConvertRepositoryImp(apiService)*/

}