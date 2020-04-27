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
import android.widget.ArrayAdapter
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.ui.model.ExercisesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_exercises.*
import java.io.ByteArrayOutputStream
import java.util.*

class AddExercises : Fragment() {
    //get data source from main activity and get disposable from Main activity
    private val viewModel : ExercisesViewModel = MainActivity.getExercisesSource()
    private val disposable = MainActivity.disposable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_exercises, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setOnClick()
    }
    companion object{
        fun newInstance(): AddExercises =
            AddExercises()
    }
    private fun setUI(){
        //set up the dropdown menu
        var dropDown = exercisesType
        val run = resources.getString(R.string.run)
        val walk = resources.getString(R.string.walk)
        val cycling = resources.getString(R.string.cycle)
        val other = resources.getString(R.string.other)
        val items = arrayOf(run, walk, cycling, other)
        val adapterArray = context?.let { ArrayAdapter(it, R.layout.spinner_layout, items) }
        dropDown.adapter = adapterArray
    }
    private fun openPreviousFragment(){
        //go to previous activity(Fragment)
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.popBackStack()
    }
    private fun setOnClick() {
        //cancel button
        cancel.setOnClickListener {
            openPreviousFragment()
        }
        //save button
        save.setOnClickListener {
            //Check that all fields have been already filled
            if (exercisesName.text.isEmpty()||cals.text.isEmpty()||location.text.isEmpty()){
                if(exercisesName.text.isEmpty()){
                    exercisesName.error = getString(R.string.empty_message)
                    exercisesName.requestFocus()
                }
                if(location.text.isEmpty()){
                    location.error = getString(R.string.empty_message)
                    location.requestFocus()
                }
                if(cals.text.isEmpty()){
                    cals.error = getString(R.string.empty_message)
                    cals.requestFocus()
                }
            }
            //insert new exercises to the sqlite
            else{
                //getting information from the EditText and drp down menu
                val email = MainActivity.userInformation?.email.toString()
                val name = exercisesName.text.toString()
                val type = exercisesType.selectedItem.toString()
                val cals = cals.text.toString().toDouble()
                val location = location.text.toString()
                val calendar = Calendar.getInstance()
                val addDate = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
               //convert image to Base64 string before put to sqlite database
                val stream = ByteArrayOutputStream()
                val img:Bitmap? = imm.drawable.toBitmap()
                img?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val image = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
                //insert new exercises and turn back to exercises activity
                val exercises = Exercises( userEmail =  email, name =  name, type = type, cals = cals,addedDate =  addDate,pic =  image,location =  location)
                disposable.add(viewModel.insertExercises(exercises)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        ExercisesActivity.exercisesList.add(exercises)
                        openPreviousFragment()}
                    .subscribe { Log.d("SUCCESS", "Good job")})

            }
        }
        //add picture button
        addPicture.setOnClickListener{
            dispatchTakePictureIntent()
        }
    }

    private val REQUEST_IMAGE_CAPTURE = 1
    //camera intent(use to connect with device camera)
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    //set image after took a photo
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imm.layoutParams.width = MATCH_PARENT
            imm.setImageBitmap(imageBitmap)
        }
    }



}