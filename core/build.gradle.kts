plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.arfdevs.myproject.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    flavorDimensions += "env"
    productFlavors {
        create("development") {
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "API_KEY", "\"1a513d843961f39b2b18d9a6e03c3e3a\"")
            buildConfigField("String", "Bearer", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxYTUxM2Q4NDM5NjFmMzliMmIxOGQ5YTZlMDNjM2UzYSIsInN1YiI6IjY1ZDgwNGU4MTQ5NTY1MDE3YmY1NDM0OCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.fsMhSnXga0toLwtJltLRLok0DEmNq1TQtAOfv8nvUV0\"")
        }
    }

    buildFeatures {
        buildConfig = true
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    //main
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    //paging
    api("androidx.paging:paging-runtime-ktx:3.2.1")
    api("androidx.paging:paging-common-ktx:3.2.1")

    //coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    //Retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:4.12.0")
    api ("com.squareup.okhttp3:okhttp:4.12.0")

    //room db
    api("androidx.room:room-runtime:2.6.1")
    api("androidx.room:room-ktx:2.6.1")
    api("androidx.room:room-paging:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    //firebase
    api(platform("com.google.firebase:firebase-bom:32.7.2"))
    api("com.google.firebase:firebase-crashlytics-ktx")
    api("com.google.firebase:firebase-analytics-ktx")
    api("com.google.firebase:firebase-auth-ktx")
    api("com.google.firebase:firebase-database-ktx")
    api("com.google.firebase:firebase-config-ktx")


    //koin di
    api("io.insert-koin:koin-core:3.5.3")
    api("io.insert-koin:koin-android:3.5.3")

    //chucker
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")

    //test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}