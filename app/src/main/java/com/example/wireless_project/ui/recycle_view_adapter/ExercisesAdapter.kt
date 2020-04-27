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
import com.example.wireless_project.database.entity.Exercises
import kotlinx.android.synthetic.main.exercises_cardview.view.*
//set exercises adapter
class ExercisesAdapter(private var items: ArrayList<Exercises>): RecyclerView.Adapter<ExercisesAdapter.ViewHolder>(){
    lateinit var con : FragmentActivity
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: Exercises, con: FragmentActivity){
            itemView.apply {
                exercises.text = item.name
                location.text = item.location
                var cal = item.cals
                var size =0
                if(item.pic!= null){
                    val img = Base64.decode(item.pic, Base64.DEFAULT)
                    size = img.size
                    val image = BitmapFactory.decodeByteArray(img, 0, size)
                    exercisesImage.setImageBitmap(image)
                }
                cals.text = String.format("%.1f", cal)
            }
            //set onClickListener for clicking on item
            itemView.setOnClickListener {
                val fragmentTransaction = con.supportFragmentManager.beginTransaction()
                val dialog: DialogFragment = ExercisesDialog.newInstance(item)
                dialog.show(fragmentTransaction, dialog.javaClass::getSimpleName.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        con = parent.context as FragmentActivity
        return ViewHolder((LayoutInflater.from(parent.context).inflate(R.layout.exercises_cardview, parent, false)))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], con)
    }

}