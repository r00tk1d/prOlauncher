package app.olauncher.data

import android.content.Context
import android.content.SharedPreferences
import android.os.UserHandle
import android.view.Gravity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

class Prefs(context: Context) {
    private val PREFS_FILENAME = "app.olauncher"

    private val FIRST_OPEN = "FIRST_OPEN"
    private val FIRST_OPEN_TIME = "FIRST_OPEN_TIME"
    private val FIRST_SETTINGS_OPEN = "FIRST_SETTINGS_OPEN"
    private val FIRST_HIDE = "FIRST_HIDE"
    private val USER_STATE = "USER_STATE"
    private val LOCK_MODE = "LOCK_MODE"
    private val AUTO_SHOW_KEYBOARD = "AUTO_SHOW_KEYBOARD"
    private val KEYBOARD_MESSAGE = "KEYBOARD_MESSAGE"
    private val DAILY_WALLPAPER = "DAILY_WALLPAPER"
    private val DAILY_WALLPAPER_URL = "DAILY_WALLPAPER_URL"
    private val HOME_ALIGNMENT = "HOME_ALIGNMENT"
    private val HOME_BOTTOM_ALIGNMENT = "HOME_BOTTOM_ALIGNMENT"
    private val APP_LABEL_ALIGNMENT = "APP_LABEL_ALIGNMENT"
    private val STATUS_BAR = "STATUS_BAR"
    private val DATE_TIME_VISIBILITY = "DATE_TIME_VISIBILITY"
    private val SWIPE_LEFT_ENABLED = "SWIPE_LEFT_ENABLED"
    private val SWIPE_RIGHT_ENABLED = "SWIPE_RIGHT_ENABLED"
    private val HIDDEN_APPS = "HIDDEN_APPS"
    private val HIDDEN_APPS_UPDATED = "HIDDEN_APPS_UPDATED"
    private val SHOW_HINT_COUNTER = "SHOW_HINT_COUNTER"
    private val APP_THEME = "APP_THEME"
    private val ABOUT_CLICKED = "ABOUT_CLICKED"
    private val RATE_CLICKED = "RATE_CLICKED"
    private val WALLPAPER_MSG_SHOWN = "WALLPAPER_MSG_SHOWN"
    private val SHARE_SHOWN_TIME = "SHARE_SHOWN_TIME"
    private val SWIPE_DOWN_ACTION = "SWIPE_DOWN_ACTION"
    private val TEXT_SIZE_SCALE = "TEXT_SIZE_SCALE"
    private val PRO_MESSAGE_SHOWN = "PRO_MESSAGE_SHOWN"
    private val HIDE_SET_DEFAULT_LAUNCHER = "HIDE_SET_DEFAULT_LAUNCHER"
    private val SCREEN_TIME_LAST_UPDATED = "SCREEN_TIME_LAST_UPDATED"
    private val LAUNCHER_RESTART_TIMESTAMP = "LAUNCHER_RECREATE_TIMESTAMP"
    private val SHOWN_ON_DAY_OF_YEAR = "SHOWN_ON_DAY_OF_YEAR"
    // Home button for recents feature disabled
    // private val HOME_BUTTON_SHOW_RECENTS = "HOME_BUTTON_SHOW_RECENTS"

    private val HOME_APPS = "HOME_APPS"
    private val PINNED_APPS = "PINNED_APPS"

