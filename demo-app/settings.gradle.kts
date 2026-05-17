pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AiAndroidWorkflowDemo"

include(":app")

include(":core:common")
include(":core:model")
include(":core:domain")
include(":core:data")
include(":core:designsystem")

include(":feature:hub")
include(":feature:playbook")
include(":feature:benchmark")
include(":feature:release")
