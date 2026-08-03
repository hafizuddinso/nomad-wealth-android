import java.util.Properties
plugins { id("com.android.application"); id("org.jetbrains.kotlin.android"); id("org.jetbrains.kotlin.plugin.compose") }
val secrets=Properties(); val sf=rootProject.file("secrets.properties"); if(sf.exists()) sf.inputStream().use(secrets::load)
fun secret(name:String)=secrets.getProperty(name) ?: System.getenv(name) ?: ""
android {
 namespace="com.hafizuddin.nomadwealth"; compileSdk=35
 defaultConfig {
  applicationId="com.hafizuddin.nomadwealth"; minSdk=26; targetSdk=35; versionCode=1; versionName="1.0"
  buildConfigField("String","SUPABASE_URL","\"${secret("SUPABASE_URL").replace("\"","\\\"")}\"")
  buildConfigField("String","SUPABASE_ANON_KEY","\"${secret("SUPABASE_ANON_KEY").replace("\"","\\\"")}\"")
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
