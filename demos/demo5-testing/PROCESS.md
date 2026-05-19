# Demo 5 实操过程

> AI 写测试：哪些能自动生成，哪些必须人写

---

## Round 1：初始测试生成

### Human Prompt

```
为 UseCase 生成单元测试
```

### AI Response

```kotlin
// ❌ 测试直接依赖真实 Room 和 FakeRemoteDataSource
@Test
fun testGetArticles() {
    val db = Room.databaseBuilder(...).build()  // ❌ 太慢
    val repo = ArticleRepositoryImpl(db.articleDao(), FakeRemoteDataSource(assets))
    val useCase = GetArticlesUseCase(repo)
    val list = useCase()
    assert(list.isNotEmpty())
}
```

### Human Review

| 问题 | 严重度 |
|------|:---:|
| 测试依赖真实 Room | 🔴 慢且不可靠 |
| 无 Mock/Fake | 🔴 |
| 无异常场景测试 | 🟡 |
| assert 用错了（assert vs assertTrue） | 🟡 |

---

## Round 2：要求 Fake 实现

### Human Feedback

```
测试规范（参照 CLAUDE.md）：
1. 使用 Fake Repository，不要依赖真实的 Room/DataSource
2. 用 runTest + Turbine 测 Flow
3. 每个测试一个 assert 语义
4. 覆盖：正常路径 + 边界条件（空数据、找不到、多次切换）
```

### AI Response

```kotlin
// ✅ 使用 Fake Repository
class FakeArticleRepository : ArticleRepository {
    private val articles = MutableStateFlow(...)
    ...
}

// ✅ 用 runTest
@Test
fun `toggle unchecked to checked`() = runTest {
    useCase("1")
    val article = repo.getArticle("1")
    assertTrue(article?.isBookmarked == true)
}

// ✅ 用 Turbine
useCase("Kotlin").test {
    val results = awaitItem()
    assertEquals(1, results.size)
}
```

---

## 总结

| 人做的 | AI 做的 |
|-------|--------|
| 决定 Fake 策略（不用 MockK 而用手写 Fake） | 生成 Fake 实现代码 |
| 审核测试覆盖范围（缺边界case） | 生成基础场景测试 |
| 确认 Turbine 用法 | 生成 Turbine 测试代码 |

**AI 生成测试的效率估算**：
- 自动生成：7 个测试 / 2 分钟（90%）
- 人工补充：边界条件、并发场景（10%）
