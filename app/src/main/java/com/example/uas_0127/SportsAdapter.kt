package com.example.uas_0127

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_0127.R
import com.example.uas_0127.EventItem
import com.example.uas_0127.BaseAdapter
import com.example.uas_0127.utils.getLongDate
import kotlinx.android.synthetic.main.item_sports_list.view.*


class SportsAdapter(private val onClickListener: (position: Int) -> Unit) : BaseAdapter<RecyclerView.ViewHolder, EventItem>() {

    companion object {
        const val TRANSACTION_ITEM_TYPE = 1
    }

    init {
        setHasStableIds(true)
    }

    private var data: MutableList<EventItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_sports_list, parent, false)
        return ProductViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductViewHolder -> holder.bindData(data[position],position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TRANSACTION_ITEM_TYPE
    }

    override fun addAllData(data: MutableList<EventItem>) {
        this.data.addAll(data)
        this.notifyDataSetChanged()
    }

    override fun addData(data: EventItem) {
        this.data.add(data)
        this.notifyDataSetChanged()
    }

    override fun getDataAt(position: Int): EventItem {
        return data[position]
    }

    override fun getAllData(): MutableList<EventItem> {
        return data
    }

    override fun setData(data: MutableList<EventItem>) {

        this.data = data
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}

class ProductViewHolder(viewItem: View, onClickListener: (position: Int) -> Unit) : RecyclerView.ViewHolder(viewItem) {
    init {
        viewItem.setOnClickListener {
            onClickListener(adapterPosition)
        }
    }
    fun bindData(data: EventItem, position : Int){
        if((data.intHomeScore!!.equals("null")||data.intAwayScore!!.equals("null"))&&!data.strHomeTeam!!.equals("null")) {
            itemView.date.text = getLongDate(data.dateEvent)
            itemView.ID_HOME_TEAM.text = data.strHomeTeam
            itemView.ID_HOME_SCORE.visibility = View.GONE
            itemView.ID_AWAY_TEAM.text = data.strAwayTeam
            itemView.ID_AWAY_SCORE.visibility = View.GONE
        }else if(!data.strHomeTeam!!.equals("null")) {
            itemView.date.text = getLongDate(data.dateEvent)
            itemView.ID_HOME_TEAM.text = data.strHomeTeam
            itemView.ID_HOME_SCORE.text = data.intHomeScore
            itemView.ID_AWAY_TEAM.text = data.strAwayTeam
            itemView.ID_AWAY_SCORE.text = data.intAwayScore
            itemView.ID_HOME_SCORE.visibility = View.VISIBLE
            itemView.ID_AWAY_SCORE.visibility = View.VISIBLE
        }else{
            itemView.date.text = getLongDate(data.dateEvent)
            itemView.ID_HOME_TEAM.text = ""
            itemView.ID_HOME_SCORE.text = ""
            itemView.ID_AWAY_TEAM.text = ""
            itemView.ID_AWAY_SCORE.text = ""
        }
    }
}