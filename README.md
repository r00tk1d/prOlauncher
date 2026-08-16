# prOlauncher

A personal vibecoded fork of [**Olauncher | Minimal AF Launcher**](https://github.com/tanujnotes/Olauncher), the minimal, ad-free launcher for Android.

This repository is forked from the original [Olauncher](https://github.com/tanujnotes/Olauncher) project by [Tanuj Notes](https://github.com/tanujnotes) and builds on top of it. All the original Olauncher functionality still applies — this fork only **adds** the features listed below.

- Upstream project: [tanujnotes/Olauncher](https://github.com/tanujnotes/Olauncher)
- License: [GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html)

## Added features

All changes are on top of upstream Olauncher (v6.7.19).

### Folders
- **Folders**: Create up to 10 folders on the home screen (long-press a slot → *Add folder*). Creating a folder from an app slot keeps the app in place and places the new folder in the first empty slot. Add apps to a folder by long-pressing the folder on the home screen → *Add app*, or by long-pressing an empty app slot inside the folder view.
- **Search history**: when the search box is empty, the drawer shows the apps you most recently launched from the search (instead of the full list). Typing a letter switches to normal filtering; clearing the text shows the history again.
- **Pin apps for a limited time**: long-press an app in the app drawer/search and choose *Pin* to put it at the very top of the home screen for a chosen duration (1 day, 7 days, or a custom amount of days/hours). Pinned apps show a small pin icon in front of their name and disappear automatically when the time runs out. Pinning is refused when the home screen already holds 10 apps/folders; re-pinning an already pinned app extends its time.
- **Drag & drop reordering** of home screen slots.
- **No preset app count**: the fixed "number of home apps" setting was removed. Apps are added individually (long-press an empty slot → *Add app*, or tap the hint when the home is empty), and empty slots are hidden.
- **10 instead of 8 slots**
- **Sectioned long-press menu**: the home slot menu groups options under *Add slot* (Add app, Add folder) and *Modify Slot* (Replace app, Rename app, Remove app) headers; the same applies to the folder-apps menu and to folders (long-press a folder → *Add app* to add an app into it).


## Building

Requires a JDK (17 or newer) and the Android SDK (set `sdk.dir` in `local.properties`).

From the project root:

```bash
# Debug APK (signed with the debug key, ready to install):
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk

# Release APK (minified with R8):
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk (signed) or app-release-unsigned.apk (no signing config)
```

Install the debug build directly on a connected device with:

```bash
./gradlew installDebug
```

### Signing the release build

The release build is signed automatically with your keystore when the following settings are available. Provide them either as project properties in `~/.gradle/gradle.properties` or as environment variables:

```properties
KEYSTORE_PATH=/path/to/prOlauncher-release.jks
KEYSTORE_PASSWORD=your-store-password
KEY_ALIAS=prOlauncher
KEY_PASSWORD=your-key-password
```

Generate a keystore once (JDK's `keytool`):

```bash
keytool -genkeypair -v \
  -keystore prOlauncher-release.jks \
  -alias prOlauncher \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -storepass <password> -keypass <password> \
  -dname "CN=prOlauncher, O=prOlauncher, C=DE"
```

Keep the keystore and passwords safe — anyone who has them can sign apps as you. Without a keystore, the release build falls back to an unsigned `app-release-unsigned.apk`.

## Testing on a connected phone

1. On your phone, enable **Developer options** (Settings → About phone → tap "Build number" 7 times) and turn on **USB debugging** (Settings → Developer options → USB debugging).
2. Connect the phone with a USB cable and allow USB debugging on the "Allow USB debugging?" prompt (check "Always allow").
3. Verify the phone is visible to `adb`:

```bash
adb devices
# ~/Android/Sdk/platform-tools/adb if adb is not on your PATH
```

The output should show your device as `device` (not `unauthorized` or `offline`). If it says `unauthorized`, unplug/replug and accept the prompt again.

4. Build and install the debug build directly on the phone:

```bash
./gradlew installDebug
```

5. Launch the app and watch the logs while you test:

```bash
adb logcat --pid=$(adb shell pidof -s app.olauncher)
# Ctrl-C to stop
```

To reinstall after a code change, just run `./gradlew installDebug` again. The launcher replaces your current home screen — use the Recents button to navigate back to your old launcher while testing.

## Releases/Install

APKs are built automatically by a GitHub Actions workflow (`.github/workflows/build-release.yml`) and published as a GitHub Release.

To publish a new release, push a tag (matching `v*`). This creates and pushes the next tag (`v<version>-pro.<n>`), derived from the `versionName` in `app/build.gradle` and the highest existing `-pro.n` tag:

```bash
BASE=$(sed -n 's/.*versionName "v\([0-9.]*\)".*/\1/p' app/build.gradle)
LAST_PRO=$(git tag --sort=-v:refname | grep -m1 "^v${BASE}-pro\.")
if [ -n "$LAST_PRO" ]; then NEXT=$((${LAST_PRO##*.} + 1)); else NEXT=1; fi
TAG="v${BASE}-pro.${NEXT}"
git tag "$TAG"
git push origin "$TAG"
```

Run `echo "$TAG"` first to preview the tag name without pushing it.

You can also trigger the build from the **Actions** tab (workflow *Build APK and create release* → *Run workflow*). Every release contains:

- `app-release.apk` — minified release build, signed with your keystore (ready to install, updates in place)
- `app-debug.apk` — debug build (installable, but each workflow run signs it with a freshly generated debug key, so it cannot update an existing install)
- `app-release-unsigned.apk` — only produced when no signing keystore is configured

To have releases signed with your stable key, add these repository secrets (**Settings → Secrets and variables → Actions**): `KEYSTORE_BASE64` (your keystore base64-encoded: `base64 -w0 prOlauncher-release.jks`), `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`.

To install the signed release APK, download `app-release.apk` from the release page, allow installing apps from unknown sources for your browser/downloader, and open the file. Because it uses the same key on every release, you can update by simply installing over the previous version.
