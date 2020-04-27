package com.example.wireless_project.ui.recycle_view_adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Food
import kotlinx.android.synthetic.main.food_cardview.view.*
//set FoodAdapter
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
                val totalCal = (item.carb*4 )+( item.fat*9) +(item.protein*4)
                var size =0
                if(item.pic!= null){
                    val img = Base64.decode(item.pic, Base64.DEFAULT)
                    size = img.size
                    val image = BitmapFactory.decodeByteArray(img, 0, size)
                    foodImage.setImageBitmap(image)
                }
                cals.text = String.format("%.1f", totalCal)
            }
            //set onClickListener for clicking on item
            itemView.setOnClickListener {
                val fragmentTransaction = con.supportFragmentManager.beginTransaction()
                val dialog: DialogFragment = FoodDialog.newInstance(item)
                dialog.show(fragmentTransaction, dialog.javaClass::getSimpleName.toString())
            }
        }
    }
}
