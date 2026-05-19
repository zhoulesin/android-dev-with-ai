# Demo 4 实操过程

> 编码实现中 AI 常见翻车：接口设计、缓存策略、跨层引用

---

## Round 1：一次性要求全写

### Human Prompt

```
根据 task-breakdown.md 完成所有模块代码
```

### AI Response（问题百出）

```kotlin
// ArticleRepositoryImpl.kt ❌
class ArticleRepositoryImpl {
    suspend fun getArticles(): List<Article> {
        // 直接用 FakeRemoteDataSource，没有 Room 缓存
        return fakeRemoteDataSource.fetchArticles()
    }
    // ❌ 没有实现 ArticleRepository 接口
    // ❌ 没有 Hilt @Inject
    // ❌ 每次打开都重新加载
}

// DataModule.kt ❌
@Module
class DataModule {
    @Provides fun provideRepo() = ArticleRepositoryImpl()
    // ❌ 没有 @Singleton，每次都是新实例
    // ❌ 没有 @InstallIn
}
```

### Human Review

| 问题 | 影响 |
|------|------|
| 未实现接口 | domain 层引用的 ArticleRepository 没有实现类 |
| 无 Room 缓存 | 首次打开后离线无法阅读 |
| 无 Hilt 注解 | 编译失败 |
| 全放一个文件 | 不可维护 |

---

## Round 2：逐层纠正

### Human Feedback

```
逐个模块检查：
1. core:domain 层：
   - ArticleRepository 接口方法区分 observe（Flow）和 suspend（一次性）
   - 每个 UseCase 独立文件

2. core:data 层：
   - ArticleEntity 单独文件
   - ArticleDao 方法按查询频率排序
   - ArticleRepositoryImpl 先查 Room 缓存，空则拉 Remote
   - DataModule 加 @InstallIn(SingletonComponent) 和 @Singleton

3. feature 层：
   - ViewModel 不要引用具体的 Repository 实现
   - 通过 UseCase 访问数据
```

### AI Response

AI 将代码拆分到独立文件，但仍有问题：

```kotlin
// ArticleRepositoryImpl.kt ⚠️
override suspend fun getArticles(): List<Article> {
    return articleDao.observeAll().map { ... }
        .let { flow -> ... } // ⚠️ 用 collect 封装 Flow 到 suspend
}
```

---

## Round 3：细节修正

### Human Feedback

```
ArticleRepositoryImpl.getArticles() 里的 Flow 处理有问题：
- 不要用 collect{} 把 Flow 转成 suspend 再返回 List
- Room 数据已存在就直接读，不存在就拉取+缓存+读
- count() 判断比 try-catch 更清晰
```

### AI Response（终版）

```kotlin
override suspend fun getArticles(): List<Article> {
    if (articleDao.count() == 0) {
        val remote = remoteDataSource.fetchArticles()
        articleDao.insertAll(remote.map { it.toEntity() })
    }
    return articleDao.observeAll().first().map { it.toDomain() }
}
```

---

## 总结

| 关键翻车 | 发生原因 | 修正 |
|---------|---------|------|
| 跳过接口实现 | AI 直接写实现类 | 要求先定义 domain 接口 |
| 无 Room 缓存 | AI 只按 PRD 表面理解 | 补充缓存策略要求 |
| Hilt 注解缺失 | AI 生成代码优先，配置其次 | 逐模块检查 |
| Flow 误用 | AI 对 Flow 语义理解有限 | 具体指出问题 |
