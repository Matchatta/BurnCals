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
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.ui.ExercisesActivity
import com.example.wireless_project.ui.MainActivity
import com.example.wireless_project.ui.model.ExercisesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.exercises_dialog.*

class ExercisesDialog: DialogFragment() {
    private val viewModel : ExercisesViewModel = MainActivity.getExercisesSource()
    private val disposable = MainActivity.disposable
    companion object{
        lateinit var exercises: Exercises
        fun newInstance(exercises: Exercises): ExercisesDialog {
            this.exercises = exercises
            return ExercisesDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        setUI()
        setUp()
        //set dialog size
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = ViewGroup.LayoutParams.WRAP_CONTENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }
    //set up layout
    private fun setUI() {
        //set dropdown
        var dropDown = exercisesType
        val run = resources.getString(R.string.run)
        val walk = resources.getString(R.string.walk)
        val cycling = resources.getString(R.string.cycle)
        val other = resources.getString(R.string.other)
        val items = arrayOf(run, walk, cycling, other)
        val adapterArray = context?.let { ArrayAdapter(it, R.layout.spinner_layout, items) }
        dropDown.adapter = adapterArray
        dropDown.setSelection(items.indexOf(exercises.type))
        //set default values
        exercisesName.setText(exercises.name, TextView.BufferType.EDITABLE)
        location.setText(exercises.location, TextView.BufferType.EDITABLE)
        cals.setText(exercises.cals.toString(), TextView.BufferType.EDITABLE)
        if(exercises.pic!= null){
            val imgByte = Base64.decode(exercises.pic, Base64.DEFAULT)
            val size = imgByte!!.size
            val image = BitmapFactory.decodeByteArray(imgByte, 0, size)
            exercisesImage.setImageBitmap(image)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.exercises_dialog, container, false)
    //set all button action
    private fun setUp(){
        //save button
        save.setOnClickListener {
            //retrieve data from field
            exercises.name = exercisesName.text.toString()
            exercises.location = location.text.toString()
            exercises.cals = cals.text.toString().toDouble()
            exercises.type = exercisesType.selectedItem.toString()
            //update information
            disposable.add(viewModel.updateExercises(exercises)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { dismiss() }
                .subscribe { Log.d("SUCCESS", "Good job")})

        }
        //dismiss this dialog
        cancel.setOnClickListener {
            dismiss()
        }
        //delete exercises data
        delete.setOnClickListener {
            //open alert dialog
            val builder = AlertDialog.Builder(context, R.style.DialogTheme2)
            builder.setMessage(resources.getString(R.string.message))
            //delete data
            builder.setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                disposable.add(viewModel.deleteExercises(exercises)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        Toast.makeText(context, resources.getString(R.string.delete), Toast.LENGTH_SHORT).show()
                        ExercisesActivity.deleteData(exercises)
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