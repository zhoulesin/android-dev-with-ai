# 四层模块引用溯源（第 3 章 · 场景二扩展）

考的是工具能否在 **app / feature / domain / data** 分层下找到**真实调用链**，而不是只改 `core:data` 里打开的文件。

## 模块结构

```
:app
:feature:profile          → ViewModel，依赖 domain
:core:domain              → ProfileRepository 接口
:core:data                → ProfileRepositoryImpl（考题：改接口签名）
```

## 任务

将 `ProfileRepository.loadProfile()` 的返回类型从 `Profile` 改为 `Result<Profile>`，更新 **domain 接口、data 实现、feature 内全部调用方**，且 feature **不得** `import` data 层实现类。

## 验收

- [ ] 15+ 处引用全部更新（含 Preview / Test）
- [ ] 无 `import ...data...ProfileRepositoryImpl` 出现在 feature
- [ ] 跑 `./gradlew :feature:profile:compileDebugKotlin` 通过

## 与场景二关系

第 3 章「场景二」原任务为 `core:network` 的 `ApiService` 变更；本 fixture 强调 **Clean 分层下的纵向引用**，建议与场景二**同一轮工具评测**一并执行。
