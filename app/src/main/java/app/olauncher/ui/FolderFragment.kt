package app.olauncher.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class FolderFragment : BaseFragment(), View.OnClickListener, View.OnLongClickListener {

    private lateinit var prefs: Prefs
    private val viewModel: MainViewModel by activityViewModels()
    private var folderSlot = 1

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

        binding.tvFolderName.text =
            "\u2039 " + prefs.getFolderName(folderSlot).ifBlank { getString(R.string.folder) }
        binding.tvFolderName.setOnClickListener(this)
        binding.tvFolderName.setOnLongClickListener(this)

        initClickListeners()
        populateFolder()
    }

    override fun onResume() {
        super.onResume()
        populateFolder()
    }

    private fun initClickListeners() {
        binding.folderApp1.setOnClickListener(this)
        binding.folderApp2.setOnClickListener(this)
        binding.folderApp3.setOnClickListener(this)
        binding.folderApp4.setOnClickListener(this)
        binding.folderApp5.setOnClickListener(this)
        binding.folderApp6.setOnClickListener(this)
        binding.folderApp7.setOnClickListener(this)
        binding.folderApp8.setOnClickListener(this)
        binding.folderApp1.setOnLongClickListener(this)
        binding.folderApp2.setOnLongClickListener(this)
        binding.folderApp3.setOnLongClickListener(this)
        binding.folderApp4.setOnLongClickListener(this)
        binding.folderApp5.setOnLongClickListener(this)
        binding.folderApp6.setOnLongClickListener(this)
        binding.folderApp7.setOnLongClickListener(this)
        binding.folderApp8.setOnLongClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvFolderName -> findNavController().popBackStack()

            else -> {
                try {
                    val position = view.tag.toString().toInt() - 1
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
        val position = view.tag.toString().toInt() - 1
        val folderApp = prefs.getFolderApps(folderSlot).getOrNull(position)
        if (folderApp == null) {
            showAppListForFolder(position)
        } else {
            AlertDialog.Builder(requireContext())
                .setItems(arrayOf(getString(R.string.replace_app), getString(R.string.remove_app))) { _, which ->
                    when (which) {
                        0 -> showAppListForFolder(position)
                        1 -> {
                            prefs.removeFolderApp(folderSlot, position)
                            populateFolder()
                        }
                    }
                }
                .show()
        }
        return true
    }

    private fun populateFolder() {
        val apps = prefs.getFolderApps(folderSlot)
        val name = prefs.getFolderName(folderSlot).ifBlank { getString(R.string.folder) }
        binding.tvFolderName.text = "\u2039 $name"
        binding.tvFolderHint.isVisible = apps.none { it != null }

        val slotViews = listOf(
            binding.folderApp1,
            binding.folderApp2,
            binding.folderApp3,
            binding.folderApp4,
            binding.folderApp5,
            binding.folderApp6,
            binding.folderApp7,
            binding.folderApp8,
        )
        for (i in slotViews.indices) {
            val tv = slotViews[i]
            val app = apps.getOrNull(i)
            if (app == null || app.appPackage.isEmpty()
                || isPackageInstalled(requireContext(), app.appPackage, app.user).not()
            ) {
                if (app != null && app.appPackage.isNotEmpty()) {
                    prefs.removeFolderApp(folderSlot, i)
                }
                tv.text = ""
                tv.visibility = View.VISIBLE
                continue
            }
            tv.text = app.appLabel
            tv.visibility = View.VISIBLE
        }
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
                    Constants.Key.FLAG to (Constants.FLAG_SET_FOLDER_APP_1 + position),
                    Constants.Key.RENAME to false,
                    Constants.Key.FOLDER_SLOT to folderSlot
                )
            )
        } catch (e: Exception) {
            findNavController().navigate(
                R.id.appListFragment,
                bundleOf(
                    Constants.Key.FLAG to (Constants.FLAG_SET_FOLDER_APP_1 + position),
                    Constants.Key.RENAME to false,
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
