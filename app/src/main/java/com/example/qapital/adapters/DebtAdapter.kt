package com.example.qapital.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qapital.R
import com.example.qapital.models.DebtModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DebtAdapter (private val debtList: ArrayList<DebtModel>) : RecyclerView.Adapter<DebtAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDebt = debtList[position]

        holder.tvDebtName.text = currentDebt.debtName
        holder.tvAmount.text = currentDebt.debtAmount.toString()
        holder.tvPayStatus.text = currentDebt.payStatus

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val result = Date(currentDebt.debtBorrowedDate!!)
        holder.tvBorrowedDate.text = simpleDateFormat.format(result)
    }

    override fun getItemCount(): Int {
        return debtList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val tvDebtName : TextView = itemView.findViewById(R.id.tvName)
        val tvPayStatus : TextView = itemView.findViewById(R.id.tvPayStatus)
        val tvAmount : TextView = itemView.findViewById(R.id.tvAmount)
        val tvBorrowedDate : TextView = itemView.findViewById(R.id.tvBorrowedDate)

        init{
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}