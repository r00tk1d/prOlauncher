package app.olauncher.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import app.olauncher.R

data class SectionedMenuItem(
    val label: String,
    val isHeader: Boolean,
    val action: (() -> Unit)? = null,
)

class SectionedMenuAdapter(
    context: Context,
    private val items: List<SectionedMenuItem>,
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): SectionedMenuItem = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getViewTypeCount(): Int = 2

    override fun getItemViewType(position: Int): Int = if (items[position].isHeader) 0 else 1

    override fun isEnabled(position: Int): Boolean = !items[position].isHeader

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = items[position]
        val view = if (item.isHeader) {
            convertView ?: inflater.inflate(R.layout.item_section_header, parent, false)
        } else {
            convertView ?: inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        }
        view.findViewById<TextView>(android.R.id.text1)?.text = item.label
        return view
    }
}
