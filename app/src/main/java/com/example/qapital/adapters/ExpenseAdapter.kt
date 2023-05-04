package com.example.qapital.adapters
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import com.example.qapital.R
import com.example.qapital.models.ExpenseModel

class ExpenseAdapter(private val expenseList:ArrayList<ExpenseModel>) :
    RecyclerView.Adapter<ExpenseAdapter.ViewHolder>(){

    private lateinit var eListener:onExpenseItemClickListener

    interface onExpenseItemClickListener{
        fun onExpenseItemClick(position: Int)
    }

    fun setOnExpenseItemClickListener(clickListener: onExpenseItemClickListener){
        eListener=clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.expense_list_item,parent,false)
        return ViewHolder(itemView,eListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentExpense = expenseList[position]
        holder.tvExpenseTitle.text = currentExpense.expenseTitle
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }
    class ViewHolder(itemView:View,clickListener: onExpenseItemClickListener):RecyclerView.ViewHolder(itemView){
        val tvExpenseTitle:TextView = itemView.findViewById(R.id.tvExpenseTitle)
        init {
            itemView.setOnClickListener {
                clickListener.onExpenseItemClick(adapterPosition)
            }
        }
    }
}