package app.olauncher.ui

import android.animation.LayoutTransition
import android.app.AlertDialog
import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import app.olauncher.MainViewModel
import app.olauncher.R
import app.olauncher.data.AppModel
import app.olauncher.data.Constants
import app.olauncher.data.Prefs
import app.olauncher.databinding.FragmentFolderBinding
import app.olauncher.helper.getUserHandleFromString
import app.olauncher.helper.isPackageInstalled
import app.olauncher.helper.showKeyboard
import app.olauncher.listener.ViewSwipeTouchListener

class FolderFragment : BaseFragment(), View.OnClickListener, View.OnLongClickListener {

    private lateinit var prefs: Prefs
    private val viewModel: MainViewModel by activityViewModels()
    private var folderSlot = 1
    private var draggedPosition = -1

    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = Prefs(requireContext())
        folderSlot = arguments?.getInt(Constants.Key.FOLDER_SLOT, 1) ?: 1

        binding.tvFolderName.setText("\u2039 " + getFolderDisplayName())
        binding.tvFolderName.setOnClickListener(this)
        binding.tvFolderName.setOnLongClickListener(this)
        binding.tvFolderHint.setOnClickListener(this)
        binding.folderMainLayout.setOnClickListener(this)

        populateFolder()
    }

    override fun onResume() {
        super.onResume()
        setFolderAlignment()
        populateFolder()
    }

    private fun getFolderDisplayName(): String =
        prefs.getFolderName(folderSlot).ifBlank { getString(R.string.folder) }

    private fun getFolderAppDragListener(): View.OnDragListener {
        return View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> true

                DragEvent.ACTION_DRAG_ENTERED -> {
                    val target = view.tag.toString().toInt()
                    if (draggedPosition != -1 && draggedPosition != target) {
                        prefs.swapFolderApps(folderSlot, draggedPosition, target)
                        refreshFolderAppText(draggedPosition)
                        refreshFolderAppText(target)
                        draggedPosition = target
                    }
                    true
                }

                DragEvent.ACTION_DROP -> {
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    draggedPosition = -1
                    binding.root.post { binding.root.layoutTransition = LayoutTransition() }
                    true
                }

                else -> false
            }
        }
    }

    private fun refreshFolderAppText(position: Int) {
        val tv = binding.folderAppsLayout.getChildAt(position) as? TextView ?: return
        val app = prefs.getFolderApps(folderSlot).getOrNull(position) ?: return
        tv.text = app.appLabel
    }

    private fun startFolderAppDrag(view: View) {
        draggedPosition = view.tag.toString().toInt()
        val data = ClipData.newPlainText("slot", draggedPosition.toString())
        view.startDragAndDrop(data, View.DragShadowBuilder(view), draggedPosition, 0)
        binding.root.layoutTransition = null
    }

    private fun textOnClick(view: View) = onClick(view)

    private fun textOnLongClick(view: View) = onLongClick(view)

    override fun onClick(view: View) {
        when (view.id) {
            R.id.folderMainLayout, R.id.tvFolderName -> findNavController().popBackStack()

            R.id.tvFolderHint -> showAppListForFolder(prefs.getFolderApps(folderSlot).size)

            else -> {
                try {
                    val position = view.tag.toString().toInt()
                    val folderApp = prefs.getFolderApps(folderSlot).getOrNull(position) ?: return
                    launchFolderApp(folderApp)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onLongClick(view: View): Boolean {
        if (view.id == R.id.tvFolderName) {
            findNavController().popBackStack()
            return true
        }
        val position = view.tag.toString().toInt()
        val labels = mutableListOf<String>()
        val actions = mutableListOf<() -> Unit>()

        labels.add(getString(R.string.add_app))
        actions.add { showAppListForFolder(prefs.getFolderApps(folderSlot).size) }
        labels.add(getString(R.string.replace_app))
        actions.add { showAppListForFolder(position) }
        labels.add(getString(R.string.rename)); actions.add { showFolderAppNameDialog(position) }
        labels.add(getString(R.string.remove_app))
        actions.add {
            prefs.removeFolderApp(folderSlot, position)
            populateFolder()
        }
        AlertDialog.Builder(requireContext())
            .setItems(labels.toTypedArray()) { _, which -> actions[which]() }
            .show()
        return true
    }

    private fun showFolderAppNameDialog(position: Int) {
        val app = prefs.getFolderApps(folderSlot).getOrNull(position) ?: return
        val editText = EditText(requireContext()).apply {
            setText(app.appLabel)
            setSelectAllOnFocus(true)
            hint = getString(R.string.app_name_hint)
        }
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.rename_app)
            .setView(editText)
            .setPositiveButton(R.string.okay) { _, _ ->
                val name = editText.text.toString().trim()
                if (name.isNotBlank()) {
                    prefs.renameFolderApp(folderSlot, position, name)
                    populateFolder()
                }
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
        editText.showKeyboard()
    }

    private fun setFolderAlignment() {
        val horizontalGravity = prefs.homeAlignment
        val verticalGravity = if (prefs.homeBottomAlignment) Gravity.BOTTOM else Gravity.CENTER_VERTICAL
        binding.folderAppsLayout.gravity = horizontalGravity or verticalGravity
        for (i in 0 until binding.folderAppsLayout.childCount) {
            (binding.folderAppsLayout.getChildAt(i) as? TextView)?.gravity = horizontalGravity
        }
    }

    private fun populateFolder() {
        val apps = prefs.getFolderApps(folderSlot)
        binding.tvFolderName.text = "\u2039 ${getFolderDisplayName()}"

        val cleaned = mutableListOf<AppModel.FolderApp>()
        for (folderApp in apps) {
            if (isFolderAppValid(folderApp)) cleaned.add(folderApp)
        }
        if (cleaned.size != apps.size) prefs.saveFolderApps(folderSlot, cleaned)

        val container = binding.folderAppsLayout
        while (container.childCount > cleaned.size) {
            container.removeViewAt(container.childCount - 1)
        }
        for (i in cleaned.indices) {
            val textView = if (i < container.childCount) {
                container.getChildAt(i) as TextView
            } else {
                createFolderItemView().also { container.addView(it) }
            }
            textView.tag = i
            textView.text = cleaned[i].appLabel
        }
        binding.tvFolderHint.isVisible = cleaned.isEmpty()
    }

    private fun createFolderItemView(): TextView {
        val textView = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_app_text, binding.folderAppsLayout, false) as TextView
        textView.gravity = prefs.homeAlignment
        // These fire only on d-pad/keyboard events; touch is consumed by ViewSwipeTouchListener
        textView.setOnClickListener(this)
        textView.setOnLongClickListener(this)
        textView.setOnDragListener(getFolderAppDragListener())
        textView.setOnTouchListener(getFolderAppTouchListener(textView))
        return textView
    }

    private fun getFolderAppTouchListener(view: View): View.OnTouchListener {
        return object : ViewSwipeTouchListener(requireContext(), view) {
            override fun onLongClick(view: View) {
                super.onLongClick(view)
                textOnLongClick(view)
            }

            override fun onLongPressMove(view: View) {
                super.onLongPressMove(view)
                startFolderAppDrag(view)
            }

            override fun onClick(view: View) {
                super.onClick(view)
                textOnClick(view)
            }
        }
    }

    private fun isFolderAppValid(folderApp: AppModel.FolderApp): Boolean {
        if (folderApp.appPackage.isEmpty()) return false
        if (folderApp.isShortcut) return true
        return isPackageInstalled(requireContext(), folderApp.appPackage, folderApp.user)
    }

    private fun launchFolderApp(folderApp: AppModel.FolderApp) {
        if (folderApp.appLabel.isEmpty()) return
        if (folderApp.isShortcut && folderApp.shortcutId.isNotEmpty()) {
            viewModel.selectedApp(
                AppModel.PinnedShortcut(
                    shortcutId = folderApp.shortcutId,
                    appLabel = folderApp.appLabel,
                    user = getUserHandleFromString(requireContext(), folderApp.user),
                    key = null,
                    appPackage = folderApp.appPackage,
                    isNew = false,
                ),
                Constants.FLAG_LAUNCH_APP
            )
        } else if (folderApp.appPackage.isNotEmpty()) {
            viewModel.selectedApp(
                AppModel.App(
                    appLabel = folderApp.appLabel,
                    key = null,
                    appPackage = folderApp.appPackage,
                    activityClassName = folderApp.activityClassName,
                    isNew = false,
                    user = getUserHandleFromString(requireContext(), folderApp.user)
                ),
                Constants.FLAG_LAUNCH_APP
            )
        }
    }

    private fun showAppListForFolder(position: Int) {
        viewModel.getAppList(true)
        try {
            findNavController().navigate(
                R.id.action_folderFragment_to_appListFragment,
                bundleOf(
                    Constants.Key.FLAG to Constants.FLAG_SET_FOLDER_APP,
                    Constants.Key.POSITION to position,
                    Constants.Key.FOLDER_SLOT to folderSlot
                )
            )
        } catch (e: Exception) {
            findNavController().navigate(
                R.id.appListFragment,
                bundleOf(
                    Constants.Key.FLAG to Constants.FLAG_SET_FOLDER_APP,
                    Constants.Key.POSITION to position,
                    Constants.Key.FOLDER_SLOT to folderSlot
                )
            )
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
