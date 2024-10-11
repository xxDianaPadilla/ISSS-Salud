plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "diana.padilla.isss_salud"
    compileSdk = 34

    defaultConfig {
        applicationId = "diana.padilla.isss_salud"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

configurations.all {
    resolutionStrategy {
        // Fuerza a Gradle a usar la versión 1.38 de Bouncy Castle
        force("org.bouncycastle:bcprov-jdk14:1.38")
    }

}

dependencies {

    implementation("com.airbnb.android:lottie:6.4.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation(files("libs\\activation.jar"))
    implementation(files("libs\\additionnal.jar"))
    implementation(files("libs\\mail.jar"))
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation ("androidx.appcompat:appcompat:1.3.1")
    implementation("com.oracle.database.jdbc:ojdbc6:11.2.0.4")
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("net.sf.jasperreports:jasperreports:6.20.0"){
        exclude(group = "org.bouncycastle")
    }
    implementation("org.apache.poi:poi:5.2.2")
    implementation("com.lowagie:itext:2.1.7"){
        exclude(group = "org.bouncycastle")
    }
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-collections4:4.4")
}