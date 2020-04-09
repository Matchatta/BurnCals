package com.example.wireless_project.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.ui.model.ExercisesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_exercises.*
import java.io.ByteArrayOutputStream
import java.util.*

class AddExercises : Fragment() {

    private val viewModel : ExercisesViewModel = MainActivity.getExercisesSource()
    private val disposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_exercises, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClick()
    }
    companion object{
        fun newInstance(): AddExercises =
            AddExercises()
    }
    private fun openFoodFragment(){
        val fragmentManager = activity?.supportFragmentManager
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.container, ExercisesActivity.newInstance())
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
    private fun setOnClick() {
        cancel.setOnClickListener {
            openFoodFragment()
        }
        save.setOnClickListener {
            val email = MainActivity.userInformation?.email.toString()
            val name = exercisesName.text.toString()
            val cals = cals.text.toString().toDouble()
            val location = location.text.toString()
            val calendar =Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DATE)
            val addDate = "$day/$month/$year"
            val stream = ByteArrayOutputStream()
            val img:Bitmap? = imm.drawable.toBitmap()
            img?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val image = stream.toByteArray()
            val exercises = Exercises(null, email, name, cals, addDate, image, location)
            disposable.add(viewModel.insertExercises(exercises)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { openFoodFragment() }
                    .subscribe { Log.d("SUCCESS", "Good job")})

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