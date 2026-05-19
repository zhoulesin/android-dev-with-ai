# 数据模型设计

> 由 LLM 从 PRD 功能规格提取数据实体

## 核心实体

### Article（文章）

```kotlin
data class Article(
    val id: String,           // 唯一标识，如 "1", "2"
    val title: String,        // 文章标题
    val summary: String,      // 摘要（列表页展示）
    val content: String,      // 正文 Markdown
    val author: String,       // 作者名
    val publishDate: String,  // 发布日期 "yyyy-MM-dd"
    val tags: List<String>,   // 标签列表 ["Kotlin", "Compose"]
    val isBookmarked: Boolean // 收藏状态
)
```

**字段约束**：
- `id`：非空唯一，从 articles.json 读取
- `title`：非空，最大 200 字符
- `content`：可含 Markdown 标记，纯文本展示
- `tags`：可为空列表
- `isBookmarked`：默认 false

### ArticleEntity（Room 持久化）

```kotlin
@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val author: String,
    val publishDate: String,
    val tags: String,          // 逗号分隔 "Kotlin,Compose"
    val isBookmarked: Boolean
)
```

**映射关系**：
- `Article.tags` ↔ `ArticleEntity.tags`：`List<String>` ↔ `String`（逗号拼接）
- Room 不支持 `List<String>` 直接存储，需手动转换

## ER 关系

```
┌─────────────┐
│   Article   │
├─────────────┤
│ PK id       │
│ title       │
│ summary     │
│ content     │
│ author      │
│ publishDate │
│ tags        │
│ isBookmarked│
└─────────────┘
    │
    │  (仅 Room)
    ▼
┌─────────────────┐
│  ArticleEntity   │
│  (Room Table)    │
└─────────────────┘
```

## 数据来源

```
articles.json (assets)
    │  org.json 解析
    ▼
FakeRemoteDataSource.fetchArticles()
    │  delay(400ms) 模拟网络
    ▼
List<Article>
    │  toEntity() 映射
    ▼
Room Database
    │  Flow 观察
    ▼
UI (LazyColumn)
```

## articles.json 格式规范

```json
[
  {
    "id": "1",
    "title": "文章标题",
    "summary": "文章摘要",
    "content": "# Markdown 正文",
    "author": "作者",
    "publishDate": "2025-05-18",
    "tags": ["Kotlin", "Compose"]
  }
]
```

新增文章直接追加到 JSON 数组末尾，AI 会自动识别并加载。
