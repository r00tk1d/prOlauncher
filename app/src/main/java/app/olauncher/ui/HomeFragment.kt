package app.olauncher.ui

import android.animation.LayoutTransition
import android.app.admin.DevicePolicyManager
import android.app.AlertDialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.content.res.Configuration
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import app.olauncher.MainViewModel
import app.olauncher.R
import app.olauncher.data.AppModel
import app.olauncher.data.Constants
import app.olauncher.data.Prefs
import app.olauncher.databinding.FragmentHomeBinding
import app.olauncher.helper.appUsagePermissionGranted
import app.olauncher.helper.dpToPx
import app.olauncher.helper.expandNotificationDrawer
import app.olauncher.helper.getChangedAppTheme
import app.olauncher.helper.getUserHandleFromString
import app.olauncher.helper.isPackageInstalled
import app.olauncher.helper.openAlarmApp
import app.olauncher.helper.openCalendar
import app.olauncher.helper.openCameraApp
import app.olauncher.helper.openDialerApp
import app.olauncher.helper.openSearch
import app.olauncher.helper.setPlainWallpaperByTheme
import app.olauncher.helper.showKeyboard
import app.olauncher.helper.showToast
import app.olauncher.listener.OnSwipeTouchListener
import app.olauncher.listener.ViewSwipeTouchListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : BaseFragment(), View.OnClickListener, View.OnLongClickListener {

    private lateinit var prefs: Prefs
    private lateinit var viewModel: MainViewModel
    private lateinit var deviceManager: DevicePolicyManager

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var draggedIndex = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = Prefs(requireContext())
        viewModel = activity?.run {
            ViewModelProvider(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        deviceManager = context?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        initObservers()
        setHomeAlignment(prefs.homeAlignment)
        initSwipeTouchListener()
        initClickListeners()
    }

    override fun onResume() {
        super.onResume()
        populateHomeScreen(false)
        viewModel.isOlauncherDefault()
        if (prefs.showStatusBar) showStatusBar()
        else hideStatusBar()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.lock -> {}
            // Home button for recents feature disabled
            // R.id.recents -> {}
            R.id.clock -> openClockApp()
            R.id.date -> openCalendarApp()
            R.id.setDefaultLauncher -> viewModel.resetLauncherLiveData.call()
            R.id.tvScreenTime -> openScreenTimeDigitalWellbeing()
            R.id.tvHomeHint -> showAppList(
                Constants.FLAG_SET_HOME_APP,
                position = prefs.getHomeApps().size,
                includeHiddenApps = true,
            )

            else -> {
                try { // Launch app
                    val index = view.tag.toString().toInt()
                    if (prefs.getHomeApps().getOrNull(index)?.isFolder == true)
                        openFolder(index)
                    else
                        homeAppClicked(index)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun openClockApp() {
        if (prefs.clockAppPackage.isBlank())
            openAlarmApp(requireContext())
        else
            launchApp(
                "Clock",
                prefs.clockAppPackage,
                prefs.clockAppClassName,
                prefs.clockAppUser
            )
    }

    private fun openCalendarApp() {
        if (prefs.calendarAppPackage.isBlank())
            openCalendar(requireContext())
        else
            launchApp(
                "Calendar",
                prefs.calendarAppPackage,
                prefs.calendarAppClassName,
                prefs.calendarAppUser
            )
    }

    override fun onLongClick(view: View): Boolean {
        when (view.id) {
            R.id.clock -> {
                showAppList(Constants.FLAG_SET_CLOCK_APP)
                prefs.clockAppPackage = ""
                prefs.clockAppClassName = ""
                prefs.clockAppUser = ""
            }

            R.id.date -> {
                showAppList(Constants.FLAG_SET_CALENDAR_APP)
                prefs.calendarAppPackage = ""
                prefs.calendarAppClassName = ""
                prefs.calendarAppUser = ""
            }

            R.id.tvScreenTime -> {
                showAppList(Constants.FLAG_SET_SCREEN_TIME_APP)
                prefs.screenTimeAppPackage = ""
                prefs.screenTimeAppClassName = ""
                prefs.screenTimeAppUser = ""
            }

            R.id.setDefaultLauncher -> {
                prefs.hideSetDefaultLauncher = true
                binding.setDefaultLauncher.visibility = View.GONE
                if (viewModel.isOlauncherDefault.value != true) {
                    requireContext().showToast(R.string.set_as_default_launcher)
                    findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                }
            }

            else -> {
                try {
                    showSlotMenu(view.tag.toString().toInt())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return true
    }

    private fun initObservers() {
        if (prefs.firstSettingsOpen) {
            binding.firstRunTips.visibility = View.VISIBLE
            binding.setDefaultLauncher.visibility = View.GONE
        } else binding.firstRunTips.visibility = View.GONE

        viewModel.refreshHome.observe(viewLifecycleOwner) {
            populateHomeScreen(it)
        }
        viewModel.isOlauncherDefault.observe(viewLifecycleOwner, Observer {
            if (it != true) {
                if (prefs.dailyWallpaper && prefs.appTheme == AppCompatDelegate.MODE_NIGHT_YES) {
                    prefs.dailyWallpaper = false
                    viewModel.cancelWallpaperWorker()
                }
                prefs.homeBottomAlignment = false
                setHomeAlignment()
            }
            if (binding.firstRunTips.isVisible) return@Observer
            binding.setDefaultLauncher.isVisible = it.not() && prefs.hideSetDefaultLauncher.not()
        })
        viewModel.homeAppAlignment.observe(viewLifecycleOwner) {
            setHomeAlignment(it)
        }
        viewModel.toggleDateTime.observe(viewLifecycleOwner) {
            populateDateTime()
        }
        viewModel.screenTimeValue.observe(viewLifecycleOwner) {
            it?.let { binding.tvScreenTime.text = it }
        }
        // Home button for recents feature disabled
        // viewModel.showRecentApps.observe(viewLifecycleOwner) {
        //     binding.recents.performClick()
        // }
    }

    private fun initSwipeTouchListener() {
        val context = requireContext()
        binding.mainLayout.setOnTouchListener(getSwipeGestureListener(context))
    }

    private fun initClickListeners() {
        binding.lock.setOnClickListener(this)
        // Home button for recents feature disabled
        // binding.recents.setOnClickListener(this)
        binding.clock.setOnClickListener(this)
        binding.date.setOnClickListener(this)
        binding.clock.setOnLongClickListener(this)
        binding.date.setOnLongClickListener(this)
        binding.setDefaultLauncher.setOnClickListener(this)
        binding.setDefaultLauncher.setOnLongClickListener(this)
        binding.tvScreenTime.setOnClickListener(this)
        binding.tvScreenTime.setOnLongClickListener(this)
        binding.tvHomeHint.setOnClickListener(this)
    }

    private fun getHomeSlotDragListener(): View.OnDragListener {
        return View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> true

                DragEvent.ACTION_DRAG_ENTERED -> {
                    val target = view.tag.toString().toInt()
                    if (draggedIndex != -1 && draggedIndex != target) {
                        prefs.swapHomeApps(draggedIndex, target)
                        refreshHomeSlotText(draggedIndex)
                        refreshHomeSlotText(target)
                        draggedIndex = target
                    }
                    true
                }

                DragEvent.ACTION_DROP -> {
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    draggedIndex = -1
                    binding.root.post { binding.root.layoutTransition = LayoutTransition() }
                    true
                }

                else -> false
            }
        }
    }

    private fun refreshHomeSlotText(index: Int) {
        val textView = binding.homeAppsLayout.getChildAt(index) as? TextView ?: return
        val homeApp = prefs.getHomeApps().getOrNull(index) ?: return
        if (homeApp.isFolder) {
            val name = homeApp.folderName.ifBlank { getString(R.string.folder) }
            textView.text = "\u25B8 $name"
        } else {
            textView.text = homeApp.appLabel
        }
    }

    private fun setHomeAlignment(horizontalGravity: Int = prefs.homeAlignment) {
        val verticalGravity = if (prefs.homeBottomAlignment) Gravity.BOTTOM else Gravity.CENTER_VERTICAL
        binding.homeAppsLayout.gravity = horizontalGravity or verticalGravity
        binding.dateTimeLayout.gravity = horizontalGravity
        for (i in 0 until binding.homeAppsLayout.childCount) {
            (binding.homeAppsLayout.getChildAt(i) as? TextView)?.gravity = horizontalGravity
        }
    }

    private fun populateDateTime() {
        binding.dateTimeLayout.isVisible = prefs.dateTimeVisibility != Constants.DateTime.OFF
        binding.clock.isVisible = Constants.DateTime.isTimeVisible(prefs.dateTimeVisibility)
        binding.date.isVisible = Constants.DateTime.isDateVisible(prefs.dateTimeVisibility)

//        var dateText = SimpleDateFormat("EEE, d MMM", Locale.getDefault()).format(Date())
        val dateFormat = SimpleDateFormat("EEE, d MMM", Locale.getDefault())
        var dateText = dateFormat.format(Date())

        if (!prefs.showStatusBar) {
            val battery = (requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager)
                .getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (battery > 0)
                dateText = getString(R.string.day_battery, dateText, battery)
        }
        binding.date.text = dateText.replace(".,", ",")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun populateScreenTime() {
        if (requireContext().appUsagePermissionGranted().not()) return

        viewModel.getTodaysScreenTime()
        binding.tvScreenTime.visibility = View.VISIBLE

        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val horizontalMargin = if (isLandscape) 64.dpToPx() else 10.dpToPx()
        val marginTop = if (isLandscape) {
            if (prefs.dateTimeVisibility == Constants.DateTime.DATE_ONLY) 36.dpToPx() else 56.dpToPx()
        } else {
            if (prefs.dateTimeVisibility == Constants.DateTime.DATE_ONLY) 45.dpToPx() else 72.dpToPx()
        }
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = marginTop
            marginStart = horizontalMargin
            marginEnd = horizontalMargin
            gravity = if (prefs.homeAlignment == Gravity.END) Gravity.START else Gravity.END
        }
        binding.tvScreenTime.layoutParams = params
        binding.tvScreenTime.setPadding(10.dpToPx())
    }

    private fun populateHomeScreen(appCountUpdated: Boolean) {
        populateDateTime()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            populateScreenTime()

        val homeApps = prefs.getHomeApps()
        val cleaned = mutableListOf<AppModel.HomeApp>()
        for (homeApp in homeApps) {
            if (homeApp.isFolder || isHomeAppValid(homeApp)) cleaned.add(homeApp)
        }
        if (cleaned.size != homeApps.size) prefs.saveHomeApps(cleaned)

        val container = binding.homeAppsLayout
        while (container.childCount > cleaned.size) {
            container.removeViewAt(container.childCount - 1)
        }
        for (i in cleaned.indices) {
            val textView = if (i < container.childCount) {
                container.getChildAt(i) as TextView
            } else {
                createHomeItemView().also { container.addView(it) }
            }
            textView.tag = i
            if (cleaned[i].isFolder) {
                val name = cleaned[i].folderName.ifBlank { getString(R.string.folder) }
                textView.text = "\u25B8 $name"
            } else {
                textView.text = cleaned[i].appLabel
            }
        }
        binding.tvHomeHint.isVisible = cleaned.isEmpty() && prefs.firstSettingsOpen.not()
    }

    private fun createHomeItemView(): TextView {
        val textView = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_app_text, binding.homeAppsLayout, false) as TextView
        textView.gravity = prefs.homeAlignment
        // These fire only on d-pad/keyboard events; touch is consumed by ViewSwipeTouchListener
        textView.setOnClickListener(this)
        textView.setOnLongClickListener(this)
        textView.setOnDragListener(getHomeSlotDragListener())
        textView.setOnTouchListener(getViewSwipeTouchListener(requireContext(), textView))
        return textView
    }

    private fun isHomeAppValid(homeApp: AppModel.HomeApp): Boolean {
        val userHandle = getUserHandleFromString(requireContext(), homeApp.user)
        if (homeApp.isShortcut) {
            val launcherApps = requireContext().getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
            val query = LauncherApps.ShortcutQuery().apply {
                setPackage(homeApp.appPackage)
                setQueryFlags(LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED)
            }
            return try {
                val shortcuts = launcherApps.getShortcuts(query, userHandle)
                shortcuts?.any { it.id == homeApp.shortcutId } == true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        return isPackageInstalled(requireContext(), homeApp.appPackage, homeApp.user)
    }

    private fun launchAppOrShortcut(
        appName: String,
        packageName: String,
        activityClassName: String?,
        shortcutId: String?,
        isShortcut: Boolean,
        userString: String,
        fallback: (() -> Unit)? = null,
    ) {
        if (appName.isEmpty()) {
            showLongPressToast()
            return
        }
        if (isShortcut && !shortcutId.isNullOrEmpty()) {
            launchShortcut(
                packageName = packageName,
                shortcutId = shortcutId,
                shortcutLabel = appName,
                userString = userString
            )
        } else if (packageName.isNotEmpty()) {
            launchApp(
                appName = appName,
                packageName = packageName,
                activityClassName = activityClassName,
                userString = userString
            )
        } else {
            fallback?.invoke()
        }
    }

    private fun launchShortcut(shortcutId: String, packageName: String, shortcutLabel: String, userString: String) {
        viewModel.selectedApp(
            AppModel.PinnedShortcut(
                shortcutId = shortcutId,
                appLabel = shortcutLabel,
                user = getUserHandleFromString(requireContext(), userString),
                key = null,
                appPackage = packageName,
                isNew = false,
            ),
            Constants.FLAG_LAUNCH_APP
        )
    }

    private fun launchApp(appName: String, packageName: String, activityClassName: String?, userString: String) {
        viewModel.selectedApp(
            AppModel.App(
                appLabel = appName,
                key = null,
                appPackage = packageName,
                activityClassName = activityClassName,
                isNew = false,
                user = getUserHandleFromString(requireContext(), userString)
            ),
            Constants.FLAG_LAUNCH_APP
        )
    }

    private fun homeAppClicked(index: Int) {
        val homeApp = prefs.getHomeApps().getOrNull(index) ?: return
        launchAppOrShortcut(
            appName = homeApp.appLabel,
            packageName = homeApp.appPackage,
            activityClassName = homeApp.activityClassName,
            shortcutId = homeApp.shortcutId,
            isShortcut = homeApp.isShortcut,
            userString = homeApp.user
        )
    }

    private fun openSwipeRightApp() {
        if (!prefs.swipeRightEnabled) return
        launchAppOrShortcut(
            appName = prefs.appNameSwipeRight,
            packageName = prefs.appPackageSwipeRight,
            activityClassName = prefs.appActivityClassNameRight,
            shortcutId = prefs.shortcutIdSwipeRight,
            isShortcut = prefs.isShortcutSwipeRight,
            userString = prefs.appUserSwipeRight,
            fallback = { openDialerApp(requireContext()) }
        )
    }

    private fun openSwipeLeftApp() {
        if (!prefs.swipeLeftEnabled) return
        launchAppOrShortcut(
            appName = prefs.appNameSwipeLeft,
            packageName = prefs.appPackageSwipeLeft,
            activityClassName = prefs.appActivityClassNameSwipeLeft,
            shortcutId = prefs.shortcutIdSwipeLeft,
            isShortcut = prefs.isShortcutSwipeLeft,
            userString = prefs.appUserSwipeLeft,
            fallback = { openCameraApp(requireContext()) }
        )
    }

    private fun showAppList(flag: Int, position: Int = -1, includeHiddenApps: Boolean = false) {
        viewModel.getAppList(includeHiddenApps)
        try {
            findNavController().navigate(
                R.id.action_mainFragment_to_appListFragment,
                bundleOf(Constants.Key.FLAG to flag, Constants.Key.POSITION to position)
            )
        } catch (e: Exception) {
            findNavController().navigate(
                R.id.appListFragment,
                bundleOf(Constants.Key.FLAG to flag, Constants.Key.POSITION to position)
            )
            e.printStackTrace()
        }
    }

    private fun showSlotMenu(index: Int) {
        val homeApp = prefs.getHomeApps().getOrNull(index)
        val isFolderEntry = homeApp?.isFolder == true
        val hasApp = homeApp != null && homeApp.appPackage.isNotEmpty()

        val labels = mutableListOf<String>()
        val actions = mutableListOf<() -> Unit>()

        if (isFolderEntry) {
            labels.add(getString(R.string.rename_folder)); actions.add { showFolderNameDialog(index) }
            labels.add(getString(R.string.remove_folder)); actions.add { removeFolder(index) }
        } else {
            labels.add(getString(R.string.add_app)); actions.add {
                showAppList(
                    Constants.FLAG_SET_HOME_APP,
                    position = prefs.getHomeApps().size,
                    includeHiddenApps = true,
                )
            }
            if (hasApp) {
                labels.add(getString(R.string.replace_app)); actions.add {
                    showAppList(Constants.FLAG_SET_HOME_APP, position = index, includeHiddenApps = true)
                }
                labels.add(getString(R.string.rename_app)); actions.add {
                    showAppNameDialog(index)
                }
                labels.add(getString(R.string.remove_app)); actions.add {
                    prefs.removeHomeApp(index)
                    populateHomeScreen(false)
                }
            }
            labels.add(getString(R.string.create_folder)); actions.add { showFolderNameDialog(index) }
        }

        AlertDialog.Builder(requireContext())
            .setItems(labels.toTypedArray()) { _, which -> actions[which]() }
            .show()
    }

    private fun showFolderNameDialog(index: Int) {
        val isCreating = prefs.getHomeApps().getOrNull(index)?.isFolder != true
        val editText = EditText(requireContext()).apply {
            if (isCreating) setText(getString(R.string.folder)) else setText(prefs.getFolderName(index))
            setSelectAllOnFocus(true)
            hint = getString(R.string.folder_name_hint)
        }
        AlertDialog.Builder(requireContext())
            .setTitle(if (isCreating) R.string.create_folder else R.string.rename_folder)
            .setView(editText)
            .setPositiveButton(R.string.okay) { _, _ ->
                val name = editText.text.toString().trim().ifBlank { getString(R.string.folder) }
                if (isCreating) createFolder(name) else {
                    prefs.renameFolderApp(index, name)
                    populateHomeScreen(false)
                }
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
        editText.showKeyboard()
    }

    private fun createFolder(name: String) {
        prefs.createFolder(name)
        populateHomeScreen(false)
    }

    private fun showAppNameDialog(index: Int) {
        val homeApp = prefs.getHomeApps().getOrNull(index) ?: return
        val editText = EditText(requireContext()).apply {
            setText(homeApp.appLabel)
            setSelectAllOnFocus(true)
            hint = getString(R.string.app_name_hint)
        }
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.rename_app)
            .setView(editText)
            .setPositiveButton(R.string.okay) { _, _ ->
                setHomeAppName(index, editText.text.toString().trim())
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
        editText.showKeyboard()
    }

    private fun setHomeAppName(index: Int, name: String) {
        if (name.isBlank()) return
        val apps = prefs.getHomeApps().toMutableList()
        val homeApp = apps.getOrNull(index) ?: return
        if (homeApp.isFolder) return
        apps[index] = homeApp.copy(appLabel = name)
        prefs.saveHomeApps(apps)
        populateHomeScreen(false)
    }

    private fun removeFolder(index: Int) {
        prefs.removeHomeApp(index)
        populateHomeScreen(false)
    }

    private fun openFolder(index: Int) {
        try {
            findNavController().navigate(
                R.id.action_mainFragment_to_folderFragment,
                bundleOf(Constants.Key.FOLDER_SLOT to index)
            )
        } catch (e: Exception) {
            findNavController().navigate(
                R.id.folderFragment,
                bundleOf(Constants.Key.FOLDER_SLOT to index)
            )
            e.printStackTrace()
        }
    }

    private fun swipeDownAction() {
        when (prefs.swipeDownAction) {
            Constants.SwipeDownAction.SEARCH -> openSearch(requireContext())
            else -> expandNotificationDrawer(requireContext())
        }
    }

    private fun lockPhone() {
        requireActivity().runOnUiThread {
            try {
                deviceManager.lockNow()
            } catch (e: SecurityException) {
                requireContext().showToast(getString(R.string.please_turn_on_double_tap_to_unlock), Toast.LENGTH_LONG)
                findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
            } catch (e: Exception) {
                requireContext().showToast(getString(R.string.launcher_failed_to_lock_device), Toast.LENGTH_LONG)
                prefs.lockModeOn = false
            }
        }
    }

    private fun showStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            requireActivity().window.insetsController?.show(WindowInsets.Type.statusBars())
        else
            @Suppress("DEPRECATION", "InlinedApi")
            requireActivity().window.decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
    }

    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        }
    }

    private fun changeAppTheme() {
        if (prefs.dailyWallpaper.not()) return
        val changedAppTheme = getChangedAppTheme(requireContext(), prefs.appTheme)
        prefs.appTheme = changedAppTheme
        if (prefs.dailyWallpaper) {
            setPlainWallpaperByTheme(requireContext(), changedAppTheme)
            viewModel.setWallpaperWorker()
        }
        requireActivity().recreate()
    }

    private fun openScreenTimeDigitalWellbeing() {
        if (prefs.screenTimeAppPackage.isNotBlank()) {
            launchApp(
                "Screen Time",
                prefs.screenTimeAppPackage,
                prefs.screenTimeAppClassName,
                prefs.screenTimeAppUser
            )
            return
        }
        val intent = Intent()
        try {
            intent.setClassName(
                Constants.DIGITAL_WELLBEING_PACKAGE_NAME,
                Constants.DIGITAL_WELLBEING_ACTIVITY
            )
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                intent.setClassName(
                    Constants.DIGITAL_WELLBEING_SAMSUNG_PACKAGE_NAME,
                    Constants.DIGITAL_WELLBEING_SAMSUNG_ACTIVITY
                )
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showLongPressToast() = requireContext().showToast(getString(R.string.long_press_to_select_app))

    private fun textOnClick(view: View) = onClick(view)

    private fun textOnLongClick(view: View) = onLongClick(view)

    private fun getSwipeGestureListener(context: Context): View.OnTouchListener {
        return object : OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                openSwipeLeftApp()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                openSwipeRightApp()
            }

            override fun onSwipeUp() {
                super.onSwipeUp()
                showAppList(Constants.FLAG_LAUNCH_APP)
            }

            override fun onSwipeDown() {
                super.onSwipeDown()
                swipeDownAction()
            }

            override fun onLongClick() {
                super.onLongClick()
                try {
                    findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                    viewModel.firstOpen(false)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onDoubleClick() {
                super.onDoubleClick()
                if (!prefs.lockModeOn) return
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    binding.lock.performClick()
                else
                    lockPhone()
            }

            override fun onClick() {
                super.onClick()
                viewModel.checkForMessages.call()
            }
        }
    }

    private fun getViewSwipeTouchListener(context: Context, view: View): View.OnTouchListener {
        return object : ViewSwipeTouchListener(context, view) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                openSwipeLeftApp()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                openSwipeRightApp()
            }

            override fun onSwipeUp() {
                super.onSwipeUp()
                showAppList(Constants.FLAG_LAUNCH_APP)
            }

            override fun onSwipeDown() {
                super.onSwipeDown()
                swipeDownAction()
            }

            override fun onLongClick(view: View) {
                super.onLongClick(view)
                textOnLongClick(view)
            }

            override fun onLongPressMove(view: View) {
                super.onLongPressMove(view)
                draggedIndex = view.tag.toString().toInt()
                val data = ClipData.newPlainText("slot", draggedIndex.toString())
                view.startDragAndDrop(data, View.DragShadowBuilder(view), draggedIndex, 0)
                binding.root.layoutTransition = null
            }

            override fun onClick(view: View) {
                super.onClick(view)
                textOnClick(view)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
