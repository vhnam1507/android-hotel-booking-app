plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "project.tttn.vuhoangnam_20h1120232_hotelbookingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "project.tttn.vuhoangnam_20h1120232_hotelbookingapp"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets {
        getByName("main") {
            java {
                srcDirs("src\\main\\java", "src\\main\\java\\customer")
            }
        }
    }
    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.google.android.material:material:1.12.0")
}