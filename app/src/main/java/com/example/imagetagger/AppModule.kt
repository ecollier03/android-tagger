package com.example.imagetagger

import android.content.Context
import androidx.room.Room
import com.example.imagetagger.repositories.PhotoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // This makes the module scoped to the entire app
object AppModule {

    @Provides
    @Singleton
    fun providePhotosDatabase(@ApplicationContext context: Context): PhotoDatabase {
        return Room
            .databaseBuilder(
                context,
                PhotoDatabase::class.java,
                "photo_database"
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}