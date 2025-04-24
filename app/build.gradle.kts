import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("com.android.application")
  id("kotlin-android")
  id("com.mikepenz.aboutlibraries.plugin")
}

android {
  namespace = "com.sparkleside"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.sparkleside"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    vectorDrawables.useSupportLibrary = true
    resConfigs("bn-rBD", "de", "ar-rTN", "es-rES", "fr-rTN", "in-rID", "pt-rBR", "pt-rPT", "zh-rCN")
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    isCoreLibraryDesugaringEnabled = true
  }

  flavorDimensions.add("cpuArch")
  productFlavors {
    create("arch_arm32") {
      dimension = "cpuArch"
      versionCode = 4 * 1000 + 1
      versionNameSuffix = "-arm32"
    }
    create("arch_arm64") {
      dimension = "cpuArch"
      versionCode = 3 * 1000 + 1
      versionNameSuffix = "-arm64"
    }
  }

  sourceSets {
    arch_arm32 {
      assets.srcDir("arch_arm32/assets")
    }

    arch_arm64 {
      assets.srcDir("arch_arm64/assets")
    }
  }

  buildFeatures {
    viewBinding = true
    buildConfig = true
  }

  splits {
    abi {
      isEnable = true
      reset()
      include("arm64-v8a", "armeabi-v7a")
      isUniversalApk = false
    }
  }

  signingConfigs {
    create("release") {
      storeFile = file("release_key.jks")
      storePassword = "release_temp"
      keyAlias = "release_temp"
      keyPassword = "release_temp"
    }
    getByName("debug") {
      storeFile = file("testkey.keystore")
      storePassword = "testkey"
      keyAlias = "testkey"
      keyPassword = "testkey"
    }
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      isShrinkResources = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_17
  }
}

dependencies {
  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.appcompat:appcompat:1.6.1")

  implementation("com.google.android.material:material:1.13.0-alpha08")

  implementation(platform("io.github.Rosemoe.sora-editor:bom:0.23.4"))
  implementation("io.github.Rosemoe.sora-editor:editor:0.23.4")
  implementation("com.github.bumptech.glide:glide:4.16.0")
  implementation("androidx.activity:activity:1.6.0-alpha05")

  implementation("com.blankj:utilcodex:1.31.1")
  implementation("nl.dionsegijn:konfetti-xml:2.0.4")
  implementation("com.mikepenz:aboutlibraries:11.2.3")

  implementation("org.nanohttpd:nanohttpd:2.3.1")

  implementation("io.noties.markwon:core:4.6.2")
  implementation("io.noties.markwon:html:4.6.2")
  implementation("io.noties.markwon:image:4.6.2")

  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")

  // termux
  implementation("com.github.termux.termux-app:terminal-view:0.118.1")
  implementation("com.github.termux.termux-app:terminal-emulator:0.118.1")

  implementation(project(":peekandpop"))
  implementation(project(":maskable"))
  implementation(project(":fastui"))
  implementation(project(":filetree"))
  implementation(project(":java-compiler"))
}
