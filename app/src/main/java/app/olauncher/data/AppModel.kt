package app.olauncher.data

import android.os.UserHandle
import java.text.CollationKey

sealed class AppModel : Comparable<AppModel> {
    abstract val appLabel: String
    abstract val key: CollationKey?
    abstract val appPackage: String
    abstract val user: UserHandle
    abstract val isNew: Boolean

    data class App(
        override val appLabel: String,
        override val key: CollationKey?,
        override val appPackage: String,
        val activityClassName: String?,
        override val isNew: Boolean = false,
        override val user: UserHandle,
    ) : AppModel()

    data class PinnedShortcut(
        override val appLabel: String,
        override val key: CollationKey?,
        override val appPackage: String,
        val shortcutId: String,
        override val isNew: Boolean = false,
        override val user: UserHandle,
    ) : AppModel()

    data class PrivateSpaceHeader(
        val isLocked: Boolean = true,
        override val user: UserHandle = android.os.Process.myUserHandle(),
    ) : AppModel() {
        override val appLabel: String = ""
        override val key: CollationKey? = null
        override val appPackage: String = ""
        override val isNew: Boolean = false
    }

    data class FolderApp(
        val appLabel: String,
        val appPackage: String,
        val activityClassName: String?,
        val user: String,
        val isShortcut: Boolean = false,
        val shortcutId: String = "",
    )

    data class PinnedApp(
        val appLabel: String,
        val appPackage: String,
        val activityClassName: String?,
        val user: String,
        val isShortcut: Boolean = false,
        val shortcutId: String = "",
        val expiresAt: Long,
    )

    data class HomeApp(
        val appLabel: String = "",
        val appPackage: String = "",
        val activityClassName: String? = null,
        val user: String = "",
        val isShortcut: Boolean = false,
        val shortcutId: String = "",
        val isFolder: Boolean = false,
        val folderName: String = "",
        val folderApps: List<FolderApp> = emptyList(),
    )

    override fun compareTo(other: AppModel): Int = when {
        key != null && other.key != null -> key!!.compareTo(other.key)
        else -> appLabel.compareTo(other.appLabel, true)
    }
}