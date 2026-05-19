# Demo 2 实操过程

> AI 解析 PRD 的 3 轮迭代：从模糊需求到结构化技术方案

---

## Round 1：直接喂 PRD

### Human Prompt

```
你是 Tech Lead，分析下面的 PRD，输出技术方案。
```

### AI Response（初版）

```markdown
我认为这个项目需要以下功能：
- 文章列表（主要功能）
- 文章详情
- 收藏
- 搜索

技术栈用 Compose + Hilt + Room 即可。不需要太多模块。
```

### Human Review

| 检查项 | 结果 | 问题 |
|--------|:---:|------|
| 结构化程度 | ❌ | 自由文本，无标准化格式 |
| 任务可执行性 | ❌ | "用 Compose+Hilt" 太模糊，无法分配任务 |
| UseCase 拆分 | ❌ | 完全没有，直接跳到"写代码" |
| 数据模型 | ❌ | 未定义 Article 结构 |
| 风险评估 | ❌ | 未提及任何风险 |

**结论**：没有结构化 Prompt，AI 产出的就是闲聊级别的回复，无法指导开发。

---

## Round 2：给结构化 Prompt

### Human Feedback + 新 Prompt

```
你是 Android Tech Lead，请按以下模板输出分析结果：

1. 模块划分（每个模块的职责、对应 PRD 功能点）
2. 领域模型（数据类定义、字段说明）
3. Repository 接口（方法签名 + 说明）
4. UseCase 清单（每个功能的 UseCase）
5. 数据流（从数据源到 UI 的路径）
6. UI 状态设计（每个 Screen 的 UiState）
7. 开发任务清单（带优先级和依赖）
```

### AI Response（结构化版）

```markdown
## 模块划分
| 模块 | 职责 |
|------|------|
| feature:feed | 首页列表 |
| feature:article | 详情 |
...

## 领域模型
Article(id, title, summary, content, author, date, tags, bookmark)

## UseCase
GetArticles, GetArticle, SearchArticles, ToggleBookmark
```

### Human Review

| 检查项 | 结果 | 问题 |
|--------|:---:|------|
| 结构完整 | ✅ | 按要求输出了 7 个维度 |
| 模块划分 | ⚠️ | 分类太粗，未区分 observe 和 suspend 方法 |
| 数据流 | ❌ | 只说了"从 API 读数据"，没提 Room 缓存 |
| UseCase | ⚠️ | 缺少 ObserveArticlesUseCase（列表需要 Flow 自动更新） |
| 任务清单 | ❌ | 只列了"写代码"一个任务 |

**结论**：结构化 Prompt 有效，但 AI 默认忽略了一些关键架构细节。

---

## Round 3：补充架构约束

### Human Feedback

```
补充要求：
1. 区分 observe（Flow 持续观察）和 suspend（一次性操作）
2. 数据流要包含 Room 缓存层：Remote → Room → Repository → UseCase → ViewModel
3. 任务清单至少拆到 15 项，标注 T-shirt sizing
4. 加上 ADR 架构决策记录
5. 考虑离线场景：首次加载缓存，后续读缓存
```

### AI Response（终版）

AI 补全了完整的数据流设计、ADR 决策、15+ 项任务清单。

详见 [`AFTER/`](./AFTER/) 中的 6 份最终文档。

### 验证

| 检查项 | 结果 |
|--------|:---:|
| PRD 8 个用户故事全部覆盖 | ✅ |
| 数据流含 Remote → Room → Repo → UseCase → VM → UI | ✅ |
| ADR 记录了 3 个架构决策 | ✅ |
| 任务清单 21 项，含规模和依赖 | ✅ |
| observe/suspend 分类明确 | ✅ |

---

## 总结

| 轮次 | 问题 | 教训 |
|:---:|------|------|
| R1 | AI 输出太模糊 | **必须给结构化模板**，否则 AI 自由发挥 |
| R2 | 架构细节缺失 | **模板只给了形式，没给架构约束** |
| R3 | 补齐后达标 | 人审时追问"数据怎么流动？缓存怎么做？" |

**核心教训**：
1. 不给模板 = AI 闲聊；给模板 = AI 填表；给模板+约束 = AI 专业输出
2. PRD 解析的第一步应该是"让 AI 提问"，而不是"让 AI 回答"
3. 好的 PRD 应该有编号（US-01），方便后续任务追踪
