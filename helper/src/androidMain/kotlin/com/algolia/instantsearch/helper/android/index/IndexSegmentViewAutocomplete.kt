package com.algolia.instantsearch.helper.android.index

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.algolia.instantsearch.core.event.Callback
import com.algolia.instantsearch.helper.index.IndexSegmentView


public class IndexSegmentViewAutocomplete(
    val view: AutoCompleteTextView,
    val adapter: ArrayAdapter<String>
) : IndexSegmentView,
    AdapterView.OnItemClickListener {

    override var onSelectionChange: Callback<Int>? = null

    private var map: Map<Int, String>? = null

    init {
        view.setAdapter(adapter)
        view.onItemClickListener = this
    }

    override fun setSelected(selected: Int?) {
        map?.get(selected)?.let { view.setText(it, false) }
    }

    override fun setMap(map: Map<Int, String>) {
        this.map = map
        adapter.setNotifyOnChange(false)
        adapter.clear()
        adapter.addAll(map.values)
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onSelectionChange?.invoke(position)
    }
}