package com.example.wireless_project.ui.recycle_view_adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Food
import kotlinx.android.synthetic.main.food_cardview.view.*
import androidx.fragment.app.FragmentActivity

class FoodAdapter(private var items: ArrayList<Food>): RecyclerView.Adapter<FoodAdapter.ViewHolder>() {
    lateinit var con : FragmentActivity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        con = parent.context as FragmentActivity
        return ViewHolder((LayoutInflater.from(parent.context).inflate(R.layout.food_cardview, parent, false)))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], con)
    }
    override fun getItemCount() = items.size
    class ViewHolder(itemsView: View): RecyclerView.ViewHolder(itemsView){
        fun bind(item: Food, con: FragmentActivity){
            itemView.apply {
                food.text = item.name
                location.text = item.location
                var totalCal = (item.carb*4 )+( item.fat*9) +(item.protein*4)
                var size =0
                if(item.pic!= null){
                    size = item.pic!!.size
                    val image = BitmapFactory.decodeByteArray(item.pic, 0, size)
                    foodImage.setImageBitmap(image)
                }
                cals.text = totalCal.toString()
            }
            itemView.setOnClickListener {
                val fragmentTransaction = con.supportFragmentManager.beginTransaction()
                val dialog: DialogFragment = FoodDialog.newInstance(item)
                dialog.show(fragmentTransaction, dialog.javaClass::getSimpleName.toString())
            }
        }
    }
}
