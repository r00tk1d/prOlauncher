package app.olauncher.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeAppsCodecTest {

    @Test
    fun `empty home apps round-trip`() {
        assertEquals(emptyList<AppModel.HomeApp>(), HomeAppsCodec.decodeHomeApps(HomeAppsCodec.encodeHomeApps(emptyList())))
    }

    @Test
    fun `regular and shortcut home apps round-trip`() {
        val apps = listOf(
            AppModel.HomeApp(
                appLabel = "Settings",
                appPackage = "com.android.settings",
                activityClassName = "com.android.settings.Settings",
                user = "UserHandle{0}",
                isShortcut = false,
            ),
            AppModel.HomeApp(
                appLabel = "Pinned Site",
                appPackage = "com.example.browser",
                activityClassName = null,
                user = "UserHandle{0}",
                isShortcut = true,
                shortcutId = "my-shortcut",
            ),
        )
        val decoded = HomeAppsCodec.decodeHomeApps(HomeAppsCodec.encodeHomeApps(apps))
        assertEquals(apps, decoded)
    }

    @Test
    fun `folders with apps round-trip`() {
        val apps = listOf(
            AppModel.HomeApp(
                appLabel = "Telegram",
                appPackage = "org.telegram.messenger",
                activityClassName = "org.telegram.ui.LaunchActivity",
                user = "UserHandle{0}",
            ),
            AppModel.HomeApp(
                isFolder = true,
                folderName = "Work",
                folderApps = listOf(
                    AppModel.FolderApp(
                        appLabel = "Mail",
                        appPackage = "com.example.mail",
                        activityClassName = "com.example.mail.MainActivity",
                        user = "UserHandle{0}",
                        isShortcut = false,
                    ),
                    AppModel.FolderApp(
                        appLabel = "Docs",
                        appPackage = "com.example.docs",
                        activityClassName = null,
                        user = "UserHandle{0}",
                        isShortcut = true,
                        shortcutId = "recent-doc",
                    ),
                ),
            ),
        )
        val decoded = HomeAppsCodec.decodeHomeApps(HomeAppsCodec.encodeHomeApps(apps))
        assertEquals(apps, decoded)
    }

    @Test
    fun `folder apps round-trip`() {
        val apps = listOf(
            AppModel.FolderApp(
                appLabel = "Alarm",
                appPackage = "com.example.clock",
                activityClassName = "com.example.clock.Alarm",
                user = "UserHandle{0}",
            ),
            AppModel.FolderApp(
                appLabel = "Site",
                appPackage = "com.example.site",
                activityClassName = null,
                user = "UserHandle{0}",
                isShortcut = true,
                shortcutId = "site",
            ),
        )
        val decoded = HomeAppsCodec.decodeFolderApps(HomeAppsCodec.encodeFolderApps(apps))
        assertEquals(apps, decoded)
    }

    @Test
    fun `empty pinned apps round-trip`() {
        assertEquals(emptyList<AppModel.PinnedApp>(), HomeAppsCodec.decodePinnedApps(HomeAppsCodec.encodePinnedApps(emptyList())))
    }

    @Test
    fun `regular and shortcut pinned apps round-trip`() {
        val apps = listOf(
            AppModel.PinnedApp(
                appLabel = "Spotify",
                appPackage = "com.spotify.music",
                activityClassName = "com.spotify.music.MainActivity",
                user = "UserHandle{0}",
                isShortcut = false,
                expiresAt = 123456789L,
            ),
            AppModel.PinnedApp(
                appLabel = "Site",
                appPackage = "com.example.site",
                activityClassName = null,
                user = "UserHandle{0}",
                isShortcut = true,
                shortcutId = "site",
                expiresAt = 987654321L,
            ),
        )
        val decoded = HomeAppsCodec.decodePinnedApps(HomeAppsCodec.encodePinnedApps(apps))
        assertEquals(apps, decoded)
    }

    @Test
    fun `blank and corrupt pinned input decode to empty list`() {
        assertTrue(HomeAppsCodec.decodePinnedApps("").isEmpty())
        assertTrue(HomeAppsCodec.decodePinnedApps("not json").isEmpty())
        assertTrue(HomeAppsCodec.decodePinnedApps("[]").isEmpty())
    }

    @Test
    fun `blank and corrupt input decode to empty list`() {
        assertTrue(HomeAppsCodec.decodeHomeApps("").isEmpty())
        assertTrue(HomeAppsCodec.decodeHomeApps("not json").isEmpty())
        assertTrue(HomeAppsCodec.decodeFolderApps("").isEmpty())
        assertTrue(HomeAppsCodec.decodeFolderApps("[]").isEmpty())
    }

    @Test
    fun `blank activity class decodes to null`() {
        val app = AppModel.HomeApp(
            appLabel = "App",
            appPackage = "com.example.app",
            activityClassName = null,
            user = "UserHandle{0}",
        )
        val decoded = HomeAppsCodec.decodeHomeApps(HomeAppsCodec.encodeHomeApps(listOf(app))).single()
        assertEquals(null, decoded.activityClassName)
    }
}
