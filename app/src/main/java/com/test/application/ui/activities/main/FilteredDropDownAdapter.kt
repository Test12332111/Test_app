package com.test.application.ui.activities.main


import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.test.application.utils.toPx

class FilteredDropDownAdapter(val items: Array<String>) : BaseAdapter(), Filterable {


    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        return TextView(p2?.context).apply {
            this.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 48.toPx())
            this.setPadding(16.toPx(), 0, 16.toPx(), 0)
            this.gravity = Gravity.CENTER_VERTICAL
            this.textSize = 16f
            this.text = items[p0]
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                return FilterResults()
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {

            }
        }
    }
}
