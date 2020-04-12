package com.example.perfectcalc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perfectcalc.R
import com.example.perfectcalc.model.ConverterHistoryItem
import kotlinx.android.synthetic.main.item_converter_history.view.*

class HistoryListAdapter: RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {
    var recyclerView: RecyclerView? = null

    var items = emptyList<ConverterHistoryItem>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    fun snapToStart() {
        recyclerView?.postDelayed({
            (recyclerView?.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
            Unit
        }, 200)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_converter_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items.getOrNull(holder.adapterPosition)?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val fromNumberTV = view.fromNumber
        val fromNumberBaseTV = view.fromNumberBase
        val toNumberTV = view.toNumber
        val toNumberBaseTV = view.toNumberBase

        fun bind(item: ConverterHistoryItem) {
            fromNumberTV.setText(item.fromNumber.toString())
//            fromNumberTV.isSelected = true
            fromNumberBaseTV.setText(item.fromNumber.numberBase.base.toString())
            toNumberTV.setText(item.toNumber.toString())
//            toNumberTV.isSelected = true
            toNumberBaseTV.setText(item.toNumber.numberBase.base.toString())
        }
    }
}