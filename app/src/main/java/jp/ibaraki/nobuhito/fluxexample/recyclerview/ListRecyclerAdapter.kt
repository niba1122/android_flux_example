package jp.ibaraki.nobuhito.fluxexample.recyclerview

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import jp.ibaraki.nobuhito.fluxexample.R
import kotlin.properties.Delegates

class ListRecyclerAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<ListItemType> by Delegates.observable(emptyList()) { _, old, new ->
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    old[oldItemPosition]::class == new[newItemPosition]::class

            override fun getOldListSize(): Int = old.size

            override fun getNewListSize(): Int = new.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = old[oldItemPosition]
                val newItem = new[newItemPosition]
                return when {
                    oldItem is ListItemType.Element && newItem is ListItemType.Element ->
                        oldItem.element == newItem.element
                    oldItem is ListItemType.Loading && newItem is ListItemType.Loading -> true
                    else -> false
                }
            }

        }).dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_element_cell -> {
                val view = LayoutInflater.from(context).inflate(R.layout.view_element_cell, parent, false) as ElementCell
                ElementCellViewHolder(view)
            }
            R.layout.view_loading_cell -> {
                val view = LayoutInflater.from(context).inflate(R.layout.view_loading_cell, parent, false) as LoadingCell
                LoadingCellViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.view_element_cell, parent, false) as ElementCell
                ElementCellViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item) {
            is ListItemType.Element -> (holder as ElementCellViewHolder).view.build(item.element)
            is ListItemType.Loading -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int =
            when (items[position]) {
                is ListItemType.Element -> R.layout.view_element_cell
                is ListItemType.Loading -> R.layout.view_loading_cell
            }
}

sealed class ListItemType {
    class Element(val element: jp.ibaraki.nobuhito.fluxexample.model.Element) : ListItemType()
    class Loading : ListItemType()
}
