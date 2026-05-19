# 开发任务看板

> 由 LLM 根据功能拆解自动生成，含优先级、预估、依赖关系

## 任务清单

| # | 任务 | 模块 | 故事 | 规模 | 依赖 | 状态 |
|---|------|------|------|:---:|------|:---:|
| T01 | 定义 Article 数据类 | domain | — | XS | — | ✅ |
| T02 | 定义 ArticleRepository 接口 | domain | — | XS | T01 | ✅ |
| T03 | 实现 GetArticlesUseCase | domain | US-01 | XS | T02 | ✅ |
| T04 | 实现 GetArticleUseCase | domain | US-02 | XS | T02 | ✅ |
| T05 | 实现 SearchArticlesUseCase | domain | US-07 | XS | T02 | ✅ |
| T06 | 实现 ToggleBookmarkUseCase | domain | US-04 | XS | T02 | ✅ |
| T07 | 实现 ObserveArticlesUseCase | domain | US-01 | XS | T02 | ✅ |
| T08 | 实现 ObserveBookmarksUseCase | domain | US-05 | XS | T02 | ✅ |
| T09 | 创建 ArticleEntity | data | — | S | T01 | ✅ |
| T10 | 创建 ArticleDao | data | — | S | T09 | ✅ |
| T11 | 创建 ArticleDatabase | data | — | XS | T10 | ✅ |
| T12 | 实现 FakeRemoteDataSource | data | US-01 | S | — | ✅ |
| T13 | 实现 ArticleRepositoryImpl | data | US-01 | M | T10,T12 | ✅ |
| T14 | 编写 DataModule（Hilt） | data | — | S | T13 | ✅ |
| T15 | 创建 articles.json | app/assets | — | XS | — | ✅ |
| T16 | FeedScreen + FeedViewModel | feed | US-01,03,04 | M | T14,T15 | ✅ |
| T17 | ArticleScreen + ArticleViewModel | article | US-02 | S | T14 | ✅ |
| T18 | BookmarkScreen + BookmarkViewModel | bookmark | US-05,06 | S | T14 | ✅ |
| T19 | SearchScreen + SearchViewModel | search | US-07,08 | M | T14 | ✅ |
| T20 | MainActivity NavHost 集成 | app | — | S | T16-19 | ✅ |
| T21 | 暗色模式适配 | designsystem | — | XS | — | ✅ |

**规模说明**：XS(<1h) S(1-2h) M(2-4h) L(1-2d)

## 看板

```
Backlog                In Progress           Done
                                           ┌─────┐
                                           │T01  │
                                           │T02  │
                                           │ ... │
                                           │T21  │
                                           └─────┘
```

## 关键路径

```
T01 → T02 → T09 → T10 → T11 → T13 → T14 → T16 → T20
                                      ↘ T17 ↗
                                      ↘ T18 ↗
                                      ↘ T19 ↗
```

T01-T14（domain + data）是阻塞项，必须最先完成。
