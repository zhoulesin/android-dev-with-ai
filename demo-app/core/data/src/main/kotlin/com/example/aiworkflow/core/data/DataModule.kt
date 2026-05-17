package com.example.aiworkflow.core.data

import android.content.Context
import android.content.res.AssetManager
import com.example.aiworkflow.core.common.DefaultDispatcherProvider
import com.example.aiworkflow.core.common.DispatcherProvider
import com.example.aiworkflow.core.domain.*
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
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager =
        context.assets

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun bindContentRepository(repo: AssetContentRepository): ContentRepository = repo

    @Provides
    @Singleton
    fun bindBenchmarkRepository(repo: BenchmarkCatalog): BenchmarkRepository = repo

    @Provides
    @Singleton
    fun bindChecklistRepository(repo: ReleaseChecklistRepository): ChecklistRepository = repo

    @Provides
    @Singleton
    fun provideGetHubDestinationsUseCase(): GetHubDestinationsUseCase =
        GetHubDestinationsUseCase()

    @Provides
    @Singleton
    fun provideListPlaybookDocumentsUseCase(repo: ContentRepository): ListPlaybookDocumentsUseCase =
        ListPlaybookDocumentsUseCase(repo)

    @Provides
    @Singleton
    fun provideGetPlaybookDocumentUseCase(repo: ContentRepository): GetPlaybookDocumentUseCase =
        GetPlaybookDocumentUseCase(repo)

    @Provides
    @Singleton
    fun provideListBenchmarkScenariosUseCase(repo: BenchmarkRepository): ListBenchmarkScenariosUseCase =
        ListBenchmarkScenariosUseCase(repo)

    @Provides
    @Singleton
    fun provideGetBenchmarkDetailUseCase(repo: BenchmarkRepository): GetBenchmarkDetailUseCase =
        GetBenchmarkDetailUseCase(repo)

    @Provides
    @Singleton
    fun provideObserveReleaseChecklistUseCase(repo: ChecklistRepository): ObserveReleaseChecklistUseCase =
        ObserveReleaseChecklistUseCase(repo)

    @Provides
    @Singleton
    fun provideToggleChecklistItemUseCase(repo: ChecklistRepository): ToggleChecklistItemUseCase =
        ToggleChecklistItemUseCase(repo)

    @Provides
    @Singleton
    fun provideResetReleaseChecklistUseCase(repo: ChecklistRepository): ResetReleaseChecklistUseCase =
        ResetReleaseChecklistUseCase(repo)

    @Provides
    @Singleton
    fun provideGetReleaseChecklistUseCase(observe: ObserveReleaseChecklistUseCase): GetReleaseChecklistUseCase =
        GetReleaseChecklistUseCase(observe)
}
