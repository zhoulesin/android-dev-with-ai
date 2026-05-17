# 考卷 D：`sealed` + `when` 穷尽

## 现象

`LoginAction` 新增 `ShowCaptcha`，`LoginViewModel.onAction` 的 `when` 未处理该分支；且使用了 `else` 分支掩盖遗漏。

## 验收标准

| 维度 | 通过条件 |
|------|----------|
| 编译/逻辑 | 为 `ShowCaptcha` 增加分支，移除不当 `else`（或改为编译期穷尽） |
| 状态 | `UiState` 有 `showCaptcha` 或等价字段，且与 Submit 不冲突 |
| 风格 | 不引入 `GlobalScope`；事件用 `viewModelScope` |

## 常见不及格答案

- 保留 `else -> {}` 静默吞掉新事件
- 只改 UI 不改 ViewModel
