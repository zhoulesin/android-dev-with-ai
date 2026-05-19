# Demo 2 Prompt：PRD 解读与任务拆解

你是一位 Android Tech Lead，需要根据 PRD 文档完成需求分析和任务拆解。

## 输入
- `PRD.md`（同上目录）
- 项目是基于 demo1 产出的 CLADUE.md 项目骨架

## 任务

1. **模块划分**：根据 PRD 确定需要哪些 feature 模块

2. **领域模型设计**：
   - 列出核心数据类（Article 等）及其字段
   - 定义 Repository 接口和主要方法

3. **UseCase 拆分**：
   - 每个功能对应哪些 UseCase
   - UseCase 的输入输出

4. **数据流设计**：
   - 数据从哪里来（FakeRemoteDataSource → Room → Repository → UseCase → ViewModel）
   - Flow 的使用场景（observe vs suspend）

5. **UI 状态设计**：
   - 每个界面需要哪些 UiState
   - Loading / Error / Empty 三态处理

6. **开发任务清单**：
   - 按模块拆分可执行的开发任务
   - 标注优先级和依赖关系

## 产出格式

```markdown
## 模块划分
## 领域模型
## UseCase 清单
## 数据流
## UI 状态
## 任务清单（带优先级）
```
