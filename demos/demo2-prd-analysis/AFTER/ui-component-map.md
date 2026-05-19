# UI 组件映射

> 由 LLM 将 PRD 中的界面描述映射为 Compose 组件和 ViewModel

## Screen → Component 对照表

### FeedScreen（F1 首页）

```
FeedScreen
├── Scaffold
│   ├── TopAppBar
│   │   ├── Text("BestPractice")
│   │   ├── IconButton(Search)    → onSearchClick
│   │   └── IconButton(Bookmark)  → onBookmarksClick
│   └── Content(when UiState)
│       ├── Loading → CircularProgressIndicator
│       ├── Error   → Text("加载失败: ...")
│       └── Success → LazyColumn
│           └── items(key = { it.id })
│               └── ArticleCard
│                   ├── Text(title, maxLines=2)
│                   ├── IconButton(Bookmark/BookmarkBorder)
│                   ├── Text(summary, maxLines=2)
│                   └── Row(author, date)
└── FeedViewModel
    ├── uiState: StateFlow<FeedUiState>
    ├── articles: List<Article>
    ├── isLoading: Boolean
    ├── error: String?
    └── toggleBookmark(id: String)
```

### ArticleScreen（F2 详情）

```
ArticleScreen
├── Scaffold
│   ├── TopAppBar
│   │   ├── Text(title)
│   │   └── IconButton(ArrowBack)  → onBack
│   └── Content
│       └── Column(scrollable)
│           ├── Text(title, H1)
│           ├── Text("author · date")
│           └── Text(content, markdown)
└── ArticleViewModel
    ├── articleId: String (SavedStateHandle)
    ├── uiState: StateFlow<ArticleUiState>
    ├── article: Article?
    └── isLoading: Boolean
```

### BookmarkScreen（F3 收藏）

```
BookmarkScreen
├── Scaffold
│   ├── TopAppBar
│   │   ├── Text("收藏")
│   │   └── IconButton(ArrowBack) → onBack
│   └── Content
│       ├── Empty → Text("还没有收藏文章")
│       └── LazyColumn
│           └── Card(onClick → ArticleScreen)
│               ├── Text(title)
│               └── Text(summary)
└── BookmarkViewModel
    ├── uiState: StateFlow<BookmarkUiState>
    └── observeBookmarks: Flow 自动更新
```

### SearchScreen（F4 搜索）

```
SearchScreen
├── Scaffold
│   ├── TopAppBar
│   │   ├── OutlinedTextField(搜索文章...)
│   │   └── IconButton(ArrowBack) → onBack
│   └── Content
│       ├── Empty Query → Text("输入关键词搜索")
│       ├── No Results → Text("无匹配结果")
│       └── LazyColumn
│           └── Text(title) + Divider
└── SearchViewModel
    ├── query: String
    ├── results: List<Article>
    └── onQueryChange(query) → 防抖搜索
```

## UiState 三态模式

所有 Screen 统一使用 Loading → Success → Error 三态：

| 状态 | UiState 字段 | UI 表现 |
|------|-------------|---------|
| Loading | `isLoading = true` | CircularProgressIndicator |
| Success | `articles != empty` | LazyColumn |
| Error | `error != null` | 错误文案 |
| Empty | `articles == empty` | 占位文案 |

## ViewModel 模板

```kotlin
@HiltViewModel
class XxxViewModel @Inject constructor(
    private val useCase: XxxUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(XxxUiState())
    val uiState: StateFlow<XxxUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            useCase().collect { data ->
                _uiState.update { it.copy(articles = data, isLoading = false) }
            }
        }
    }
}
```
