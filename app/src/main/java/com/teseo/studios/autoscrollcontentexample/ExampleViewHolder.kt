package com.teseo.studios.autoscrollcontentexample

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.teseo.studios.autoscrollcontentexample.databinding.ItemAutoscrollContentBinding

class ExampleViewHolder(
    private val binding: ViewBinding
) : ViewHolder(binding.root) {

    fun bind(item: ExampleModel) {
        val binding = (binding as ItemAutoscrollContentBinding)

        binding.tvMessage.text = item.message
    }

    fun onLinkItem(item: ExampleModel, onLinkItem: (String) -> Unit) {
        onLinkItem.invoke(item.message)
    }
}