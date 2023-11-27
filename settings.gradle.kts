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
//project(":unityLibrary").projectDir= File("\\unityLibrary")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs=setOf(File("${project(":unityLibrary").projectDir}/libs"))
        }
    }
}

rootProject.name = "4_inner"
include(":app")
