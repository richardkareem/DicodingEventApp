plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //safe args
    id("androidx.navigation.safeargs")
    //ksp
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.org.dicodingeventapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.org.dicodingeventapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        viewBinding = true
    }
}

dependencies {
    //WORKMANAGER
    implementation(libs.androidx.work.runtime)
    //DATASTORE
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    //
    implementation(libs.protolite.well.known.types)
    //ROOM DB
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    //FOTO
    implementation(libs.glide)
    //https req
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //DEBUG API
    implementation(libs.logging.interceptor)
    //KTX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}