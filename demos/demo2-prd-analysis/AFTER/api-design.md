# API / 数据源设计

> 由 LLM 根据数据模型和功能需求生成数据层接口

## Repository 接口（domain 层）

```kotlin
interface ArticleRepository {
    // 首次加载 + 触发缓存
    suspend fun getArticles(): List<Article>

    // 详情查询
    suspend fun getArticle(id: String): Article?

    // 搜索（返回 Flow，支持实时更新）
    fun searchArticles(query: String): Flow<List<Article>>

    // 收藏切换（本地操作，即时生效）
    suspend fun toggleBookmark(id: String)

    // 文章列表观察（Room Flow，自动更新）
    fun observeArticles(): Flow<List<Article>>

    // 收藏列表观察
    fun observeBookmarks(): Flow<List<Article>>
}
```

**设计原则**：
- `observe*` 方法返回 Flow → UI 自动响应数据变化
- `suspend` 方法用于一次性操作 → 配合 viewModelScope.launch
- 搜索也返回 Flow → 用户连续输入时自动更新结果

## FakeRemoteDataSource（data 层）

```kotlin
class FakeRemoteDataSource(assetManager: AssetManager) {
    suspend fun fetchArticles(): List<Article>
}
```

**模拟行为**：
- 读取 `assets/articles.json`
- `delay(400ms)` 模拟网络延迟
- 解析为 `List<Article>`

## ArticleDao（Room）

```kotlin
@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY publishDate DESC")
    fun observeAll(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id")
    suspend fun getById(id: String): ArticleEntity?

    @Query("SELECT * FROM articles WHERE isBookmarked = 1 ORDER BY publishDate DESC")
    fun observeBookmarks(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE title LIKE '%' || :query || '%'
            OR summary LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<ArticleEntity>>

    @Query("UPDATE articles SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun setBookmarked(id: String, bookmarked: Boolean)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(articles: List<ArticleEntity>)
}
```

## ArticleRepositoryImpl（缓存策略）

```kotlin
class ArticleRepositoryImpl(
    private val articleDao: ArticleDao,
    private val remoteDataSource: FakeRemoteDataSource
) : ArticleRepository {

    override suspend fun getArticles(): List<Article> {
        if (articleDao.count() == 0) {
            val remote = remoteDataSource.fetchArticles()
            articleDao.insertAll(remote.map { it.toEntity() })
        }
        return articleDao.observeAll().first().map { it.toDomain() }
    }
}
```

**缓存策略**：
- 首次启动：Room 为空 → 从 FakeRemote 加载 → 写入 Room
- 后续启动：Room 有数据 → 直接读缓存，不重复加载
- 收藏操作：直接更新 Room，无需 Remote 同步
