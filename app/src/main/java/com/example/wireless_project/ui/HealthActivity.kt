package com.example.wireless_project.ui

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.database.entity.Food
import kotlinx.android.synthetic.main.fragment_healt.*
import java.util.*

class HealthActivity : Fragment(){

    private lateinit var conf : MainActivity.Configuration
    //get current date
    private val calendar: Calendar = Calendar.getInstance()
    private var addDate = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get exercises goal and eating limit
        conf = MainActivity.getJSONData()
        return inflater.inflate(R.layout.fragment_healt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }
    //Set up layout
    private fun setUI(){
        //set weight
        weight.text = String.format("%.1f", weightV)
        //calculate bmi value
        val BMI = weightV/ (heightV* heightV)
        //set bmi to textView and set shape body
        bmi.text = String.format("%.1f", BMI)
        when {
            BMI < 18.5 -> {
                shape.text = getString(R.string.underweight)
            }
            BMI in 18.5..22.9 -> {
                shape.text = getString(R.string.norm)
            }
            BMI in 23.0..24.9 -> {
                shape.text = getString(R.string.overweight)
            }
            BMI in 25.0..29.9 -> {
                shape.text = getString(R.string.obese)
            }
            BMI > 29.9 -> {
                shape.text = getString(R.string.ex_obese)
            }
        }
        setUpExercises()
        setUpFood()
    }
    //burned calories
    private var burned = 0.0
    private fun setUpExercises(){
        //set up animation of progress ber and set burned calories to textView
        val circularProgress = circularProgressBarBurned
        var p = exercisesList.filter { it.addedDate == addDate }.sumByDouble { it.cals }
        burned = p
        total_burned_calories.text = String.format("%.1f", p)
        p /= conf.getGoalExercises()
        p*= 100
        circularProgress.apply {
            if(p > 100){
                p = 100.0
                progressBarColor = Color.parseColor("#EC4313")
            }
            setProgressWithAnimation(p.toFloat(), 600)
        }
    }
    private fun setUpFood(){
        //set up animation of progress ber and set eaten calories to textView
        val circularProgress = circularProgressBarEaten
        var p = foodList.filter { it.addedDate == addDate }.sumByDouble { it.carb*4+it.protein*4+it.fat*9 }
        total_calories.text = String.format("%.1f", p)
        if(((conf.getMaxEating()+burned)-p) >0.0){
            remaining_cal.text = ((conf.getMaxEating()+burned)-p).toString()
        }
        else{
            remaining_cal.text = "0.0"
        }
        //calculate the excess calories and display it on textView
        var excessCal =  (p-(conf.getMaxEating()+burned))
        if(excessCal < 0 ){
            excessCal =0.0
        }
        excess.text = String.format("%.1f", excessCal)
        val min = (excessCal/0.14)/60
        val sec = ((excessCal/0.14)%60)
        val time = "${min.toInt()}:${sec.toInt()}"
        need_exercises.text = time
        p /= (conf.getMaxEating()+burned)
        p *= 100
        //set up animation for progress bar
        circularProgress.apply {
            if(p > 100){
                p = 100.0
                progressBarColor = Color.parseColor("#EC4313")
            }
            setProgressWithAnimation(p.toFloat(), 600)
        }
    }
    companion object{
        lateinit var exercisesList: List<Exercises>
        lateinit var foodList: List<Food>
        var weightV: Double = 0.0
        var heightV: Double = 0.0
        //use to set weight and height and send back this activity to start this activity
        fun newInstance(foods: List<Food>, exercises: List<Exercises>, w: Double, h: Double): HealthActivity {
            exercisesList = exercises
            foodList = foods
            weightV = w
            //convert to meters
            heightV = h/100
            return HealthActivity()
        }

    }
}