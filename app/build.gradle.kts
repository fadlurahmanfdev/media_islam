import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt") version "1.9.23"
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

val debugPropertiesFile = rootProject.file("signingDebug.properties")
val debugProperties = Properties()
debugProperties.load(FileInputStream(debugPropertiesFile))

val releasePropertiesFile = rootProject.file("signingRelease.properties")
val releaseProperties = Properties()
releaseProperties.load(FileInputStream(releasePropertiesFile))

android {
    namespace = "co.id.fadlurahmanf.mediaislam"
    compileSdk = 34

    defaultConfig {
        applicationId = "co.id.fadlurahmanf.mediaislam"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "EQURAN_BASE_URL", "\"https://equran.id/\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = debugProperties["keyAlias"] as String
            keyPassword = debugProperties["keyPassword"] as String
            storeFile = file(debugProperties["storeFilePath"] as String)
            storePassword = debugProperties["storePassword"] as String
        }
        create("release") {
            keyAlias = releaseProperties["keyAlias"] as String
            keyPassword = releaseProperties["keyPassword"] as String
            storeFile = file(releaseProperties["storeFilePath"] as String)
            storePassword = releaseProperties["storePassword"] as String
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    flavorDimensions.add("environment")

    productFlavors {
        create("fake") {
            dimension = "environment"
            applicationIdSuffix = ".fake"
            resValue("string", "app_name", "Media Islam Fake")
        }

        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            resValue("string", "app_name", "Media Islam Dev")
        }

        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            resValue("string", "app_name", "Media Islam Staging")
        }

        create("prod") {
            dimension = "environment"
            resValue("string", "app_name", "Media Islam")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // dependency injection
    implementation("com.google.dagger:dagger:2.51.1")
    kapt("com.google.dagger:dagger-compiler:2.51.1")

    // asynchronus
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // view-model
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // chucker
    implementation("com.github.chuckerteam.chucker:library:4.0.0")

    // shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
}