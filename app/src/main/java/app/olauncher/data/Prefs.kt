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

    private val APP_NAME_1 = "APP_NAME_1"
    private val APP_NAME_2 = "APP_NAME_2"
    private val APP_NAME_3 = "APP_NAME_3"
    private val APP_NAME_4 = "APP_NAME_4"
    private val APP_NAME_5 = "APP_NAME_5"
    private val APP_NAME_6 = "APP_NAME_6"
    private val APP_NAME_7 = "APP_NAME_7"
    private val APP_NAME_8 = "APP_NAME_8"
    private val APP_NAME_9 = "APP_NAME_9"
    private val APP_NAME_10 = "APP_NAME_10"
    private val APP_PACKAGE_1 = "APP_PACKAGE_1"
    private val APP_PACKAGE_2 = "APP_PACKAGE_2"
    private val APP_PACKAGE_3 = "APP_PACKAGE_3"
    private val APP_PACKAGE_4 = "APP_PACKAGE_4"
    private val APP_PACKAGE_5 = "APP_PACKAGE_5"
    private val APP_PACKAGE_6 = "APP_PACKAGE_6"
    private val APP_PACKAGE_7 = "APP_PACKAGE_7"
    private val APP_PACKAGE_8 = "APP_PACKAGE_8"
    private val APP_PACKAGE_9 = "APP_PACKAGE_9"
    private val APP_PACKAGE_10 = "APP_PACKAGE_10"
    private val APP_ACTIVITY_CLASS_NAME_1 = "APP_ACTIVITY_CLASS_NAME_1"
    private val APP_ACTIVITY_CLASS_NAME_2 = "APP_ACTIVITY_CLASS_NAME_2"
    private val APP_ACTIVITY_CLASS_NAME_3 = "APP_ACTIVITY_CLASS_NAME_3"
    private val APP_ACTIVITY_CLASS_NAME_4 = "APP_ACTIVITY_CLASS_NAME_4"
    private val APP_ACTIVITY_CLASS_NAME_5 = "APP_ACTIVITY_CLASS_NAME_5"
    private val APP_ACTIVITY_CLASS_NAME_6 = "APP_ACTIVITY_CLASS_NAME_6"
    private val APP_ACTIVITY_CLASS_NAME_7 = "APP_ACTIVITY_CLASS_NAME_7"
    private val APP_ACTIVITY_CLASS_NAME_8 = "APP_ACTIVITY_CLASS_NAME_8"
    private val APP_ACTIVITY_CLASS_NAME_9 = "APP_ACTIVITY_CLASS_NAME_9"
    private val APP_ACTIVITY_CLASS_NAME_10 = "APP_ACTIVITY_CLASS_NAME_10"
    private val APP_USER_1 = "APP_USER_1"
    private val APP_USER_2 = "APP_USER_2"
    private val APP_USER_3 = "APP_USER_3"
    private val APP_USER_4 = "APP_USER_4"
    private val APP_USER_5 = "APP_USER_5"
    private val APP_USER_6 = "APP_USER_6"
    private val APP_USER_7 = "APP_USER_7"
    private val APP_USER_8 = "APP_USER_8"
    private val APP_USER_9 = "APP_USER_9"
    private val APP_USER_10 = "APP_USER_10"

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

    private val IS_SHORTCUT_1 = "IS_SHORTCUT_1"
    private val SHORTCUT_ID_1 = "SHORTCUT_ID_1"
    private val IS_SHORTCUT_2 = "IS_SHORTCUT_2"
    private val SHORTCUT_ID_2 = "SHORTCUT_ID_2"
    private val IS_SHORTCUT_3 = "IS_SHORTCUT_3"
    private val SHORTCUT_ID_3 = "SHORTCUT_ID_3"
    private val IS_SHORTCUT_4 = "IS_SHORTCUT_4"
    private val SHORTCUT_ID_4 = "SHORTCUT_ID_4"
    private val IS_SHORTCUT_5 = "IS_SHORTCUT_5"
    private val SHORTCUT_ID_5 = "SHORTCUT_ID_5"
    private val IS_SHORTCUT_6 = "IS_SHORTCUT_6"
    private val SHORTCUT_ID_6 = "SHORTCUT_ID_6"
    private val IS_SHORTCUT_7 = "IS_SHORTCUT_7"
    private val SHORTCUT_ID_7 = "SHORTCUT_ID_7"
    private val IS_SHORTCUT_8 = "IS_SHORTCUT_8"
    private val SHORTCUT_ID_8 = "SHORTCUT_ID_8"
    private val IS_SHORTCUT_9 = "IS_SHORTCUT_9"
    private val SHORTCUT_ID_9 = "SHORTCUT_ID_9"
    private val IS_SHORTCUT_10 = "IS_SHORTCUT_10"
    private val SHORTCUT_ID_10 = "SHORTCUT_ID_10"

    private val SHORTCUT_ID_SWIPE_LEFT = "SHORTCUT_ID_SWIPE_LEFT"
    private val IS_SHORTCUT_SWIPE_LEFT = "IS_SHORTCUT_SWIPE_LEFT"
    private val SHORTCUT_ID_SWIPE_RIGHT = "SHORTCUT_ID_SWIPE_RIGHT"
    private val IS_SHORTCUT_SWIPE_RIGHT = "IS_SHORTCUT_SWIPE_RIGHT"

    private val IS_FOLDER_1 = "IS_FOLDER_1"
    private val IS_FOLDER_2 = "IS_FOLDER_2"
    private val IS_FOLDER_3 = "IS_FOLDER_3"
    private val IS_FOLDER_4 = "IS_FOLDER_4"
    private val IS_FOLDER_5 = "IS_FOLDER_5"
    private val IS_FOLDER_6 = "IS_FOLDER_6"
    private val IS_FOLDER_7 = "IS_FOLDER_7"
    private val IS_FOLDER_8 = "IS_FOLDER_8"
    private val IS_FOLDER_9 = "IS_FOLDER_9"
    private val IS_FOLDER_10 = "IS_FOLDER_10"
    private val FOLDER_NAME_1 = "FOLDER_NAME_1"
    private val FOLDER_NAME_2 = "FOLDER_NAME_2"
    private val FOLDER_NAME_3 = "FOLDER_NAME_3"
    private val FOLDER_NAME_4 = "FOLDER_NAME_4"
    private val FOLDER_NAME_5 = "FOLDER_NAME_5"
    private val FOLDER_NAME_6 = "FOLDER_NAME_6"
    private val FOLDER_NAME_7 = "FOLDER_NAME_7"
    private val FOLDER_NAME_8 = "FOLDER_NAME_8"
    private val FOLDER_NAME_9 = "FOLDER_NAME_9"
    private val FOLDER_NAME_10 = "FOLDER_NAME_10"
    private val FOLDER_APPS_1 = "FOLDER_APPS_1"
    private val FOLDER_APPS_2 = "FOLDER_APPS_2"
    private val FOLDER_APPS_3 = "FOLDER_APPS_3"
    private val FOLDER_APPS_4 = "FOLDER_APPS_4"
    private val FOLDER_APPS_5 = "FOLDER_APPS_5"
    private val FOLDER_APPS_6 = "FOLDER_APPS_6"
    private val FOLDER_APPS_7 = "FOLDER_APPS_7"
    private val FOLDER_APPS_8 = "FOLDER_APPS_8"
    private val FOLDER_APPS_9 = "FOLDER_APPS_9"
    private val FOLDER_APPS_10 = "FOLDER_APPS_10"
    private val LAUNCH_HISTORY = "LAUNCH_HISTORY"
    private val PINNED_EXPIRIES = "PINNED_EXPIRIES"

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

    var appName1: String
        get() = prefs.getString(APP_NAME_1, "").toString()
        set(value) = prefs.edit { putString(APP_NAME_1, value).apply() }

    var appName2: String
        get() = prefs.getString(APP_NAME_2, "").toString()
        set(value) = prefs.edit { putString(APP_NAME_2, value).apply() }

    var appName3: String
        get() = prefs.getString(APP_NAME_3, "").toString()
        set(value) = prefs.edit { putString(APP_NAME_3, value).apply() }

    var appName4: String
        get() = prefs.getString(APP_NAME_4, "").toString()
        set(value) = prefs.edit { putString(APP_NAME_4, value).apply() }

    var appName5: String
        get() = prefs.getString(APP_NAME_5, "").toString()
        set(value) = prefs.edit { putString(APP_NAME_5, value).apply() }

    var appName6: String
        get() = prefs.getString(APP_NAME_6, "").toString()
        set(value) = prefs.edit { putString(APP_NAME_6, value).apply() }

    var appName7: String
        get() = prefs.getString(APP_NAME_7, "").toString()
        set(value) = prefs.edit { putString(APP_NAME_7, value).apply() }

    var appName8: String
        get() = prefs.getString(APP_NAME_8, "").toString()
        set(value) = prefs.edit { putString(APP_NAME_8, value).apply() }

    var appName9: String
        get() = prefs.getString(APP_NAME_9, "").toString()
        set(value) = prefs.edit { putString(APP_NAME_9, value).apply() }

    var appName10: String
        get() = prefs.getString(APP_NAME_10, "").toString()
        set(value) = prefs.edit { putString(APP_NAME_10, value).apply() }

    var appPackage1: String
        get() = prefs.getString(APP_PACKAGE_1, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_1, value).apply() }

    var appPackage2: String
        get() = prefs.getString(APP_PACKAGE_2, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_2, value).apply() }

    var appPackage3: String
        get() = prefs.getString(APP_PACKAGE_3, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_3, value).apply() }

    var appPackage4: String
        get() = prefs.getString(APP_PACKAGE_4, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_4, value).apply() }

    var appPackage5: String
        get() = prefs.getString(APP_PACKAGE_5, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_5, value).apply() }

    var appPackage6: String
        get() = prefs.getString(APP_PACKAGE_6, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_6, value).apply() }

    var appPackage7: String
        get() = prefs.getString(APP_PACKAGE_7, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_7, value).apply() }

    var appPackage8: String
        get() = prefs.getString(APP_PACKAGE_8, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_8, value).apply() }

    var appPackage9: String
        get() = prefs.getString(APP_PACKAGE_9, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_9, value).apply() }

    var appPackage10: String
        get() = prefs.getString(APP_PACKAGE_10, "").toString()
        set(value) = prefs.edit { putString(APP_PACKAGE_10, value).apply() }

    var appActivityClassName1: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_1, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_1, value).apply() }

    var appActivityClassName2: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_2, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_2, value).apply() }

    var appActivityClassName3: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_3, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_3, value).apply() }

    var appActivityClassName4: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_4, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_4, value).apply() }

    var appActivityClassName5: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_5, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_5, value).apply() }

    var appActivityClassName6: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_6, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_6, value).apply() }

    var appActivityClassName7: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_7, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_7, value).apply() }

    var appActivityClassName8: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_8, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_8, value).apply() }

    var appActivityClassName9: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_9, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_9, value).apply() }

    var appActivityClassName10: String?
        get() = prefs.getString(APP_ACTIVITY_CLASS_NAME_10, "").toString()
        set(value) = prefs.edit { putString(APP_ACTIVITY_CLASS_NAME_10, value).apply() }

    var appUser1: String
        get() = prefs.getString(APP_USER_1, "").toString()
        set(value) = prefs.edit { putString(APP_USER_1, value).apply() }

    var appUser2: String
        get() = prefs.getString(APP_USER_2, "").toString()
        set(value) = prefs.edit { putString(APP_USER_2, value).apply() }

    var appUser3: String
        get() = prefs.getString(APP_USER_3, "").toString()
        set(value) = prefs.edit { putString(APP_USER_3, value).apply() }

    var appUser4: String
        get() = prefs.getString(APP_USER_4, "").toString()
        set(value) = prefs.edit { putString(APP_USER_4, value).apply() }

    var appUser5: String
        get() = prefs.getString(APP_USER_5, "").toString()
        set(value) = prefs.edit { putString(APP_USER_5, value).apply() }

    var appUser6: String
        get() = prefs.getString(APP_USER_6, "").toString()
        set(value) = prefs.edit { putString(APP_USER_6, value).apply() }

    var appUser7: String
        get() = prefs.getString(APP_USER_7, "").toString()
        set(value) = prefs.edit { putString(APP_USER_7, value).apply() }

    var appUser8: String
        get() = prefs.getString(APP_USER_8, "").toString()
        set(value) = prefs.edit { putString(APP_USER_8, value).apply() }

    var appUser9: String
        get() = prefs.getString(APP_USER_9, "").toString()
        set(value) = prefs.edit { putString(APP_USER_9, value).apply() }

    var appUser10: String
        get() = prefs.getString(APP_USER_10, "").toString()
        set(value) = prefs.edit { putString(APP_USER_10, value).apply() }

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

    var isShortcut1: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_1, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_1, value) }

    var shortcutId1: String
        get() = prefs.getString(SHORTCUT_ID_1, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_1, value) }

    var isShortcut2: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_2, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_2, value) }

    var shortcutId2: String
        get() = prefs.getString(SHORTCUT_ID_2, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_2, value) }

    var isShortcut3: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_3, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_3, value) }

    var shortcutId3: String
        get() = prefs.getString(SHORTCUT_ID_3, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_3, value) }

    var isShortcut4: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_4, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_4, value) }

    var shortcutId4: String
        get() = prefs.getString(SHORTCUT_ID_4, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_4, value) }

    var isShortcut5: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_5, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_5, value) }

    var shortcutId5: String
        get() = prefs.getString(SHORTCUT_ID_5, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_5, value) }

    var isShortcut6: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_6, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_6, value) }

    var shortcutId6: String
        get() = prefs.getString(SHORTCUT_ID_6, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_6, value) }

    var isShortcut7: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_7, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_7, value) }

    var shortcutId7: String
        get() = prefs.getString(SHORTCUT_ID_7, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_7, value) }

    var isShortcut8: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_8, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_8, value) }

    var shortcutId8: String
        get() = prefs.getString(SHORTCUT_ID_8, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_8, value) }

    var isShortcut9: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_9, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_9, value) }

    var shortcutId9: String
        get() = prefs.getString(SHORTCUT_ID_9, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_9, value) }

    var isShortcut10: Boolean
        get() = prefs.getBoolean(IS_SHORTCUT_10, false)
        set(value) = prefs.edit { putBoolean(IS_SHORTCUT_10, value) }

    var shortcutId10: String
        get() = prefs.getString(SHORTCUT_ID_10, "").toString()
        set(value) = prefs.edit { putString(SHORTCUT_ID_10, value) }

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

    fun getAppName(location: Int): String {
        return when (location) {
            1 -> prefs.getString(APP_NAME_1, "").toString()
            2 -> prefs.getString(APP_NAME_2, "").toString()
            3 -> prefs.getString(APP_NAME_3, "").toString()
            4 -> prefs.getString(APP_NAME_4, "").toString()
            5 -> prefs.getString(APP_NAME_5, "").toString()
            6 -> prefs.getString(APP_NAME_6, "").toString()
            7 -> prefs.getString(APP_NAME_7, "").toString()
            8 -> prefs.getString(APP_NAME_8, "").toString()
            9 -> prefs.getString(APP_NAME_9, "").toString()
            10 -> prefs.getString(APP_NAME_10, "").toString()
            else -> ""
        }
    }

    fun getAppPackage(location: Int): String {
        return when (location) {
            1 -> prefs.getString(APP_PACKAGE_1, "").toString()
            2 -> prefs.getString(APP_PACKAGE_2, "").toString()
            3 -> prefs.getString(APP_PACKAGE_3, "").toString()
            4 -> prefs.getString(APP_PACKAGE_4, "").toString()
            5 -> prefs.getString(APP_PACKAGE_5, "").toString()
            6 -> prefs.getString(APP_PACKAGE_6, "").toString()
            7 -> prefs.getString(APP_PACKAGE_7, "").toString()
            8 -> prefs.getString(APP_PACKAGE_8, "").toString()
            9 -> prefs.getString(APP_PACKAGE_9, "").toString()
            10 -> prefs.getString(APP_PACKAGE_10, "").toString()
            else -> ""
        }
    }

    fun getAppActivityClassName(location: Int): String {
        return when (location) {
            1 -> prefs.getString(APP_ACTIVITY_CLASS_NAME_1, "").toString()
            2 -> prefs.getString(APP_ACTIVITY_CLASS_NAME_2, "").toString()
            3 -> prefs.getString(APP_ACTIVITY_CLASS_NAME_3, "").toString()
            4 -> prefs.getString(APP_ACTIVITY_CLASS_NAME_4, "").toString()
            5 -> prefs.getString(APP_ACTIVITY_CLASS_NAME_5, "").toString()
            6 -> prefs.getString(APP_ACTIVITY_CLASS_NAME_6, "").toString()
            7 -> prefs.getString(APP_ACTIVITY_CLASS_NAME_7, "").toString()
            8 -> prefs.getString(APP_ACTIVITY_CLASS_NAME_8, "").toString()
            9 -> prefs.getString(APP_ACTIVITY_CLASS_NAME_9, "").toString()
            10 -> prefs.getString(APP_ACTIVITY_CLASS_NAME_10, "").toString()
            else -> ""
        }
    }

    fun getAppUser(location: Int): String {
        return when (location) {
            1 -> prefs.getString(APP_USER_1, "").toString()
            2 -> prefs.getString(APP_USER_2, "").toString()
            3 -> prefs.getString(APP_USER_3, "").toString()
            4 -> prefs.getString(APP_USER_4, "").toString()
            5 -> prefs.getString(APP_USER_5, "").toString()
            6 -> prefs.getString(APP_USER_6, "").toString()
            7 -> prefs.getString(APP_USER_7, "").toString()
            8 -> prefs.getString(APP_USER_8, "").toString()
            9 -> prefs.getString(APP_USER_9, "").toString()
            10 -> prefs.getString(APP_USER_10, "").toString()
            else -> ""
        }
    }

    fun getShortcutId(location: Int): String {
        return when (location) {
            1 -> shortcutId1
            2 -> shortcutId2
            3 -> shortcutId3
            4 -> shortcutId4
            5 -> shortcutId5
            6 -> shortcutId6
            7 -> shortcutId7
            8 -> shortcutId8
            9 -> shortcutId9
            10 -> shortcutId10
            else -> ""
        }
    }

    fun getIsShortcut(location: Int): Boolean {
        return when (location) {
            1 -> isShortcut1
            2 -> isShortcut2
            3 -> isShortcut3
            4 -> isShortcut4
            5 -> isShortcut5
            6 -> isShortcut6
            7 -> isShortcut7
            8 -> isShortcut8
            9 -> isShortcut9
            10 -> isShortcut10
            else -> false
        }
    }

    fun setAppActivityClassName(location: Int, activityClassName: String) {
        when (location) {
            1 -> appActivityClassName1 = activityClassName
            2 -> appActivityClassName2 = activityClassName
            3 -> appActivityClassName3 = activityClassName
            4 -> appActivityClassName4 = activityClassName
            5 -> appActivityClassName5 = activityClassName
            6 -> appActivityClassName6 = activityClassName
            7 -> appActivityClassName7 = activityClassName
            8 -> appActivityClassName8 = activityClassName
            9 -> appActivityClassName9 = activityClassName
            10 -> appActivityClassName10 = activityClassName
        }
    }

    fun updateAppActivityClassName(packageName: String, activityClassName: String) {
        for (i in 1..10) {
            if (getAppPackage(i) == packageName) setAppActivityClassName(i, activityClassName)
        }
        if (clockAppPackage == packageName) clockAppClassName = activityClassName
        if (calendarAppPackage == packageName) calendarAppClassName = activityClassName
        if (screenTimeAppPackage == packageName) screenTimeAppClassName = activityClassName
        if (appPackageSwipeLeft == packageName) appActivityClassNameSwipeLeft = activityClassName
        if (appPackageSwipeRight == packageName) appActivityClassNameRight = activityClassName
    }

    fun getAppRenameLabel(appPackage: String): String = prefs.getString(appPackage, "").toString()

    fun setAppRenameLabel(appPackage: String, renameLabel: String) = prefs.edit { putString(appPackage, renameLabel) }

    fun isFolder(slot: Int): Boolean {
        return when (slot) {
            1 -> prefs.getBoolean(IS_FOLDER_1, false)
            2 -> prefs.getBoolean(IS_FOLDER_2, false)
            3 -> prefs.getBoolean(IS_FOLDER_3, false)
            4 -> prefs.getBoolean(IS_FOLDER_4, false)
            5 -> prefs.getBoolean(IS_FOLDER_5, false)
            6 -> prefs.getBoolean(IS_FOLDER_6, false)
            7 -> prefs.getBoolean(IS_FOLDER_7, false)
            8 -> prefs.getBoolean(IS_FOLDER_8, false)
            9 -> prefs.getBoolean(IS_FOLDER_9, false)
            10 -> prefs.getBoolean(IS_FOLDER_10, false)
            else -> false
        }
    }

    fun setIsFolder(slot: Int, isFolder: Boolean) {
        when (slot) {
            1 -> prefs.edit { putBoolean(IS_FOLDER_1, isFolder) }
            2 -> prefs.edit { putBoolean(IS_FOLDER_2, isFolder) }
            3 -> prefs.edit { putBoolean(IS_FOLDER_3, isFolder) }
            4 -> prefs.edit { putBoolean(IS_FOLDER_4, isFolder) }
            5 -> prefs.edit { putBoolean(IS_FOLDER_5, isFolder) }
            6 -> prefs.edit { putBoolean(IS_FOLDER_6, isFolder) }
            7 -> prefs.edit { putBoolean(IS_FOLDER_7, isFolder) }
            8 -> prefs.edit { putBoolean(IS_FOLDER_8, isFolder) }
            9 -> prefs.edit { putBoolean(IS_FOLDER_9, isFolder) }
            10 -> prefs.edit { putBoolean(IS_FOLDER_10, isFolder) }
        }
    }

    fun getFolderName(slot: Int): String {
        return when (slot) {
            1 -> prefs.getString(FOLDER_NAME_1, "").toString()
            2 -> prefs.getString(FOLDER_NAME_2, "").toString()
            3 -> prefs.getString(FOLDER_NAME_3, "").toString()
            4 -> prefs.getString(FOLDER_NAME_4, "").toString()
            5 -> prefs.getString(FOLDER_NAME_5, "").toString()
            6 -> prefs.getString(FOLDER_NAME_6, "").toString()
            7 -> prefs.getString(FOLDER_NAME_7, "").toString()
            8 -> prefs.getString(FOLDER_NAME_8, "").toString()
            9 -> prefs.getString(FOLDER_NAME_9, "").toString()
            10 -> prefs.getString(FOLDER_NAME_10, "").toString()
            else -> ""
        }
    }

    fun setFolderName(slot: Int, name: String) {
        when (slot) {
            1 -> prefs.edit { putString(FOLDER_NAME_1, name) }
            2 -> prefs.edit { putString(FOLDER_NAME_2, name) }
            3 -> prefs.edit { putString(FOLDER_NAME_3, name) }
            4 -> prefs.edit { putString(FOLDER_NAME_4, name) }
            5 -> prefs.edit { putString(FOLDER_NAME_5, name) }
            6 -> prefs.edit { putString(FOLDER_NAME_6, name) }
            7 -> prefs.edit { putString(FOLDER_NAME_7, name) }
            8 -> prefs.edit { putString(FOLDER_NAME_8, name) }
            9 -> prefs.edit { putString(FOLDER_NAME_9, name) }
            10 -> prefs.edit { putString(FOLDER_NAME_10, name) }
        }
    }

    fun getFolderApps(slot: Int): MutableList<AppModel.FolderApp?> {
        val raw = when (slot) {
            1 -> prefs.getString(FOLDER_APPS_1, "").toString()
            2 -> prefs.getString(FOLDER_APPS_2, "").toString()
            3 -> prefs.getString(FOLDER_APPS_3, "").toString()
            4 -> prefs.getString(FOLDER_APPS_4, "").toString()
            5 -> prefs.getString(FOLDER_APPS_5, "").toString()
            6 -> prefs.getString(FOLDER_APPS_6, "").toString()
            7 -> prefs.getString(FOLDER_APPS_7, "").toString()
            8 -> prefs.getString(FOLDER_APPS_8, "").toString()
            9 -> prefs.getString(FOLDER_APPS_9, "").toString()
            10 -> prefs.getString(FOLDER_APPS_10, "").toString()
            else -> ""
        }
        val apps = MutableList<AppModel.FolderApp?>(Constants.MAX_APPS_IN_FOLDER) { null }
        if (raw.isBlank()) return apps
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length().coerceAtMost(Constants.MAX_APPS_IN_FOLDER)) {
                val obj = array.optJSONObject(i) ?: continue
                apps[i] = AppModel.FolderApp(
                    appLabel = obj.optString("name"),
                    appPackage = obj.optString("package"),
                    activityClassName = obj.optString("activity").takeIf { it.isNotBlank() },
                    user = obj.optString("user"),
                    isShortcut = obj.optBoolean("isShortcut"),
                    shortcutId = obj.optString("shortcutId"),
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return apps
    }

    fun setFolderApp(slot: Int, position: Int, app: AppModel.FolderApp) {
        val apps = getFolderApps(slot)
        if (position !in apps.indices) return
        apps[position] = app
        saveFolderApps(slot, apps)
    }

    fun removeFolderApp(slot: Int, position: Int) {
        val apps = getFolderApps(slot)
        if (position !in apps.indices) return
        apps[position] = null
        saveFolderApps(slot, apps)
    }

    fun renameFolderApp(slot: Int, position: Int, newLabel: String) {
        val apps = getFolderApps(slot)
        val app = apps.getOrNull(position) ?: return
        if (app.appPackage.isEmpty()) return
        apps[position] = app.copy(appLabel = newLabel)
        saveFolderApps(slot, apps)
    }

    private fun saveFolderApps(slot: Int, apps: List<AppModel.FolderApp?>) {
        val array = JSONArray()
        for (app in apps) {
            if (app == null) {
                array.put(JSONObject.NULL)
            } else {
                array.put(JSONObject().apply {
                    put("name", app.appLabel)
                    put("package", app.appPackage)
                    put("activity", app.activityClassName ?: "")
                    put("user", app.user)
                    put("isShortcut", app.isShortcut)
                    put("shortcutId", app.shortcutId)
                })
            }
        }
        when (slot) {
            1 -> prefs.edit { putString(FOLDER_APPS_1, array.toString()) }
            2 -> prefs.edit { putString(FOLDER_APPS_2, array.toString()) }
            3 -> prefs.edit { putString(FOLDER_APPS_3, array.toString()) }
            4 -> prefs.edit { putString(FOLDER_APPS_4, array.toString()) }
            5 -> prefs.edit { putString(FOLDER_APPS_5, array.toString()) }
            6 -> prefs.edit { putString(FOLDER_APPS_6, array.toString()) }
            7 -> prefs.edit { putString(FOLDER_APPS_7, array.toString()) }
            8 -> prefs.edit { putString(FOLDER_APPS_8, array.toString()) }
            9 -> prefs.edit { putString(FOLDER_APPS_9, array.toString()) }
            10 -> prefs.edit { putString(FOLDER_APPS_10, array.toString()) }
        }
    }

    fun clearFolder(slot: Int) {
        setIsFolder(slot, false)
        setFolderName(slot, "")
        saveFolderApps(slot, MutableList(Constants.MAX_APPS_IN_FOLDER) { null })
        clearPin(slot)
    }

    fun firstEmptyHomePosition(): Int {
        for (i in 1..Constants.MAX_HOME_APPS) {
            if (getAppName(i).isEmpty() && isFolder(i).not()) return i
        }
        return 0
    }

    fun clearHomeSlot(slot: Int) {
        when (slot) {
            1 -> {
                appName1 = ""; appPackage1 = ""; appUser1 = ""
                appActivityClassName1 = ""; isShortcut1 = false; shortcutId1 = ""
            }

            2 -> {
                appName2 = ""; appPackage2 = ""; appUser2 = ""
                appActivityClassName2 = ""; isShortcut2 = false; shortcutId2 = ""
            }

            3 -> {
                appName3 = ""; appPackage3 = ""; appUser3 = ""
                appActivityClassName3 = ""; isShortcut3 = false; shortcutId3 = ""
            }

            4 -> {
                appName4 = ""; appPackage4 = ""; appUser4 = ""
                appActivityClassName4 = ""; isShortcut4 = false; shortcutId4 = ""
            }

            5 -> {
                appName5 = ""; appPackage5 = ""; appUser5 = ""
                appActivityClassName5 = ""; isShortcut5 = false; shortcutId5 = ""
            }

            6 -> {
                appName6 = ""; appPackage6 = ""; appUser6 = ""
                appActivityClassName6 = ""; isShortcut6 = false; shortcutId6 = ""
            }

            7 -> {
                appName7 = ""; appPackage7 = ""; appUser7 = ""
                appActivityClassName7 = ""; isShortcut7 = false; shortcutId7 = ""
            }

            8 -> {
                appName8 = ""; appPackage8 = ""; appUser8 = ""
                appActivityClassName8 = ""; isShortcut8 = false; shortcutId8 = ""
            }

            9 -> {
                appName9 = ""; appPackage9 = ""; appUser9 = ""
                appActivityClassName9 = ""; isShortcut9 = false; shortcutId9 = ""
            }

            10 -> {
                appName10 = ""; appPackage10 = ""; appUser10 = ""
                appActivityClassName10 = ""; isShortcut10 = false; shortcutId10 = ""
            }
        }
        setIsFolder(slot, false)
        setFolderName(slot, "")
        saveFolderApps(slot, MutableList(Constants.MAX_APPS_IN_FOLDER) { null })
        clearPin(slot)
    }

    fun getPinExpiry(slot: Int): Long {
        return getPinExpiriesMap()[slot.toString()] ?: 0L
    }

    fun setPinExpiry(slot: Int, expiry: Long) {
        val map = getPinExpiriesMap().toMutableMap()
        if (expiry > 0) map[slot.toString()] = expiry else map.remove(slot.toString())
        prefs.edit { putString(PINNED_EXPIRIES, JSONObject(map).toString()) }
    }

    fun clearPin(slot: Int) = setPinExpiry(slot, 0)

    fun swapPinExpiries(slot1: Int, slot2: Int) {
        val expiry1 = getPinExpiry(slot1)
        val expiry2 = getPinExpiry(slot2)
        setPinExpiry(slot1, expiry2)
        setPinExpiry(slot2, expiry1)
    }

    private fun getPinExpiriesMap(): Map<String, Long> {
        val raw = prefs.getString(PINNED_EXPIRIES, "").toString()
        if (raw.isBlank()) return emptyMap()
        return try {
            val obj = JSONObject(raw)
            val map = mutableMapOf<String, Long>()
            obj.keys().forEach { key -> map[key] = obj.optLong(key) }
            map
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }

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

    fun swapHomeSlots(slot1: Int, slot2: Int) {
        if (slot1 !in 1..Constants.MAX_HOME_APPS || slot2 !in 1..Constants.MAX_HOME_APPS || slot1 == slot2) return

        val name1 = getAppName(slot1); val name2 = getAppName(slot2)
        val package1 = getAppPackage(slot1); val package2 = getAppPackage(slot2)
        val activity1 = getAppActivityClassName(slot1); val activity2 = getAppActivityClassName(slot2)
        val user1 = getAppUser(slot1); val user2 = getAppUser(slot2)
        val isShortcut1 = getIsShortcut(slot1); val isShortcut2 = getIsShortcut(slot2)
        val shortcutId1 = getShortcutId(slot1); val shortcutId2 = getShortcutId(slot2)
        val isFolder1 = isFolder(slot1); val isFolder2 = isFolder(slot2)
        val folderName1 = getFolderName(slot1); val folderName2 = getFolderName(slot2)
        val folderApps1 = prefs.getString(folderAppsKey(slot1), "").toString()
        val folderApps2 = prefs.getString(folderAppsKey(slot2), "").toString()

        setAppData(slot1, name2, package2, activity2, user2, isShortcut2, shortcutId2, isFolder2, folderName2, folderApps2)
        setAppData(slot2, name1, package1, activity1, user1, isShortcut1, shortcutId1, isFolder1, folderName1, folderApps1)
        swapPinExpiries(slot1, slot2)
    }

    fun swapFolderApps(slot: Int, position1: Int, position2: Int) {
        val apps = getFolderApps(slot)
        if (position1 !in apps.indices || position2 !in apps.indices || position1 == position2) return
        val temp = apps[position1]
        apps[position1] = apps[position2]
        apps[position2] = temp
        saveFolderApps(slot, apps)
    }

    private fun folderAppsKey(slot: Int): String {
        return when (slot) {
            1 -> FOLDER_APPS_1
            2 -> FOLDER_APPS_2
            3 -> FOLDER_APPS_3
            4 -> FOLDER_APPS_4
            5 -> FOLDER_APPS_5
            6 -> FOLDER_APPS_6
            7 -> FOLDER_APPS_7
            8 -> FOLDER_APPS_8
            9 -> FOLDER_APPS_9
            10 -> FOLDER_APPS_10
            else -> FOLDER_APPS_1
        }
    }

    private fun setAppData(
        slot: Int,
        name: String,
        packageName: String,
        activityClassName: String,
        user: String,
        isShortcut: Boolean,
        shortcutId: String,
        isFolder: Boolean,
        folderName: String,
        folderApps: String,
    ) {
        when (slot) {
            1 -> {
                appName1 = name; appPackage1 = packageName; appActivityClassName1 = activityClassName
                appUser1 = user; isShortcut1 = isShortcut; shortcutId1 = shortcutId
                setIsFolder(1, isFolder); setFolderName(1, folderName)
                prefs.edit { putString(FOLDER_APPS_1, folderApps) }
            }

            2 -> {
                appName2 = name; appPackage2 = packageName; appActivityClassName2 = activityClassName
                appUser2 = user; isShortcut2 = isShortcut; shortcutId2 = shortcutId
                setIsFolder(2, isFolder); setFolderName(2, folderName)
                prefs.edit { putString(FOLDER_APPS_2, folderApps) }
            }

            3 -> {
                appName3 = name; appPackage3 = packageName; appActivityClassName3 = activityClassName
                appUser3 = user; isShortcut3 = isShortcut; shortcutId3 = shortcutId
                setIsFolder(3, isFolder); setFolderName(3, folderName)
                prefs.edit { putString(FOLDER_APPS_3, folderApps) }
            }

            4 -> {
                appName4 = name; appPackage4 = packageName; appActivityClassName4 = activityClassName
                appUser4 = user; isShortcut4 = isShortcut; shortcutId4 = shortcutId
                setIsFolder(4, isFolder); setFolderName(4, folderName)
                prefs.edit { putString(FOLDER_APPS_4, folderApps) }
            }

            5 -> {
                appName5 = name; appPackage5 = packageName; appActivityClassName5 = activityClassName
                appUser5 = user; isShortcut5 = isShortcut; shortcutId5 = shortcutId
                setIsFolder(5, isFolder); setFolderName(5, folderName)
                prefs.edit { putString(FOLDER_APPS_5, folderApps) }
            }

            6 -> {
                appName6 = name; appPackage6 = packageName; appActivityClassName6 = activityClassName
                appUser6 = user; isShortcut6 = isShortcut; shortcutId6 = shortcutId
                setIsFolder(6, isFolder); setFolderName(6, folderName)
                prefs.edit { putString(FOLDER_APPS_6, folderApps) }
            }

            7 -> {
                appName7 = name; appPackage7 = packageName; appActivityClassName7 = activityClassName
                appUser7 = user; isShortcut7 = isShortcut; shortcutId7 = shortcutId
                setIsFolder(7, isFolder); setFolderName(7, folderName)
                prefs.edit { putString(FOLDER_APPS_7, folderApps) }
            }

            8 -> {
                appName8 = name; appPackage8 = packageName; appActivityClassName8 = activityClassName
                appUser8 = user; isShortcut8 = isShortcut; shortcutId8 = shortcutId
                setIsFolder(8, isFolder); setFolderName(8, folderName)
                prefs.edit { putString(FOLDER_APPS_8, folderApps) }
            }

            9 -> {
                appName9 = name; appPackage9 = packageName; appActivityClassName9 = activityClassName
                appUser9 = user; isShortcut9 = isShortcut; shortcutId9 = shortcutId
                setIsFolder(9, isFolder); setFolderName(9, folderName)
                prefs.edit { putString(FOLDER_APPS_9, folderApps) }
            }

            10 -> {
                appName10 = name; appPackage10 = packageName; appActivityClassName10 = activityClassName
                appUser10 = user; isShortcut10 = isShortcut; shortcutId10 = shortcutId
                setIsFolder(10, isFolder); setFolderName(10, folderName)
                prefs.edit { putString(FOLDER_APPS_10, folderApps) }
            }
        }
    }
}
