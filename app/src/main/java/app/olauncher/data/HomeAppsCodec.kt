package app.olauncher.data

import org.json.JSONArray
import org.json.JSONObject

object HomeAppsCodec {

    fun encodeHomeApps(apps: List<AppModel.HomeApp>): String {
        val array = JSONArray()
        for (app in apps) {
            array.put(encodeHomeApp(app))
        }
        return array.toString()
    }

    fun decodeHomeApps(raw: String): List<AppModel.HomeApp> {
        if (raw.isBlank()) return emptyList()
        val result = mutableListOf<AppModel.HomeApp>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.optJSONObject(i) ?: continue
                result.add(decodeHomeApp(obj))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun encodePinnedApps(apps: List<AppModel.PinnedApp>): String {
        val array = JSONArray()
        for (app in apps) {
            array.put(encodePinnedApp(app))
        }
        return array.toString()
    }

    fun decodePinnedApps(raw: String): List<AppModel.PinnedApp> {
        if (raw.isBlank()) return emptyList()
        val result = mutableListOf<AppModel.PinnedApp>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.optJSONObject(i) ?: continue
                result.add(decodePinnedApp(obj))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun encodeFolderApps(apps: List<AppModel.FolderApp>): String {
        val array = JSONArray()
        for (app in apps) {
            array.put(encodeFolderApp(app))
        }
        return array.toString()
    }

    fun decodeFolderApps(raw: String): List<AppModel.FolderApp> {
        if (raw.isBlank()) return emptyList()
        val result = mutableListOf<AppModel.FolderApp>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.optJSONObject(i) ?: continue
                result.add(decodeFolderApp(obj))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun encodeHomeApp(app: AppModel.HomeApp): JSONObject {
        return JSONObject().apply {
            put("isFolder", app.isFolder)
            if (app.isFolder) {
                put("folderName", app.folderName)
                val folderApps = JSONArray()
                for (folderApp in app.folderApps) {
                    folderApps.put(encodeFolderApp(folderApp))
                }
                put("folderApps", folderApps)
            } else {
                put("name", app.appLabel)
                put("package", app.appPackage)
                put("activity", app.activityClassName ?: "")
                put("user", app.user)
                put("isShortcut", app.isShortcut)
                put("shortcutId", app.shortcutId)
            }
        }
    }

    private fun decodeHomeApp(obj: JSONObject): AppModel.HomeApp {
        if (obj.optBoolean("isFolder")) {
            return AppModel.HomeApp(
                isFolder = true,
                folderName = obj.optString("folderName"),
                folderApps = decodeFolderApps(obj.optJSONArray("folderApps").toString()),
            )
        }
        return AppModel.HomeApp(
            appLabel = obj.optString("name"),
            appPackage = obj.optString("package"),
            activityClassName = obj.optString("activity").takeIf { it.isNotBlank() },
            user = obj.optString("user"),
            isShortcut = obj.optBoolean("isShortcut"),
            shortcutId = obj.optString("shortcutId"),
        )
    }

    private fun encodePinnedApp(app: AppModel.PinnedApp): JSONObject {
        return JSONObject().apply {
            put("name", app.appLabel)
            put("package", app.appPackage)
            put("activity", app.activityClassName ?: "")
            put("user", app.user)
            put("isShortcut", app.isShortcut)
            put("shortcutId", app.shortcutId)
            put("expiresAt", app.expiresAt)
        }
    }

    private fun decodePinnedApp(obj: JSONObject): AppModel.PinnedApp {
        return AppModel.PinnedApp(
            appLabel = obj.optString("name"),
            appPackage = obj.optString("package"),
            activityClassName = obj.optString("activity").takeIf { it.isNotBlank() },
            user = obj.optString("user"),
            isShortcut = obj.optBoolean("isShortcut"),
            shortcutId = obj.optString("shortcutId"),
            expiresAt = obj.optLong("expiresAt", 0L),
        )
    }

    private fun encodeFolderApp(app: AppModel.FolderApp): JSONObject {
        return JSONObject().apply {
            put("name", app.appLabel)
            put("package", app.appPackage)
            put("activity", app.activityClassName ?: "")
            put("user", app.user)
            put("isShortcut", app.isShortcut)
            put("shortcutId", app.shortcutId)
        }
    }

    private fun decodeFolderApp(obj: JSONObject): AppModel.FolderApp {
        return AppModel.FolderApp(
            appLabel = obj.optString("name"),
            appPackage = obj.optString("package"),
            activityClassName = obj.optString("activity").takeIf { it.isNotBlank() },
            user = obj.optString("user"),
            isShortcut = obj.optBoolean("isShortcut"),
            shortcutId = obj.optString("shortcutId"),
        )
    }
}