    private val APP_NAME_SWIPE_LEFT = "APP_NAME_SWIPE_LEFT"
    private val APP_NAME_SWIPE_RIGHT = "APP_NAME_SWIPE_RIGHT"
    private val APP_PACKAGE_SWIPE_LEFT = "APP_PACKAGE_SWIPE_LEFT"
    private val APP_PACKAGE_SWIPE_RIGHT = "APP_PACKAGE_SWIPE_RIGHT"
    private val APP_ACTIVITY_CLASS_NAME_SWIPE_LEFT = "APP_ACTIVITY_CLASS_NAME_SWIPE_LEFT"
    private val APP_ACTIVITY_CLASS_NAME_SWIPE_RIGHT = "APP_ACTIVITY_CLASS_NAME_SWIPE_RIGHT"
    private val APP_USER_SWIPE_LEFT = "APP_USER_SWIPE_LEFT"
    private val APP_USER_SWIPE_RIGHT = "APP_USER_SWIPE_RIGHT"
    private val CLOCK_APP_PACKAGE = "CLOCK_APP_PACKAGE"
    private val CLOCK_APP_USER = "CLOCK_APP_USER"
    private val CLOCK_APP_CLASS_NAME = "CLOCK_APP_CLASS_NAME"
    private val CALENDAR_APP_PACKAGE = "CALENDAR_APP_PACKAGE"
    private val CALENDAR_APP_USER = "CALENDAR_APP_USER"
    private val CALENDAR_APP_CLASS_NAME = "CALENDAR_APP_CLASS_NAME"
    private val SCREEN_TIME_APP_PACKAGE = "SCREEN_TIME_APP_PACKAGE"
    private val SCREEN_TIME_APP_USER = "SCREEN_TIME_APP_USER"
    private val SCREEN_TIME_APP_CLASS_NAME = "SCREEN_TIME_APP_CLASS_NAME"

    private val SHORTCUT_ID_SWIPE_LEFT = "SHORTCUT_ID_SWIPE_LEFT"
    private val IS_SHORTCUT_SWIPE_LEFT = "IS_SHORTCUT_SWIPE_LEFT"
    private val SHORTCUT_ID_SWIPE_RIGHT = "SHORTCUT_ID_SWIPE_RIGHT"
    private val IS_SHORTCUT_SWIPE_RIGHT = "IS_SHORTCUT_SWIPE_RIGHT"

    private val LAUNCH_HISTORY = "LAUNCH_HISTORY"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var firstOpen: Boolean
        get() = prefs.getBoolean(FIRST_OPEN, true)
        set(value) = prefs.edit { putBoolean(FIRST_OPEN, value).apply() }

    var firstOpenTime: Long
        get() = prefs.getLong(FIRST_OPEN_TIME, 0L)
        set(value) = prefs.edit { putLong(FIRST_OPEN_TIME, value).apply() }

    var firstSettingsOpen: Boolean
        get() = prefs.getBoolean(FIRST_SETTINGS_OPEN, true)
        set(value) = prefs.edit { putBoolean(FIRST_SETTINGS_OPEN, value).apply() }

    var firstHide: Boolean
        get() = prefs.getBoolean(FIRST_HIDE, true)
        set(value) = prefs.edit { putBoolean(FIRST_HIDE, value).apply() }

    var userState: String
        get() = prefs.getString(USER_STATE, Constants.UserState.START).toString()
        set(value) = prefs.edit { putString(USER_STATE, value).apply() }

    var lockModeOn: Boolean
        get() = prefs.getBoolean(LOCK_MODE, false)
        set(value) = prefs.edit { putBoolean(LOCK_MODE, value).apply() }

    var autoShowKeyboard: Boolean
        get() = prefs.getBoolean(AUTO_SHOW_KEYBOARD, true)
        set(value) = prefs.edit { putBoolean(AUTO_SHOW_KEYBOARD, value).apply() }

    var keyboardMessageShown: Boolean
        get() = prefs.getBoolean(KEYBOARD_MESSAGE, false)
        set(value) = prefs.edit { putBoolean(KEYBOARD_MESSAGE, value).apply() }

    var dailyWallpaper: Boolean
        get() = prefs.getBoolean(DAILY_WALLPAPER, false)
        set(value) = prefs.edit { putBoolean(DAILY_WALLPAPER, value).apply() }

    var dailyWallpaperUrl: String
        get() = prefs.getString(DAILY_WALLPAPER_URL, "").toString()
        set(value) = prefs.edit { putString(DAILY_WALLPAPER_URL, value).apply() }

