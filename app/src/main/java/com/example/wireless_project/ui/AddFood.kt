package com.example.wireless_project.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Food
import com.example.wireless_project.ui.model.FoodViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_food.*
import java.io.ByteArrayOutputStream
import java.util.*

class AddFood : Fragment() {

    val viewModel : FoodViewModel = MainActivity.getFoodDataSource()
    private val disposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_food, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClick()
    }
    companion object{
        fun newInstance(): AddFood =
            AddFood()
    }
    private fun openFoodFragment(){
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.popBackStack()
    }
    private fun setOnClick() {
        cancel.setOnClickListener {
            openFoodFragment()
        }
        save.setOnClickListener {
            if (carbohydrate.text.isEmpty()||foodName.text.isEmpty()||protien.text.isEmpty()||fat.text.isEmpty()||location.text.isEmpty()){
                if(carbohydrate.text.isEmpty()){
                    carbohydrate.error = getString(R.string.empty_message)
                    carbohydrate.requestFocus()
                }
                if(foodName.text.isEmpty()){
                    foodName.error = getString(R.string.empty_message)
                    foodName.requestFocus()
                }
                if(protien.text.isEmpty()){
                    protien.error = getString(R.string.empty_message)
                    protien.requestFocus()
                }
                if(fat.text.isEmpty()){
                    fat.error = getString(R.string.empty_message)
                    fat.requestFocus()
                }
                if(location.text.isEmpty()){
                    location.error = getString(R.string.empty_message)
                    location.requestFocus()
                }
            }
           else{
                val email = MainActivity.userInformation?.email.toString()
                val name = foodName.text.toString()
                val card = carbohydrate.text.toString().toDouble()
                val protein = protien.text.toString().toDouble()
                val fat = fat.text.toString().toDouble()
                val location = location.text.toString()
                val calendar = Calendar.getInstance()
                val addDate = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
                val stream = ByteArrayOutputStream()
                val img:Bitmap? = imm.drawable.toBitmap()
                img?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val image = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
                val food = Food(userEmail = email, name = name, carb = card, protein = protein, fat = fat,pic = image, location = location, addedDate = addDate)
                disposable.add(viewModel.insertFood(food)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        FoodActivity.foodList.add(food)
                        openFoodFragment() }
                    .subscribe { Log.d("SUCCESS", "Good job")})
            }

        }
        addPicture.setOnClickListener{
            dispatchTakePictureIntent()
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imm.layoutParams.width = MATCH_PARENT
            imm.setImageBitmap(imageBitmap)
        }
    }



}