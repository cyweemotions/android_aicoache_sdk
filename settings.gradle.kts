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
        maven { url = uri( "https://jitpack.io") }
        flatDir {
            dirs("libs")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri( "https://jitpack.io") }

        flatDir {
            dirs ("libs")// 确保正确指定了本地库目录
        }
    }
}

rootProject.name = "AiCoache"
include(":app")
//include(":app:Traininglib")