    var homeAlignment: Int
        get() = prefs.getInt(HOME_ALIGNMENT, Gravity.START)
        set(value) = prefs.edit { putInt(HOME_ALIGNMENT, value).apply() }

    var homeBottomAlignment: Boolean
        get() = prefs.getBoolean(HOME_BOTTOM_ALIGNMENT, false)
        set(value) = prefs.edit { putBoolean(HOME_BOTTOM_ALIGNMENT, value).apply() }

    var appLabelAlignment: Int
        get() = prefs.getInt(APP_LABEL_ALIGNMENT, Gravity.START)
        set(value) = prefs.edit { putInt(APP_LABEL_ALIGNMENT, value).apply() }

    var showStatusBar: Boolean
        get() = prefs.getBoolean(STATUS_BAR, false)
        set(value) = prefs.edit { putBoolean(STATUS_BAR, value).apply() }

    var dateTimeVisibility: Int
        get() = prefs.getInt(DATE_TIME_VISIBILITY, Constants.DateTime.ON)
        set(value) = prefs.edit { putInt(DATE_TIME_VISIBILITY, value).apply() }

    var swipeLeftEnabled: Boolean
        get() = prefs.getBoolean(SWIPE_LEFT_ENABLED, true)
        set(value) = prefs.edit { putBoolean(SWIPE_LEFT_ENABLED, value).apply() }

    var swipeRightEnabled: Boolean
        get() = prefs.getBoolean(SWIPE_RIGHT_ENABLED, true)
        set(value) = prefs.edit { putBoolean(SWIPE_RIGHT_ENABLED, value).apply() }

    var appTheme: Int
        get() = prefs.getInt(APP_THEME, AppCompatDelegate.MODE_NIGHT_YES)
        set(value) = prefs.edit { putInt(APP_THEME, value).apply() }

    var textSizeScale: Float
        get() = prefs.getFloat(TEXT_SIZE_SCALE, 1.0f)
        set(value) = prefs.edit { putFloat(TEXT_SIZE_SCALE, value).apply() }

    var proMessageShown: Boolean
        get() = prefs.getBoolean(PRO_MESSAGE_SHOWN, false)
        set(value) = prefs.edit { putBoolean(PRO_MESSAGE_SHOWN, value).apply() }

    var hideSetDefaultLauncher: Boolean
        get() = prefs.getBoolean(HIDE_SET_DEFAULT_LAUNCHER, false)
        set(value) = prefs.edit { putBoolean(HIDE_SET_DEFAULT_LAUNCHER, value).apply() }

    var screenTimeLastUpdated: Long
        get() = prefs.getLong(SCREEN_TIME_LAST_UPDATED, 0L)
        set(value) = prefs.edit { putLong(SCREEN_TIME_LAST_UPDATED, value).apply() }

    var launcherRestartTimestamp: Long
        get() = prefs.getLong(LAUNCHER_RESTART_TIMESTAMP, 0L)
        set(value) = prefs.edit { putLong(LAUNCHER_RESTART_TIMESTAMP, value).apply() }

    var shownOnDayOfYear: Int
        get() = prefs.getInt(SHOWN_ON_DAY_OF_YEAR, 0)
        set(value) = prefs.edit { putInt(SHOWN_ON_DAY_OF_YEAR, value).apply() }

    // Home button for recents feature disabled
    // var homeButtonShowRecents: Boolean
    //     get() = prefs.getBoolean(HOME_BUTTON_SHOW_RECENTS, false)
    //     set(value) = prefs.edit { putBoolean(HOME_BUTTON_SHOW_RECENTS, value).apply() }

    var hiddenApps: MutableSet<String>
        get() = prefs.getStringSet(HIDDEN_APPS, mutableSetOf()) as MutableSet<String>
        set(value) = prefs.edit { putStringSet(HIDDEN_APPS, value).apply() }

    var hiddenAppsUpdated: Boolean
        get() = prefs.getBoolean(HIDDEN_APPS_UPDATED, false)
        set(value) = prefs.edit { putBoolean(HIDDEN_APPS_UPDATED, value).apply() }

