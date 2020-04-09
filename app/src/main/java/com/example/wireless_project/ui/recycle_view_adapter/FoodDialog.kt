package com.example.wireless_project.ui.recycle_view_adapter

import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Food
import com.example.wireless_project.ui.MainActivity
import com.example.wireless_project.ui.model.FoodViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.confirm_dialog.*
import kotlinx.android.synthetic.main.food_dialog.*
import kotlinx.android.synthetic.main.food_dialog.cancel
import kotlinx.android.synthetic.main.food_dialog.delete

class FoodDialog: DialogFragment() {
    val viewModel : FoodViewModel = MainActivity.getFoodDataSource()
    private val disposable = CompositeDisposable()
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

    private fun setUI() {
        foodName.setText(food.name, TextView.BufferType.EDITABLE)
        location.setText(food.location, TextView.BufferType.EDITABLE)
        carbohydrate.setText(food.carb.toString(), TextView.BufferType.EDITABLE)
        protien.setText(food.protein.toString(), TextView.BufferType.EDITABLE)
        fat.setText(food.fat.toString(), TextView.BufferType.EDITABLE)
        if(food.pic!= null){
            val size = food.pic!!.size
            val image = BitmapFactory.decodeByteArray(food.pic, 0, size)
            foodImage.setImageBitmap(image)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.food_dialog, container, false)

    private fun setUp(){
        save.setOnClickListener {
            food.name = foodName.text.toString()
            food.location = location.text.toString()
            food.carb = carbohydrate.text.toString().toDouble()
            food.fat = fat.text.toString().toDouble()
            food.protein = protien.text.toString().toDouble()
            disposable.add(viewModel.updateFood(food)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { dismiss() }
                .subscribe { Log.d("SUCCESS", "Good job")})

        }
        cancel.setOnClickListener {
            dismiss()
        }
        delete.setOnClickListener {
            val dialog = context?.let { it1 -> Dialog(it1) }
            if (dialog != null) {
                dialog.setContentView(R.layout.confirm_dialog)
                dialog.window?.apply {
                    setLayout(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT)
                    setGravity(Gravity.CENTER)
                }
                val textView = dialog.findViewById<TextView>(R.id.text)
                val foodView = dialog.findViewById<TextView>(R.id.foodDel)
                foodView.text = "\""+ food.name+"\""
                textView.text = getString(R.string.delete_check)
                val cancel = dialog.findViewById<Button>(R.id.cancel)
                val confirm = dialog.findViewById<Button>(R.id.delete)
                cancel.setOnClickListener {
                    dialog.dismiss()
                }
                confirm.setOnClickListener {
                    disposable.add(viewModel.deleteFood(food)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            dialog.dismiss()
                            dismiss() }
                        .subscribe { Log.d("SUCCESS", "Good job")})
                }
                dialog.show()
            }
        }
    }

}