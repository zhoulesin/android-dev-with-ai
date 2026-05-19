# 发布检查结果

> 对照 release-checklist 逐项审计

## Gradle / 依赖

- [x] 版本号仅在 `gradle/libs.versions.toml`，子模块无散落硬编码
- [x] 模块间暴露类型用 `api`（`domain` → `data`），内部用 `implementation`
- [x] `./gradlew :app:assembleDebug` 已通过
- [x] 依赖无 Duplicate class 冲突

## Manifest

- [x] `exported=true` 仅 MainActivity（LAUNCHER），合理
- [x] Debug 配置未泄漏到 Release（单 Manifest 无额外配置）
- [x] 无第三方 SDK manifest 合并冲突

## AI 参与变更

- [x] `build.gradle.kts` / `libs.versions.toml` 已人工 Diff
- [x] UiState sealed class when 分支已穷尽
- [x] 提交中无 `local.properties`、`google-services.json`、密钥（已 gitignore）

## 灰度与回滚

- [~] 灰度比例与观察窗口：MVP 阶段暂不适用
- [~] 回滚条件：MVP 阶段暂不适用

## 总结

| 类别 | 通过 | 待改进 | 阻塞 |
|------|:---:|:---:|:---:|
| Gradle/依赖 | 4 | 0 | 0 |
| Manifest | 3 | 0 | 0 |
| AI 变更 | 3 | 0 | 0 |
| 灰度回滚 | 0 | 2 | 0 |

**结论**：可发布 MVP 版本。

---

## PR 描述模板（AI 生成）

```markdown
## 发布信息
- 版本号：1.0.0
- 影响模块：全模块（首次发布）
- 是否含 AI 生成/修改的 Gradle：否

## 发包前检查
### Gradle / 依赖
- [x] 版本号仅在 gradle/libs.versions.toml
- [x] 暴露类型用 api，内部实现用 implementation
- [x] ./gradlew :app:assembleDebug 已通过
- [x] 依赖有变更时已查传递依赖，无 Duplicate class
### Manifest
- [x] 已查看 Merged Manifest
- [x] Debug 配置未泄漏到 Release
- [x] 未为合并冲突删除第三方 SDK 组件
### AI 参与变更
- [x] build.gradle.kts / libs.versions.toml 已人工 Diff
- [x] 新增 sealed 子类对应 when 分支已穷尽
- [x] 提交中无密钥

## 验证命令
./gradlew :app:assembleDebug
```