    var toShowHintCounter: Int
        get() = prefs.getInt(SHOW_HINT_COUNTER, 1)
        set(value) = prefs.edit { putInt(SHOW_HINT_COUNTER, value).apply() }

    var aboutClicked: Boolean
        get() = prefs.getBoolean(ABOUT_CLICKED, false)
        set(value) = prefs.edit { putBoolean(ABOUT_CLICKED, value).apply() }

    var rateClicked: Boolean
        get() = prefs.getBoolean(RATE_CLICKED, false)
        set(value) = prefs.edit { putBoolean(RATE_CLICKED, value).apply() }

    var wallpaperMsgShown: Boolean
        get() = prefs.getBoolean(WALLPAPER_MSG_SHOWN, false)
        set(value) = prefs.edit { putBoolean(WALLPAPER_MSG_SHOWN, value).apply() }

    var shareShownTime: Long
        get() = prefs.getLong(SHARE_SHOWN_TIME, 0L)
        set(value) = prefs.edit { putLong(SHARE_SHOWN_TIME, value).apply() }

    var swipeDownAction: Int
        get() = prefs.getInt(SWIPE_DOWN_ACTION, Constants.SwipeDownAction.NOTIFICATIONS)
        set(value) = prefs.edit { putInt(SWIPE_DOWN_ACTION, value).apply() }

    var appNameSwipeLeft: String
        get() = prefs.getString(APP_NAME_SWIPE_LEFT, "Camera").toString()
        set(value) = prefs.edit { putString(APP_NAME_SWIPE_LEFT, value).apply() }

    var appNameSwipeRight: String
        get() = prefs.getString(APP_NAME_SWIPE_RIGHT, "Phone").toString()
        set(value) = prefs.edit { putString(APP_NAME_SWIPE_RIGHT, value).apply() }

