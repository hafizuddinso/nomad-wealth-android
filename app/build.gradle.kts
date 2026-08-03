import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

/*
 * Reads configuration from:
 * 1. secrets.properties when building locally
 * 2. GitHub Actions environment variables when building online
 */
val localSecrets = Properties()
val secretsFile = rootProject.file("secrets.properties")

if (secretsFile.exists()) {
    secretsFile.inputStream().use { input ->
        localSecrets.load(input)
    }
}

fun readSecret(name: String): String {
    return (
        localSecrets.getProperty(name)
            ?: System.getenv(name)
            ?: ""
    ).trim()
}

/*
 * Converts a value into a valid Java string literal.
 * This protects the generated BuildConfig.java from hidden newlines,
 * quotation marks and backslashes.
 */
fun javaStringLiteral(value: String): String {
    val escapedValue = value
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\r", "\\r")
        .replace("\n", "\\n")

    return "\"$escapedValue\""
}

val supabaseUrl = readSecret("SUPABASE_URL")
val supabaseAnonKey = readSecret("SUPABASE_ANON_KEY")

android {
    namespace = "com.hafizuddin.nomadwealth"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hafizuddin.nomadwealth"

        minSdk = 26
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "SUPABASE_URL",
            javaStringLiteral(supabaseUrl)
        )

        buildConfigField(
            "String",
            "SUPABASE_ANON_KEY",
            javaStringLiteral(supabaseAnonKey)
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val composeBom = platform(
        "androidx.compose:compose-bom:2024.12.01"
    )

    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
