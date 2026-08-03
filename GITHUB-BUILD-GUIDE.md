# Build the APK using only GitHub

## 1. Create the repository

1. Open GitHub.
2. Click **+** at the top-right.
3. Click **New repository**.
4. Repository name: `nomad-wealth-android`.
5. Description: `Native Kotlin and Jetpack Compose personal finance app with Supabase authentication.`
6. Select **Private** for the first build.
7. Do not add a README, gitignore, or license.
8. Click **Create repository**.

## 2. Upload the project

1. Download and extract `NomadWealth-Android-v1.0-GitHub.zip`.
2. Open the new empty repository.
3. Click **uploading an existing file**, or **Add file → Upload files**.
4. Open the extracted project folder in Finder.
5. Press **Command + Shift + .** to show hidden files.
6. Press **Command + A** to select everything.
7. Drag everything into GitHub's upload box.
8. Confirm these are present: `.github`, `app`, `gradle`, `build.gradle.kts`, `settings.gradle.kts`, `README.md`.
9. Commit message: `Add Nomad Wealth Android application`.
10. Click **Commit changes**.

## 3. Add Supabase secrets

1. Open the repository.
2. Click **Settings**.
3. Click **Secrets and variables** in the left sidebar.
4. Click **Actions**.
5. Click **New repository secret**.
6. Name: `SUPABASE_URL`.
7. Value: your Supabase project URL, such as `https://your-project.supabase.co`.
8. Click **Add secret**.
9. Add another secret named `SUPABASE_ANON_KEY`.
10. Paste the Supabase anon or publishable key.
11. Click **Add secret**.

Never use a Supabase service-role key.

## 4. Run the cloud build

1. Click the repository's **Actions** tab.
2. Enable workflows if GitHub asks.
3. Click **Build Nomad Wealth APK** in the left sidebar.
4. Click **Run workflow**.
5. Keep branch `main`.
6. Click the green **Run workflow** button.
7. Wait around 3–10 minutes.
8. Open the run after it displays a green check mark.

## 5. Download the APK

1. Open the successful run.
2. Scroll to **Artifacts**.
3. Click `Nomad-Wealth-Android-Debug-APK`.
4. Extract the downloaded ZIP.
5. The installable file is `app-debug.apk`.

## 6. Install on Android

1. Send `app-debug.apk` to the phone.
2. Open the file.
3. When Android asks, open **Settings** and enable **Allow from this source**.
4. Return and tap **Install**.
5. Open **Nomad Wealth**.

## Updating later

Upload changed files and commit them to `main`. GitHub automatically builds a new APK.

## Important

This is a debug APK for testing and portfolio demonstrations. It is not a Google Play production build.
