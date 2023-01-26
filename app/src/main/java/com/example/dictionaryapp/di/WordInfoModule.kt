package com.example.dictionaryapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import com.example.dictionaryapp.data.local.Converters
import com.example.dictionaryapp.data.local.WordInfoDao
import com.example.dictionaryapp.data.local.WordInfoDatabase
import com.example.dictionaryapp.data.remote.DictionaryApi
import com.example.dictionaryapp.data.repository.WordInfoRepositoryImpl
import com.example.dictionaryapp.domain.repository.WordInfoRepository
import com.example.dictionaryapp.domain.usecase.GetWordInfo
import com.example.dictionaryapp.util.GsonParser
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordInfoModule {

    @Provides
    @Singleton
    fun provideGetWordInfo(repository: WordInfoRepository):GetWordInfo{
        return  GetWordInfo(repository)
    }

    @Provides
    @Singleton
    fun provideWordInfoRepository(
        db: WordInfoDatabase,
        api: DictionaryApi
    ):WordInfoRepository{
        return WordInfoRepositoryImpl(dao = db.dao, api = api)
    }

    @Provides
    @Singleton
    fun provideWordInfoDatabase(app: Application): WordInfoDatabase{
        return Room.databaseBuilder(
            app,
            WordInfoDatabase::class.java,
            "word_db"
        ).addTypeConverter(Converters(GsonParser(Gson())))
            .addMigrations()
            .build()
    }

    @Provides
    @Singleton
    fun provideDictionaryApi():DictionaryApi{
        return Retrofit.Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }

}