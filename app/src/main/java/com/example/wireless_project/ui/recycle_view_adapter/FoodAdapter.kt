package com.example.wireless_project.ui.recycle_view_adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Food
import kotlinx.android.synthetic.main.food_cardview.view.*

class FoodAdapter(private var items: ArrayList<Food>): RecyclerView.Adapter<FoodAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder((LayoutInflater.from(parent.context).inflate(R.layout.food_cardview, parent, false)))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    override fun getItemCount() = items.size

    class ViewHolder(itemsView: View): RecyclerView.ViewHolder(itemsView){
        fun bind(item: Food){
            itemView.apply {
                food.text = item.name
                location.text = item.location
                var total_cal = (item.carb*4 )+( item.fat*7) +(item.protein*4)
                var size =0
                if(item.pic!= null){
                    size = item.pic.size
                    val image = BitmapFactory.decodeByteArray(item.pic, 0, size)
                    foodImage.setImageBitmap(image)
                }

                cals.text = total_cal.toString()
            }
        }
    }
}