    var appPackageSwipeLeft: String
        get() = prefs.getString(APP_PACKAGE_SWIPE_LEFT, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_SWIPE_LEFT, value).apply() }

    var appActivityClassNameSwipeLeft: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_SWIPE_LEFT, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_SWIPE_LEFT, value).apply() }

    var appPackageSwipeRight: String
        get() = prefs.getString(APP_PACKAGE_SWIPE_RIGHT, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_SWIPE_RIGHT, value).apply() }

    var appActivityClassNameRight: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_SWIPE_RIGHT, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_SWIPE_RIGHT, value).apply() }

    var appUserSwipeLeft: String
        get() = prefs.getString(APP_USER_SWIPE_LEFT, "").toString()
        set(value) = prefs.edit { putString(APP_USER_SWIPE_LEFT, value).apply() }

    var appUserSwipeRight: String
        get() = prefs.getString(APP_USER_SWIPE_RIGHT, "").toString()
        set(value) = prefs.edit { putString(APP_USER_SWIPE_RIGHT, value).apply() }

    var clockAppPackage: String
        get() = prefs.getString(CLOCK_APP_PACKAGE, "").toString()
        set(value) = prefs.edit { putString(CLOCK_APP_PACKAGE, value).apply() }

    var clockAppUser: String
        get() = prefs.getString(CLOCK_APP_USER, "").toString()
        set(value) = prefs.edit { putString(CLOCK_APP_USER, value).apply() }

    var clockAppClassName: String?
        get() = prefs.getString(CLOCK_APP_CLASS_NAME, "").toString()
        set(value) = prefs.edit { putString(CLOCK_APP_CLASS_NAME, value).apply() }

    var calendarAppPackage: String
        get() = prefs.getString(CALENDAR_APP_PACKAGE, "").toString()
        set(value) = prefs.edit { putString(CALENDAR_APP_PACKAGE, value).apply() }

    var calendarAppUser: String
        get() = prefs.getString(CALENDAR_APP_USER, "").toString()
        set(value) = prefs.edit { putString(CALENDAR_APP_USER, value).apply() }

    var calendarAppClassName: String?
        get() = prefs.getString(CALENDAR_APP_CLASS_NAME, "").toString()
        set(value) = prefs.edit { putString(CALENDAR_APP_CLASS_NAME, value).apply() }

    var screenTimeAppPackage: String
        get() = prefs.getString(SCREEN_TIME_APP_PACKAGE, "").toString()
        set(value) = prefs.edit { putString(SCREEN_TIME_APP_PACKAGE, value).apply() }

    var screenTimeAppUser: String
        get() = prefs.getString(SCREEN_TIME_APP_USER, "").toString()
        set(value) = prefs.edit { putString(SCREEN_TIME_APP_USER, value).apply() }

    var screenTimeAppClassName: String?
        get() = prefs.getString(SCREEN_TIME_APP_CLASS_NAME, "").toString()
        set(value) = prefs.edit { putString(SCREEN_TIME_APP_CLASS_NAME, value).apply() }

    var shortcutIdSwipeLeft: String
        get() = prefs.getString(SHORTCUT_ID_SWIPE_LEFT, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_SWIPE_LEFT, value) }

    var isShortcutSwipeLeft: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_SWIPE_LEFT, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_SWIPE_LEFT, value) }

    var shortcutIdSwipeRight: String
        get() = prefs.getString(SHORTCUT_ID_SWIPE_RIGHT, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_SWIPE_RIGHT, value) }

    var isShortcutSwipeRight: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_SWIPE_RIGHT, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_SWIPE_RIGHT, value) }

    fun getHomeApps(): List<AppModel.HomeApp> =
        HomeAppsCodec.decodeHomeApps(prefs.getString(HOME_APPS, "").toString())

    fun saveHomeApps(apps: List<AppModel.HomeApp>) =
        prefs.edit { putString(HOME_APPS, HomeAppsCodec.encodeHomeApps(apps)) }

    fun saveHomeApp(position: Int, app: AppModel.HomeApp) {
        val apps = getHomeApps().toMutableList()
        if (position in 0 until apps.size) apps[position] = app
        else apps.add(app)
        saveHomeApps(apps)
    }

    fun removeHomeApp(position: Int) {
        val apps = getHomeApps().toMutableList()
        if (position !in apps.indices) return
        apps.removeAt(position)
        saveHomeApps(apps)
    }

    fun getPinnedApps(): List<AppModel.PinnedApp> =
        HomeAppsCodec.decodePinnedApps(prefs.getString(PINNED_APPS, "").toString())

    fun savePinnedApps(apps: List<AppModel.PinnedApp>) =
        prefs.edit { putString(PINNED_APPS, HomeAppsCodec.encodePinnedApps(apps)) }

    fun pinApp(app: AppModel.PinnedApp) {
        val apps = getPinnedApps().toMutableList()
        apps.removeAll { samePinnedApp(it, app.appPackage, app.user, app.isShortcut, app.shortcutId) }
        apps.add(app)
        savePinnedApps(apps)
    }

    fun removePinnedApp(
        appPackage: String,
        user: String,
        isShortcut: Boolean = false,
        shortcutId: String = "",
    ) {
        val apps = getPinnedApps().toMutableList()
        apps.removeAll { samePinnedApp(it, appPackage, user, isShortcut, shortcutId) }
        savePinnedApps(apps)
    }

    fun getActivePinnedApps(now: Long = System.currentTimeMillis()): List<AppModel.PinnedApp> {
        val all = getPinnedApps()
        val active = all.filter { it.expiresAt > now }
        if (active.size != all.size) savePinnedApps(active)
        return active
    }

    private fun samePinnedApp(
        app: AppModel.PinnedApp,
        appPackage: String,
        user: String,
        isShortcut: Boolean,
        shortcutId: String,
    ): Boolean = app.appPackage == appPackage &&
        app.user == user &&
        app.isShortcut == isShortcut &&
        app.shortcutId == shortcutId

    fun swapHomeApps(i: Int, j: Int) {
        if (i == j) return
        val apps = getHomeApps().toMutableList()
        if (i !in apps.indices || j !in apps.indices) return
        val temp = apps[i]
        apps[i] = apps[j]
        apps[j] = temp
        saveHomeApps(apps)
    }

    fun createFolder(name: String) {
        if (name.isEmpty()) return
        saveHomeApps(getHomeApps() + AppModel.HomeApp(isFolder = true, folderName = name))
    }

    fun renameFolderApp(index: Int, name: String) {
        val apps = getHomeApps().toMutableList()
        val folder = apps.getOrNull(index) ?: return
        if (!folder.isFolder) return
        apps[index] = folder.copy(folderName = name)
        saveHomeApps(apps)
    }

    fun getFolderName(index: Int): String =
        getHomeApps().getOrNull(index)?.folderName ?: ""

    fun getFolderApps(index: Int): List<AppModel.FolderApp> =
        getHomeApps().getOrNull(index)?.folderApps ?: emptyList()

    fun saveFolderApp(index: Int, position: Int, app: AppModel.FolderApp) {
        val apps = getHomeApps().toMutableList()
        val folder = apps.getOrNull(index) ?: return
        if (!folder.isFolder) return
        val folderApps = folder.folderApps.toMutableList()
        if (position in 0 until folderApps.size) folderApps[position] = app
        else folderApps.add(app)
        apps[index] = folder.copy(folderApps = folderApps)
        saveHomeApps(apps)
    }

    fun saveFolderApps(index: Int, apps: List<AppModel.FolderApp>) {
        val homeApps = getHomeApps().toMutableList()
        val folder = homeApps.getOrNull(index) ?: return
        if (!folder.isFolder) return
        homeApps[index] = folder.copy(folderApps = apps)
        saveHomeApps(homeApps)
    }

    fun removeFolderApp(index: Int, position: Int) {
        val apps = getHomeApps().toMutableList()
        val folder = apps.getOrNull(index) ?: return
        if (!folder.isFolder) return
        val folderApps = folder.folderApps.toMutableList()
        if (position !in folderApps.indices) return
        folderApps.removeAt(position)
        apps[index] = folder.copy(folderApps = folderApps)
        saveHomeApps(apps)
    }

    fun renameFolderApp(index: Int, position: Int, newLabel: String) {
        val apps = getHomeApps().toMutableList()
        val folder = apps.getOrNull(index) ?: return
        if (!folder.isFolder) return
        val folderApps = folder.folderApps.toMutableList()
        val folderApp = folderApps.getOrNull(position) ?: return
        if (folderApp.appPackage.isEmpty()) return
        folderApps[position] = folderApp.copy(appLabel = newLabel)
        apps[index] = folder.copy(folderApps = folderApps)
        saveHomeApps(apps)
    }

    fun swapFolderApps(index: Int, position1: Int, position2: Int) {
        if (position1 == position2) return
        val apps = getHomeApps().toMutableList()
        val folder = apps.getOrNull(index) ?: return
        if (!folder.isFolder) return
        val folderApps = folder.folderApps.toMutableList()
        if (position1 !in folderApps.indices || position2 !in folderApps.indices) return
        val temp = folderApps[position1]
        folderApps[position1] = folderApps[position2]
        folderApps[position2] = temp
        apps[index] = folder.copy(folderApps = folderApps)
        saveHomeApps(apps)
    }

    fun updateAppActivityClassName(packageName: String, activityClassName: String) {
        var updated = false
        val apps = getHomeApps().map { homeApp ->
            when {
                !homeApp.isFolder && homeApp.appPackage == packageName -> {
                    updated = true
                    homeApp.copy(activityClassName = activityClassName)
                }

                homeApp.isFolder && homeApp.folderApps.any { it.appPackage == packageName } -> {
                    updated = true
                    homeApp.copy(
                        folderApps = homeApp.folderApps.map {
                            if (it.appPackage == packageName) it.copy(activityClassName = activityClassName)
                            else it
                        }
                    )
                }

                else -> homeApp
            }
        }
        if (updated) saveHomeApps(apps)
        if (clockAppPackage == packageName) clockAppClassName = activityClassName
        if (calendarAppPackage == packageName) calendarAppClassName = activityClassName
        if (screenTimeAppPackage == packageName) screenTimeAppClassName = activityClassName
        if (appPackageSwipeLeft == packageName) appActivityClassNameSwipeLeft = activityClassName
        if (appPackageSwipeRight == packageName) appActivityClassNameRight = activityClassName
    }

    fun getAppRenameLabel(appPackage: String): String = prefs.getString(appPackage, "").toString()

    fun setAppRenameLabel(appPackage: String, renameLabel: String) = prefs.edit { putString(appPackage, renameLabel) }

    fun addLaunchHistory(appModel: AppModel) {
        if (appModel is AppModel.PrivateSpaceHeader) return
        val history = getLaunchHistoryRaw().toMutableList()
        history.removeAll { existing ->
            when (existing) {
                is AppModel.PinnedShortcut -> appModel is AppModel.PinnedShortcut &&
                    existing.appPackage == appModel.appPackage &&
                    existing.user == appModel.user &&
                    existing.shortcutId == appModel.shortcutId

                else -> appModel is AppModel.App &&
                    existing.appPackage == appModel.appPackage &&
                    existing.user == appModel.user
            }
        }
        history.add(0, appModel)
        while (history.size > Constants.MAX_LAUNCH_HISTORY) history.removeAt(history.lastIndex)

        val array = JSONArray()
        for (app in history) {
            when (app) {
                is AppModel.PrivateSpaceHeader -> {}
                is AppModel.PinnedShortcut -> array.put(JSONObject().apply {
                    put("isShortcut", true)
                    put("label", app.appLabel)
                    put("package", app.appPackage)
                    put("activity", "")
                    put("user", app.user.toString())
                    put("shortcutId", app.shortcutId)
                })

                is AppModel.App -> array.put(JSONObject().apply {
                    put("isShortcut", false)
                    put("label", app.appLabel)
                    put("package", app.appPackage)
                    put("activity", app.activityClassName ?: "")
                    put("user", app.user.toString())
                    put("shortcutId", "")
                })
            }
        }
        prefs.edit { putString(LAUNCH_HISTORY, array.toString()) }
    }

    fun getLaunchHistory(): List<AppModel> {
        val result = mutableListOf<AppModel>()
        for (app in getLaunchHistoryRaw()) {
            when (app) {
                is AppModel.PrivateSpaceHeader -> {}
                is AppModel.PinnedShortcut -> result.add(app)
                is AppModel.App -> result.add(app)
            }
        }
        return result
    }

    private fun getLaunchHistoryRaw(): List<AppModel> {
        val raw = prefs.getString(LAUNCH_HISTORY, "").toString()
        if (raw.isBlank()) return emptyList()
        val result = mutableListOf<AppModel>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.optJSONObject(i) ?: continue
                val isShortcut = obj.optBoolean("isShortcut")
                val user = parseUserHandle(obj.optString("user"))
                val app = if (isShortcut) {
                    AppModel.PinnedShortcut(
                        appLabel = obj.optString("label"),
                        key = null,
                        appPackage = obj.optString("package"),
                        shortcutId = obj.optString("shortcutId"),
                        user = user,
                    )
                } else {
                    AppModel.App(
                        appLabel = obj.optString("label"),
                        key = null,
                        appPackage = obj.optString("package"),
                        activityClassName = obj.optString("activity").takeIf { it.isNotBlank() },
                        user = user,
                    )
                }
                result.add(app)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun parseUserHandle(userString: String): UserHandle {
        val id = userString.substringAfter("UserHandle{").substringBefore("}").toIntOrNull()
        return if (id != null) UserHandle.getUserHandleForUid(id * 100000)
        else android.os.Process.myUserHandle()
    }
}
