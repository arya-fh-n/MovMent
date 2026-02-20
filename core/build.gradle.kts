plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("com.google.firebase.crashlytics")
    id("jacoco")
}

private val coverageExclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*"
)

android {
    namespace = "com.arfdevs.myproject.core"
    compileSdk = 35

    configure<JacocoPluginExtension> {
        toolVersion = "0.8.10"
    }

    val jacocoTestReport = tasks.create("jacocoTestReport")

    androidComponents.onVariants { variant ->
        val testTaskName = "test${variant.name.capitalize()}UnitTest"

        val reportTask =
            tasks.register("jacoco${testTaskName.capitalize()}Report", JacocoReport::class) {
                dependsOn(testTaskName)

                reports {
                    html.required.set(true)
                }

                classDirectories.setFrom(
                    fileTree("$buildDir/tmp/kotlin-classes/${variant.name}") {
                        exclude(coverageExclusions)
                    }
                )

                sourceDirectories.setFrom(
                    files("$projectDir/src/main/java")
                )
                executionData.setFrom(file("$buildDir/jacoco/$testTaskName.exec"))
            }

        jacocoTestReport.dependsOn(reportTask)
    }

    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }


    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    flavorDimensions += "env"
    productFlavors {
        create("development") {
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "API_KEY", "your-api-key")
            buildConfigField(
                "String",
                "Bearer",
                "your-api-access-token"
            )
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    //main
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")

    //paging
    api("androidx.paging:paging-runtime-ktx:3.3.6")
    api("androidx.paging:paging-common-ktx:3.3.6")

    //coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    //Retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:4.12.0")
    api("com.squareup.okhttp3:okhttp:4.12.0")

    //room db
    api("androidx.room:room-runtime:2.7.2")
    api("androidx.room:room-ktx:2.7.2")
    api("androidx.room:room-paging:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")

    //worker
    api("androidx.work:work-runtime-ktx:2.10.2")

    //firebase
    api(platform("com.google.firebase:firebase-bom:33.15.0"))
    api("com.google.firebase:firebase-crashlytics")
    api("com.google.firebase:firebase-analytics")
    api("com.google.firebase:firebase-auth")
    api("com.google.firebase:firebase-database")
    api("com.google.firebase:firebase-config")

    //koin di
    api("io.insert-koin:koin-core:3.5.3")
    api("io.insert-koin:koin-android:3.5.3")

    //chucker
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")

    //test
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.4.0")
    testImplementation("org.mockito:mockito-inline:4.4.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("org.mockito:mockito-core:5.4.0")
}