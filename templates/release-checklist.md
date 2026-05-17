# Android 发包前检查清单（Release / PR）

> 对应文档：[第 7 章 §7.0](../docs/07-接入项目全流程.md)  
> 考题参考：[scenario-01](../fixtures/android-benchmark/scenario-01-gradle-duplicate-class/) · [scenario-03](../fixtures/android-benchmark/scenario-03-version-catalog/) · [scenario-07](../fixtures/android-benchmark/scenario-07-manifest-merge/)

复制下方 **「PR 描述区」** 到 Pull Request 或发布工单；勾选须由 **Reviewer / Release 负责人** 完成，AI 只可生成初稿。

---

## PR 描述区（复制从这里开始）

```markdown
## 发布信息

- 版本号：
- 影响模块：
- 是否含 AI 生成/修改的 Gradle 或 Manifest：是 / 否

## 发包前检查（Release）

### Gradle / 依赖

- [ ] 版本号仅在 `gradle/libs.versions.toml`（或团队 catalog），子模块无散落硬编码
- [ ] 暴露类型用 `api`，内部实现用 `implementation`
- [ ] `./gradlew :app:assembleRelease`（或受影响模块 `compileReleaseKotlin`）已通过
- [ ] 依赖有变更时已查传递依赖，无 Duplicate class

### Manifest

- [ ] 已查看 **Merged Manifest**（Release），`exported` / 权限 / SDK 组件符合预期
- [ ] Debug 配置未泄漏到 Release
- [ ] 未为合并冲突删除第三方 SDK 的 permission / service / provider

### AI 参与变更（若适用）

- [ ] `build.gradle.kts` / `libs.versions.toml` 已人工 Diff
- [ ] 新增 `sealed` 子类对应 `when` 分支已穷尽
- [ ] 提交中无 `local.properties`、`google-services.json`、密钥

### 灰度与回滚（上线工单填写）

- [ ] 灰度比例与观察窗口已确认
- [ ] 回滚条件与负责人已写明

## 验证命令（按需粘贴实际执行的）

\`\`\`bash
./gradlew :app:assembleRelease
# ./gradlew :app:processReleaseMainManifest
\`\`\`
```

---

## 完整说明（不放进 PR 也可）

### Gradle / 依赖

| 检查项 | 说明 |
|--------|------|
| Version Catalog | 单一版本源，避免子模块 `platform("...bom:...")` 硬编码 |
| api / implementation | 公开 API 泄漏类型时必须 `api`（考卷 B） |
| 编译 | 至少 Release 变体或受影响 feature 模块 compile |
| 传递依赖 | `dependencies` / `dependencyInsight`，Room 等注意 Duplicate class（考卷 A） |

### Manifest

| 检查项 | 说明 |
|--------|------|
| Merged Manifest | Android Studio：Analyze APK；或构建产物 `processReleaseMainManifest` |
| exported | Release 敏感 Activity 应为 `false`；Debug 用独立 manifest / `tools:replace`（第 8 章坑十一） |
| SDK 节点 | 禁止为消冲突删除 library 声明的组件 |

### AI 加查

| 检查项 | 说明 |
|--------|------|
| Gradle Diff | AI 常改 catalog 与多模块脚本，必须人审 |
| sealed / when | 新增 Action 须同步 ViewModel / UI（考卷 D） |
| 密钥 | 见 [`templates/rules/CLAUDE.md`](./rules/CLAUDE.md) 禁止项 |

### 建议 CI 对齐（可选）

```yaml
# 示例：PR 必跑（按项目裁剪）
# - ./gradlew :app:assembleRelease
# - ./gradlew detekt ktlintCheck
# - secret scan (detect-secrets / gitleaks)
```

---

## 维护

- 团队有自定义 flavor / 动态模块时，在 PR 模板中追加对应模块的 `compile` 任务。
- 每季度与 [第 11 章](../docs/11-持续更新.md) 一起复核 AGP / BOM 相关条目。
