import groovy.json.JsonOutput
import java.util.Properties
plugins { id("com.android.application"); id("org.jetbrains.kotlin.android"); id("org.jetbrains.kotlin.plugin.compose") }
val secrets=Properties(); val sf=rootProject.file("secrets.properties"); if(sf.exists()) sf.inputStream().use(secrets::load)
fun secret(name:String)=secrets.getProperty(name) ?: System.getenv(name) ?: ""
android {
 namespace="com.hafizuddin.nomadwealth"; compileSdk=35
 defaultConfig {
  applicationId="com.hafizuddin.nomadwealth"; minSdk=26; targetSdk=35; versionCode=1; versionName="1.0"
  buildConfigField("String","https://kddlsbtfxgtmbpthtcjt.supabase.co","\"${secret("https://kddlsbtfxgtmbpthtcjt.supabase.co").replace("\"","\\\"")}\"")
  buildConfigField("String","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtkZGxzYnRmeGd0bWJwdGh0Y2p0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODU2NzI1MzEsImV4cCI6MjEwMTI0ODUzMX0.knFs8gbzsVIMDe2kqj55Yxwywob1q0U_9iW4OiPnh18","\"${secret("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtkZGxzYnRmeGd0bWJwdGh0Y2p0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODU2NzI1MzEsImV4cCI6MjEwMTI0ODUzMX0.knFs8gbzsVIMDe2kqj55Yxwywob1q0U_9iW4OiPnh18").replace("\"","\\\"")}\"")
 }
 buildFeatures { compose=true; buildConfig=true }
 compileOptions { sourceCompatibility=JavaVersion.VERSION_17; targetCompatibility=JavaVersion.VERSION_17 }
 kotlinOptions { jvmTarget="17" }
 packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
}
dependencies {
 val bom=platform("androidx.compose:compose-bom:2024.12.01")
 implementation(bom); androidTestImplementation(bom)
 implementation("androidx.core:core-ktx:1.15.0")
 implementation("androidx.activity:activity-compose:1.10.0")
 implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
 implementation("androidx.compose.ui:ui")
 implementation("androidx.compose.ui:ui-tooling-preview")
 implementation("androidx.compose.foundation:foundation")
 implementation("androidx.compose.material3:material3")
 implementation("androidx.compose.material:material-icons-extended")
 debugImplementation("androidx.compose.ui:ui-tooling")
}
