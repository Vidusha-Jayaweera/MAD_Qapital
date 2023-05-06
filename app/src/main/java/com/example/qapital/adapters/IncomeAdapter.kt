package com.example.qapital.adapters
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import com.example.qapital.R
import com.example.qapital.models.IncomeModel

class IncomeAdapter(private val incomeList:ArrayList<IncomeModel>) :
    RecyclerView.Adapter<IncomeAdapter.ViewHolder>(){

    private lateinit var iListener:onIncomeItemClickListener

    interface onIncomeItemClickListener{
        fun onIncomeItemClick(position: Int)
    }

    fun setOnIncomeItemClickListener(clickListener: onIncomeItemClickListener){
        iListener=clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.income_list_item,parent,false)
        return ViewHolder(itemView,iListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIncome = incomeList[position]
        holder.tvIncomeTitle.text = currentIncome.incomeTitle
    }

    override fun getItemCount(): Int {
        return incomeList.size
    }
    class ViewHolder(itemView:View,clickListener: onIncomeItemClickListener):RecyclerView.ViewHolder(itemView){
        val tvIncomeTitle:TextView = itemView.findViewById(R.id.tvIncomeTitle)
        init {
            itemView.setOnClickListener {
                clickListener.onIncomeItemClick(adapterPosition)
            }
        }
    }
}