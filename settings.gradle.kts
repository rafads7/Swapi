pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Swapi"
include(":app")
include(":feature")
include(":feature:planets")
include(":feature:planets:domain")
include(":feature:planets:data")
include(":feature:planets:lib")
include(":feature:planets:presentation")
include(":core")
include(":core:base")
include(":core:base:presentation")
include(":core:database")
include(":core:network")
include(":core:base:common")
include(":core:common")
include(":core:ui")
