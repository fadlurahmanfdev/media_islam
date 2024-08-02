import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt") version "1.9.23"
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
}

val debugPropertiesFile = rootProject.file("signingDebug.properties")
val debugProperties = Properties()
debugProperties.load(FileInputStream(debugPropertiesFile))

val releasePropertiesFile = rootProject.file("signingRelease.properties")
val releaseProperties = Properties()
releaseProperties.load(FileInputStream(releasePropertiesFile))

val firebaseAppDistributionDevFile = rootProject.file("firebaseAppDistributionDev.properties")
val firebaseAppDistributionDev = Properties()
firebaseAppDistributionDev.load(FileInputStream(firebaseAppDistributionDevFile))

android {
    namespace = "co.id.fadlurahmanf.mediaislam"
    compileSdk = 34

    defaultConfig {
        applicationId = "co.id.fadlurahmanf.mediaislam"
        minSdk = 21
        targetSdk = 34
        versionCode = 2
        versionName = "0.0.1 ($versionCode)"

        buildConfigField("String", "EQURAN_BASE_URL", "\"https://equran.id/api/\"")
        buildConfigField("String", "ALADHAN_BASE_URL", "\"http://api.aladhan.com/\"")
        buildConfigField(
            "String",
            "ARTIKEL_ISLAM_BASE_URL",
            "\"https://artikel-islam.netlify.app//\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = (debugProperties["keyAlias"] as String?) ?: ""
            keyPassword = (debugProperties["keyPassword"] as String?) ?: ""
            storeFile = file((debugProperties["storeFilePath"] as String?) ?: "")
            storePassword = (debugProperties["storePassword"] as String?) ?: ""
        }
        create("release") {
            keyAlias = (releaseProperties["keyAlias"] as String?) ?: ""
            keyPassword = (releaseProperties["keyPassword"] as String?) ?: ""
            storeFile = file((releaseProperties["storeFilePath"] as String?) ?: "")
            storePassword = (releaseProperties["storePassword"] as String?) ?: ""
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
            firebaseAppDistribution {
                artifactType = "APK"
                appId = (firebaseAppDistributionDev["firebaseAppId"] as String?) ?: ""
                groups = (firebaseAppDistributionDev["firebaseDistributionGroups"] as String?) ?: ""
                serviceCredentialsFile =
                    (firebaseAppDistributionDev["googleCredentialFilePath"] as String?) ?: ""
            }
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

    // reactive rxjava3
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    // view-model
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // network
    implementation("com.github.fadlurahmanfdev:kotlin_feature_network:v0.0.5-beta")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.11.0")

    // alarm
    implementation("com.github.fadlurahmanfdev:kotlin_feature_alarm:v0.0.11-beta")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-config")

    // ui related
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // platform
    implementation("com.github.fadlurahmanfdev:kotlin_core_platform:v0.0.9-beta")

    // media player
    implementation("com.github.fadlurahmanfdev:kotlin_feature_media_player:v0.0.10-beta")

    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-rxjava2:$roomVersion")
    implementation("androidx.room:room-rxjava3:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    //paging
    val pagingVersion = "3.3.1"
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation("androidx.paging:paging-rxjava3:$pagingVersion")

    val workVersion = "2.9.0"
    implementation("androidx.work:work-rxjava2:$workVersion")
//    implementation("androidx.work:work-runtime-ktx:$workVersion")
}