package com.example.dictionary.feature_dictionary.di

import android.app.Application
import androidx.room.Room
import com.example.dictionary.feature_dictionary.data.local.Converters
import com.example.dictionary.feature_dictionary.data.local.WordInfoDao
import com.example.dictionary.feature_dictionary.data.local.WordInfoDatabase
import com.example.dictionary.feature_dictionary.data.remote.DictionaryApi
import com.example.dictionary.feature_dictionary.data.repository.WordInfoRepositoryImpl
import com.example.dictionary.feature_dictionary.data.util.GsonParser
import com.example.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import com.example.dictionary.feature_dictionary.domain.use_case.GetWordInfo
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
    fun provideGetWordInfoUseCase(repo: WordInfoRepository): GetWordInfo {
        return GetWordInfo(repo)
    }

    @Provides
    @Singleton
    fun provideWordInfoRepository(api: DictionaryApi, db: WordInfoDatabase): WordInfoRepository {
        return WordInfoRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideWordInfoDatabase(app: Application): WordInfoDatabase {
        return Room
            .databaseBuilder(
            app,
            WordInfoDatabase::class.java,
            "word_db"
            )
            /* To add custom type converter that you created for the meanings object */
            .addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun provideDictionaryApi(): DictionaryApi {
        return Retrofit.Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }

}