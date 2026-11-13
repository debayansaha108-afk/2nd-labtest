package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private var records: List<BalanceRecord>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    fun updateRecords(newRecords: List<BalanceRecord>) {
        records = newRecords
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val record = records[position]
        holder.tvDate.text = record.date
        holder.tvTime.text = record.time
        holder.tvAmount.text = "₹ ${record.amount}"
    }

    override fun getItemCount(): Int = records.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tv_history_date)
        val tvTime: TextView = itemView.findViewById(R.id.tv_history_time)
        val tvAmount: TextView = itemView.findViewById(R.id.tv_history_amount)
    }
}
