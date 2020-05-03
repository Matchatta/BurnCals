package com.example.wireless_project.ui.recycle_view_adapter

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Food
import com.example.wireless_project.ui.FoodActivity
import com.example.wireless_project.ui.MainActivity
import com.example.wireless_project.ui.model.FoodViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.food_dialog.*

class FoodDialog: DialogFragment() {
    private val viewModel : FoodViewModel = MainActivity.getFoodDataSource()
    private val disposable = MainActivity.disposable
    companion object{
        lateinit var food: Food
        fun newInstance(food: Food): FoodDialog {
            this.food = food
            return FoodDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        setUI()
        setUp()
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = ViewGroup.LayoutParams.WRAP_CONTENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width.toInt(), height)
    }
    //set up the layout
    private fun setUI() {
        foodName.setText(food.name, TextView.BufferType.EDITABLE)
        location.setText(food.location, TextView.BufferType.EDITABLE)
        carbohydrate.setText(food.carb.toString(), TextView.BufferType.EDITABLE)
        protien.setText(food.protein.toString(), TextView.BufferType.EDITABLE)
        fat.setText(food.fat.toString(), TextView.BufferType.EDITABLE)
        if(food.pic!= null){
            val imgByte = Base64.decode(food.pic, Base64.DEFAULT)
            val size = imgByte!!.size
            val image = BitmapFactory.decodeByteArray(imgByte, 0, size)
            foodImage.setImageBitmap(image)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.food_dialog, container, false)
    //set all button action
    private fun setUp(){
        //save button
        save.setOnClickListener {
            //retrieve information from field
            food.name = foodName.text.toString()
            food.location = location.text.toString()
            food.carb = carbohydrate.text.toString().toDouble()
            food.fat = fat.text.toString().toDouble()
            food.protein = protien.text.toString().toDouble()
            //update data
            disposable.add(viewModel.updateFood(food)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    FoodActivity.updateData()
                    Toast.makeText(context, resources.getString(R.string.update), Toast.LENGTH_SHORT).show()
                    dismiss() }
                .subscribe { Log.d("SUCCESS", "Good job")})

        }
        //dismiss dialog
        cancel.setOnClickListener {
            dismiss()
        }
        //delete data
        delete.setOnClickListener {
            //open alert dialog
            val builder = AlertDialog.Builder(context,  R.style.DialogTheme2)
            builder.setMessage(resources.getString(R.string.message))
            //delete data
            builder.setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                disposable.add(viewModel.deleteFood(food)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        Toast.makeText(context, resources.getString(R.string.delete), Toast.LENGTH_SHORT).show()
                        FoodActivity.deleteData(food)
                        dialog.dismiss()
                        dismiss()})
            }
            builder.setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

}