plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.emotify"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.emotify"
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
        buildFeatures {
            viewBinding = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.location)
    implementation(libs.core.ktx)
    implementation(libs.androidx.junit.ktx)



    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")


    val camerax_version="1.2.2";
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-video:${camerax_version}")
    implementation ("androidx.camera:camera-view:${camerax_version}")
    implementation ("androidx.camera:camera-extensions:${camerax_version}")

    implementation ("androidx.fragment:fragment-ktx:1.8.5")
    implementation (libs.lifecycle.viewmodel.ktx)

    implementation(libs.moshi)

    implementation (libs.okhttp)

    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    implementation (libs.glide)

   // implementation (libs.mpandroidchart)
   // implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v251)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp.v492)
    implementation (libs.play.services.auth)
    implementation ("com.google.android.gms:play-services-auth:21.3.0")

    testImplementation (libs.junit)
    testImplementation (libs.mockito.core.v570)
    testImplementation (libs.mockito.kotlin)
    testImplementation (libs.kotlinx.coroutines.test.v164)
    testImplementation (libs.androidx.core)
    testImplementation (libs.robolectric)


    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("org.mockito:mockito-android:5.2.0")


}

