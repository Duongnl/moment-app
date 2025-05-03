plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.moment_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.moment_app"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Thêm dependencies cho Retrofit và Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Thêm Lombok dependency
    implementation(libs.lombok)

    // Annotation Processor để sử dụng Lombok
    annotationProcessor(libs.lombok)

    implementation(libs.jackson.annotations)
    implementation(libs.glide)
    annotationProcessor(libs.glideCompiler)
    implementation(libs.circleimageview)

}