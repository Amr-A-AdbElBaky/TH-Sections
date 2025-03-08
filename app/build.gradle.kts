plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.dagger.hilt)
    kotlin("kapt")
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.example.thmanyah"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.thmanyah"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://api-v2-b2sit6oh3a-uc.a.run.app/\"")
        buildConfigField("String", "HOME_SECRIONS_END_POINT", "\"home_sections\"")

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes.addAll(listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
            ))
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    /*    implementation(libs.material3)
        implementation(libs.androidx.material3.icons.core)
        implementation(libs.androidx.material3.icons.extended)*/
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.google.dagger.hilt)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit.group)
    implementation(libs.bundles.coil.group)




    debugImplementation(libs.androidx.ui.tooling)

    testImplementation(libs.bundles.testing.group)

    // Compose Testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    // AndroidX Test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)

    // Hilt Testing
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.google.dagger.hilt.compiler)

    androidTestImplementation(libs.mockwebserver)

    // Mockito for Android
    // androidTestImplementation(libs.mockito.android)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.android)





    // Truth assertion library
    androidTestImplementation(libs.truth)


}