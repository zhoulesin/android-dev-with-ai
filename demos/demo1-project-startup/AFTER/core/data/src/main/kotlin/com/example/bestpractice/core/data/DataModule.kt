package com.example.bestpractice.core.data

import android.content.Context
import android.content.res.AssetManager
import androidx.room.Room
import com.example.bestpractice.core.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideArticleDatabase(@ApplicationContext context: Context): ArticleDatabase =
        Room.databaseBuilder(context, ArticleDatabase::class.java, "articles.db")
            .build()

    @Provides
    @Singleton
    fun provideArticleDao(db: ArticleDatabase): ArticleDao = db.articleDao()

    @Provides
    @Singleton
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager =
        context.assets

    @Provides
    @Singleton
    fun bindArticleRepository(impl: ArticleRepositoryImpl): ArticleRepository = impl

    @Provides
    @Singleton
    fun provideGetArticlesUseCase(repo: ArticleRepository): GetArticlesUseCase =
        GetArticlesUseCase(repo)

    @Provides
    @Singleton
    fun provideGetArticleUseCase(repo: ArticleRepository): GetArticleUseCase =
        GetArticleUseCase(repo)

    @Provides
    @Singleton
    fun provideToggleBookmarkUseCase(repo: ArticleRepository): ToggleBookmarkUseCase =
        ToggleBookmarkUseCase(repo)

    @Provides
    @Singleton
    fun provideSearchArticlesUseCase(repo: ArticleRepository): SearchArticlesUseCase =
        SearchArticlesUseCase(repo)

    @Provides
    @Singleton
    fun provideObserveArticlesUseCase(repo: ArticleRepository): ObserveArticlesUseCase =
        ObserveArticlesUseCase(repo)

    @Provides
    @Singleton
    fun provideObserveBookmarksUseCase(repo: ArticleRepository): ObserveBookmarksUseCase =
        ObserveBookmarksUseCase(repo)
}
