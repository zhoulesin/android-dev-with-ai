# Demo 1 实操过程

> 真实 AI 交互记录：CLAUDE.md → 多模块项目骨架，4 轮迭代

---

## 准备

将 `BEFORE/CLAUDE.md` 和 `BEFORE/PROMPT.md` 发给 AI。

---

## Round 1：AI 初版生成

### Human Prompt

```
请根据 CLAUDE.md 创建 Gradle 多模块项目骨架，结构如下：
:app / :core:domain / :core:data / :core:designsystem
:feature:feed / :feature:article / :feature:bookmark / :feature:search

要求：Version Catalog / Hilt / KSP / ktlint / minSdk 26
```

### AI Response（初版）

```kotlin
// core/domain/build.gradle.kts (AI 错误版本)
plugins {
    alias(libs.plugins.android.library)  // ❌ domain 不应有 Android 插件
    alias(libs.plugins.kotlin.android)
}
dependencies {
    implementation("androidx.room:room-runtime:2.6.1")  // ❌ domain 不能依赖 Android
}

// feature/feed/build.gradle.kts
dependencies {
    implementation(project(":core:data"))  // ❌ 违反 feature 不依赖 data 规则
    implementation(project(":core:domain"))  // ❌ 用 implementation 而非 api
}

// app/build.gradle.kts
dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")  // ❌ 版本号硬编码
}
```

### Human Review

| 检查项 | 结果 | 问题 |
|--------|:---:|------|
| domain 模块纯 JVM | ❌ | 用了 `android-library` 插件，依赖了 Room |
| feature 不依赖 data | ❌ | `feature:feed` 直接依赖 `:core:data` |
| 版本仅 catalog | ❌ | Retrofit 版本硬编码 |
| ktlint 配置 | ❌ | 缺失 |
| 模块暴露 `api` | ❌ | feature 用 `implementation` 导致类型无法传递 |
| Hilt @Module | ⚠️ | 模块创建了但绑定关系写反 |

---

## Round 2：修正依赖和架构

### Human Feedback

```
问题清单：
1. core:domain 是纯 JVM 模块，移除 android-library 插件和 Android 依赖
2. feature:* 不能依赖 core:data，只能依赖 core:domain + core:designsystem
3. 所有版本号必须在 gradle/libs.versions.toml
4. 模块间暴露类型用 api，内部用 implementation
5. 加上 ktlint 配置
6. Hilt @Module 需要在 core:data
```

### AI Response（修正版）

```kotlin
// core/domain/build.gradle.kts ✅
plugins {
    alias(libs.plugins.kotlin.jvm)
}
kotlin { jvmToolchain(17) }

// feature/feed/build.gradle.kts ✅
dependencies {
    api(project(":core:domain"))       // ✅ api 暴露类型
    api(project(":core:designsystem"))  // ✅ 无 core:data 依赖
}

// 但 ktlint 配置在子模块重复写了多次 ❌
```

### Human Review

| 检查项 | 结果 | 问题 |
|--------|:---:|------|
| domain 纯 JVM | ✅ | |
| feature 不依赖 data | ✅ | |
| ktlint | ⚠️ | 每个子模块都写了 `id("org.jlleitschuh.gradle.ktlint")`，应该在 root 用 `subprojects` 统一配置 |
| Hilt 依赖 | ❌ | `core:data` 缺少 `implementation(libs.hilt.android)` 和 `ksp(libs.hilt.compiler)` |
| composeOptions | ❌ | 还留着旧的 `composeOptions { kotlinCompilerExtensionVersion }` |

---

## Round 3：修复 Hilt 和 Compose 配置

### Human Feedback

```
1. ktlint 应该在 root build.gradle.kts 用 subprojects {} 统一配置
2. core:data 需要 Hilt 依赖（implementation(libs.hilt.android) + ksp(libs.hilt.compiler)）
3. Kotlin 2.0 不需要 composeOptions，改成 plugins { alias(libs.plugins.kotlin.compose) }
4. JVM toolchain 17 要加到所有模块
5. core:domain 的依赖用 api 暴露 model 类型给 feature
```

### AI Response（接近最终版）

```kotlin
// root build.gradle.kts
subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

// core/data/build.gradle.kts
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}
dependencies {
    api(project(":core:domain"))
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.room.runtime)
    ...
}
```

### Human Review

| 检查项 | 结果 | 问题 |
|--------|:---:|------|
| ktlint 统一配置 | ✅ | |
| Hilt 完整 | ✅ | |
| Compose 插件迁移 | ✅ | |
| JVM toolchain | ✅ | |
| api 暴露 | ✅ | |
| Feature 模块 | ⚠️ | FeedScreen 里 import 了 `FakeRemoteDataSource`（来自 core:data），虽然编译期不依赖，但 import 路径不对 |

---

## Round 4：最终审查

### Human Feedback

```
最后一个问题：FeedScreen.kt 里有 import com.example...core.data.FakeRemoteDataSource。
虽然 feature:feed 的 build.gradle.kts 没有依赖 core:data，但这个 import 不应该存在。
把具体实现细节移到 ViewModel 里，Screen 只依赖 UseCase。
```

### AI Response（终版）

AI 移除了 Screen 中对 data 层的直接引用，改为全部通过 UseCase。

### 验证

```
./gradlew :app:assembleDebug → BUILD SUCCESSFUL
```

---

## 总结

| 轮次 | 耗时 | 修正内容 |
|:---:|:---:|------|
| R1 | 3min | AI 生成初版骨架 |
| R2 | 5min 审 + 2min | 修正模块依赖、架构分层 |
| R3 | 3min 审 + 2min | 修正 Hilt、Compose、ktlint |
| R4 | 2min 审 + 1min | 清理跨层 import |

**总耗时 ~16 分钟**（vs 手写 ~2 小时）。关键是每轮 Prompt 明确指出了问题和修正方向，AI 才能精准修复。

**核心教训**：
1. CLAUDE.md 写清楚了规则，但 AI 仍会违反 → 需要人审
2. 第一次 Prompt 越具体越好（列出模块名、技术栈、禁止项）
3. 反馈要具体（给出错误代码和正确代码的对比）
4. 每次修一个维度的问题，不要一次提太多
