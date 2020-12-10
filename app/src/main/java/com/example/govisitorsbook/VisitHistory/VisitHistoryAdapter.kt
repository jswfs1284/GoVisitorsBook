package com.example.govisitorsbook.VisitHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.govisitorsbook.R
import kotlinx.android.synthetic.main.list_item_visit_history.view.*

class VisitHistoryAdapter(var entities: List<VisitHistoryEntity> = emptyList()) :
    RecyclerView.Adapter<VisitHistoryAdapter.ItemViewHolder>() {

    override fun getItemCount() = entities.size

    /*뷰홀더 생성하여 반환*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_visit_history, parent, false)
        return ItemViewHolder(itemView)
    }

    // 뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: VisitHistoryAdapter.ItemViewHolder, position: Int) {
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        holder.bindItems(entities[position])
    }

    // 아이템 뷰에 데이터 바인딩
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(entity: VisitHistoryEntity) {
            itemView.item_txt_address.text = entity.address
            itemView.item_txt_visit_date.text = entity.visitDate
        }
    }
}
