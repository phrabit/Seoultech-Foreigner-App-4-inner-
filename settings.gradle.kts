pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":app")
include(":unityLibrary")
include("unityLibrary:xrmanifest.androidlib")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs=setOf(File("${project(":unityLibrary").projectDir}/libs"))
        }
        maven(url = "https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
    }
}

rootProject.name = "4_inner"
include(":app")
