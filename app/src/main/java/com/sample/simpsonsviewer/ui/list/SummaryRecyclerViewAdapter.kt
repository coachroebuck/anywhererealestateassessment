package com.sample.simpsonsviewer.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.simpsonsviewer.R
import com.sample.simpsonsviewer.databinding.FragmentItemBinding
import com.sample.simpsonsviewer.model.ServiceResponseSummary

class SummaryRecyclerViewAdapter(
    private val values: List<ServiceResponseSummary>,
    private val callback: (String) -> Unit,
) : RecyclerView.Adapter<SummaryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = values[position], callback = callback)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.title
        val details: TextView = binding.details
        val icon: ImageView = binding.imageView

        override fun toString(): String {
            return super.toString() + "$title $details"
        }

        fun bind(item: ServiceResponseSummary, callback: (String) -> Unit) {
            title.text = item.title
            details.text = item.details
            Glide.with(icon)
                .load(item.icon)
                .error(R.drawable.photo_not_available)
                .into(icon)
            itemView.setOnClickListener { callback(title.text.toString()) }
        }
    }

}