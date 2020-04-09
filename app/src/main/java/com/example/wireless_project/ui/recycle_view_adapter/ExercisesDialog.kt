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
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.database.entity.Food
import com.example.wireless_project.ui.MainActivity
import com.example.wireless_project.ui.model.ExercisesViewModel
import com.example.wireless_project.ui.model.FoodViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.exercises_dialog.*

class ExercisesDialog: DialogFragment() {
    val viewModel : ExercisesViewModel = MainActivity.getExercisesSource()
    private val disposable = CompositeDisposable()
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
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = ViewGroup.LayoutParams.WRAP_CONTENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    private fun setUI() {
        exercisesName.setText(exercises.name, TextView.BufferType.EDITABLE)
        location.setText(exercises.location, TextView.BufferType.EDITABLE)
        cals.setText(exercises.cals.toString(), TextView.BufferType.EDITABLE)
        if(exercises.pic!= null){
            val size = exercises.pic!!.size
            val image = BitmapFactory.decodeByteArray(exercises.pic, 0, size)
            exercisesImage.setImageBitmap(image)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.exercises_dialog, container, false)

    private fun setUp(){
        save.setOnClickListener {
            exercises.name = exercisesName.text.toString()
            exercises.location = location.text.toString()
            exercises.cals = cals.text.toString().toDouble()
            disposable.add(viewModel.updateExercises(exercises)
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
                val exView = dialog.findViewById<TextView>(R.id.foodDel)
                exView.text = "\""+ exercises.name+"\""
                textView.text = getString(R.string.delete_check)
                val cancel = dialog.findViewById<Button>(R.id.cancel)
                val confirm = dialog.findViewById<Button>(R.id.delete)
                cancel.setOnClickListener {
                    dialog.dismiss()
                }
                confirm.setOnClickListener {
                    disposable.add(viewModel.deleteExercises(exercises)
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