# PRD 解读结果：任务拆解

> 由 AI 根据 PRD.md 自动生成

## 模块划分

| 模块 | 职责 | 新增 |
|------|------|:---:|
| `:feature:feed` | 文章列表首页 | 新增 |
| `:feature:article` | 文章详情 | 新增 |
| `:feature:bookmark` | 收藏管理 | 新增 |
| `:feature:search` | 搜索 | 新增 |
| `:core:domain` | 模型 + 接口 | 扩充 |
| `:core:data` | Room + 数据源实现 | 扩充 |

## 领域模型

```kotlin
data class Article(
    val id: String,        // 唯一标识
    val title: String,     // 文章标题
    val summary: String,   // 摘要
    val content: String,   // 正文（Markdown）
    val author: String,    // 作者
    val publishDate: String, // 发布日期
    val tags: List<String>,  // 标签列表
    val isBookmarked: Boolean // 是否收藏
)
```

## Repository 接口

```kotlin
interface ArticleRepository {
    suspend fun getArticles(): List<Article>           // 首次加载 + 缓存
    suspend fun getArticle(id: String): Article?        // 文章详情
    fun searchArticles(query: String): Flow<List<Article>>  // 搜索
    suspend fun toggleBookmark(id: String)             // 收藏切换
    fun observeArticles(): Flow<List<Article>>         // 文章列表流
    fun observeBookmarks(): Flow<List<Article>>        // 收藏列表流
}
```

## UseCase 清单

| UseCase | 对应功能 | 说明 |
|---------|---------|------|
| `GetArticlesUseCase` | F1 列表 | 首次加载触发缓存 |
| `GetArticleUseCase` | F2 详情 | 按 id 查询 |
| `SearchArticlesUseCase` | F4 搜索 | 返回 Flow，实时更新 |
| `ToggleBookmarkUseCase` | F1/F3 收藏 | 切换收藏状态 |
| `ObserveArticlesUseCase` | F1 列表 | Room Flow 观察 |
| `ObserveBookmarksUseCase` | F3 收藏列表 | 仅已收藏文章 |

## 数据流

```
assets/articles.json
        ↓
FakeRemoteDataSource（模拟网络延迟 400ms）
        ↓ 首次加载
Room Database（ArticleEntity）
        ↓ Flow
ArticleRepositoryImpl
        ↓ UseCase
ViewModel（StateFlow<UiState>）
        ↓ collectAsState
Compose UI
```

- **observe 型**（Flow）：Feed 列表、Bookmark 列表、Search — 数据变化自动更新 UI
- **suspend 型**：文章详情、收藏切换 — 一次性操作

## UI 状态设计

```kotlin
// F1 Feed
data class FeedUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

// F2 Article
data class ArticleUiState(
    val article: Article? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

// F3 Bookmark
data class BookmarkUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = true
)

// F4 Search
data class SearchUiState(
    val query: String = "",
    val results: List<Article> = emptyList()
)
```

## 开发任务清单

| # | 任务 | 模块 | 优先级 | 依赖 |
|---|------|------|:---:|---|
| 1 | 定义 Article 数据类 | core:domain | P0 | — |
| 2 | 定义 ArticleRepository 接口 | core:domain | P0 | 1 |
| 3 | 实现 UseCase 类（6个） | core:domain | P0 | 2 |
| 4 | 创建 ArticleEntity + DAO + Database | core:data | P0 | 1 |
| 5 | 实现 FakeRemoteDataSource | core:data | P0 | 1 |
| 6 | 实现 ArticleRepositoryImpl | core:data | P0 | 2,4,5 |
| 7 | 编写 DataModule（Hilt 绑定） | core:data | P0 | 3,6 |
| 8 | FeedScreen + FeedViewModel | feature:feed | P1 | 7 |
| 9 | ArticleScreen + ArticleViewModel | feature:article | P1 | 7 |
| 10 | BookmarkScreen + BookmarkViewModel | feature:bookmark | P1 | 7 |
| 11 | SearchScreen + SearchViewModel | feature:search | P1 | 7 |
| 12 | MainActivity NavHost 集成 | app | P1 | 8-11 |
