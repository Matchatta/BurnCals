package com.example.wireless_project.ui

import android.app.DatePickerDialog
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.database.entity.Food
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.exercises_record_dialog.*
import kotlinx.android.synthetic.main.food_record_dialog.*
import kotlinx.android.synthetic.main.fragment_record.*
import java.util.*
import kotlin.collections.ArrayList

class RecordActivity : Fragment() {
    private val calendar: Calendar = Calendar.getInstance()
    private val mYear = calendar.get(Calendar.YEAR)
    private val mMonth = calendar.get(Calendar.MONTH)
    private val mDay = calendar.get(Calendar.DATE)
    //private lateinit var foodList: List<Food>
    private var addDate = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
    var goalExercises: Double = 0.0
    var maxEating: Double = 0.0
    var scope ="d"
    private val disposable = CompositeDisposable()

    private var fragmentFlag = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val conf = MainActivity.getJSONData()
        goalExercises = conf.getGoalExercises()
        maxEating = conf.getMaxEating()
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //test.text = Calendar.WEEK_OF_MONTH.toString()
        loadExercisesFragment(ArrayList(exercisesList.filter{ it.addedDate == addDate}), goalExercises)
        setOnClick()
    }
    private fun setAnimation(percent: Double){
        val progressBar = circularProgressBar
        var p = percent
        progressBar.apply {
            if(p > 100){
                p = 100.0
                progressBarColor = Color.parseColor("#EC4313")
            }
            setProgressWithAnimation(p.toFloat(), 600)
        }
    }
    private fun setOnClick(){
        val colorPrimary =  resources.getColor(R.color.colorPrimary)
        val colorWhite =  resources.getColor(R.color.colorWhite)
        exercises_btn.setOnClickListener {
            pic.setImageBitmap(resources.getDrawable(R.drawable.burn).toBitmap())
            exercises_btn.setTextColor(colorPrimary)
            eating_btn.setTextColor(colorWhite)
            word.text = getString(R.string.total)
            fragmentFlag =true
            when(scope){
                "d"->{
                    day()
                }
                "m"->{
                    month()
                }
                "w"->{

                }
            }
        }
        eating_btn.setOnClickListener {
            pic.setImageBitmap(resources.getDrawable(R.drawable.burger).toBitmap())
            word.text = getString(R.string.remaining)
            eating_btn.setTextColor(colorPrimary)
            exercises_btn.setTextColor(colorWhite)
//            var burned = 0.0
//            for(ex in ArrayList(exercises.filter { it.addedDate == addDate })){
//                burned += ex.cals
//            }
//            loadFoodFragment(ArrayList(foodList.filter { it->it.addedDate==addDate }), maxEating, burned)
            fragmentFlag =false
            when(scope){
                "d"->{
                    day()
                }
                "m"->{
                    month()
                }
                "w"->{
                    week()
                }
            }
        }
        day.setOnClickListener {
            day.setTextColor(colorPrimary)
            month.setTextColor(colorWhite)
            week.setTextColor(colorWhite)
            scope ="d"
            day()
        }
        month.setOnClickListener {
            day.setTextColor(colorWhite)
            month.setTextColor(colorPrimary)
            week.setTextColor(colorWhite)
            scope ="m"
            month()
        }
        week.setOnClickListener {
            day.setTextColor(colorWhite)
            month.setTextColor(colorWhite)
            week.setTextColor(colorPrimary)
            scope = "w"
            week()
        }
        date_picker.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1, R.style.DialogTheme,
                    DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, dayOfMonth ->
                        calendar.set(mYear, mMonth, dayOfMonth)
                        addDate = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
                        //val filter = foodList.filter { it.addedDate == addDate }
                        date.text = SimpleDateFormat("MMMM dd, yyyy").format(calendar.time)
                        when (scope) {
                            "d" -> {
                                day()
                            }
                            "m" -> {
                                month()
                            }
                            "w" -> {
                                week()
                            }
                        }
                    }, this.mYear, this.mMonth, this.mDay
                )
            }?.show()

        }
    }
    private fun day(){
        if(fragmentFlag){
            loadExercisesFragment(ArrayList(exercisesList.filter { it.addedDate == addDate }), goalExercises)
        }
        else{
            var burned = 0.0
            for(ex in ArrayList(exercisesList.filter { it.addedDate == addDate })){
                burned += ex.cals
            }
            loadFoodFragment(ArrayList(foodList.filter { it.addedDate == addDate }), maxEating, burned)
        }
    }
    private fun month(){
        var maxDay = 30;
        val month = addDate.split("/")[1].toInt()
        val day = addDate.split("/")[0].toInt()
        val year = addDate.split("/")[2].toInt()
        val c = Calendar.getInstance().apply {
            set(year, month, day)
        }
        maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        if(fragmentFlag){
            loadExercisesFragment(ArrayList(exercisesList.filter {
                it.addedDate.split("/")[1] == addDate.split("/")[1]
            }), goalExercises*maxDay)
        }
        else{
            var burned = 0.0
            for(ex in ArrayList(exercisesList.filter { it.addedDate.split("/")[1] == addDate.split("/")[1]  })){
                burned += ex.cals
            }
            loadFoodFragment(ArrayList(foodList.filter {
                it.addedDate.split("/")[1] == addDate.split("/")[1]
            }), maxEating*maxDay, burned)
        }
    }
    private fun week(){
        val c : Calendar = Calendar.getInstance()
        val d : Calendar = Calendar.getInstance()
        val date = addDate.split("/")
        c.set(date[2].toInt(), date[1].toInt(), date[0].toInt())
        var week = c.get(Calendar.WEEK_OF_YEAR)
        if(fragmentFlag){
            loadExercisesFragment(ArrayList(exercisesList.filter {
                d.apply {
                    set(it.addedDate.split("/")[2].toInt(),it.addedDate.split("/")[1].toInt(), it.addedDate.split("/")[0].toInt()) }
                d.get(Calendar.WEEK_OF_YEAR) == week
            }), goalExercises*7)
        }
        else{
            var burned = 0.0
            for(ex in ArrayList(exercisesList.filter { d.apply {
                set(it.addedDate.split("/")[2].toInt(),it.addedDate.split("/")[1].toInt(), it.addedDate.split("/")[0].toInt()) }
                d.get(Calendar.WEEK_OF_YEAR) == week })){
                burned += ex.cals
            }
            loadFoodFragment(ArrayList(foodList.filter { d.apply {
                set(it.addedDate.split("/")[2].toInt(),it.addedDate.split("/")[1].toInt(), it.addedDate.split("/")[0].toInt()) }
                d.get(Calendar.WEEK_OF_YEAR) == week }), maxEating*7, burned)
        }
    }
    private fun loadFoodFragment(foodList: ArrayList<Food>, maxEating: Double, burned: Double){
        var carbValue =0.0
        var proteinValue =0.0
        var fatValue =0.0
        for(food in foodList){
            carbValue += food.carb
            proteinValue += food.protein
            fatValue += food.fat
        }
        carbValue *=4
        proteinValue *=4
        fatValue *=9
        val total = carbValue+fatValue+proteinValue
        val remaining = maxEating - total + burned
        total_calories.text = remaining.toString()
        setAnimation((total/maxEating)*100)
        loadFragment(FoodRecord.newInstance(carbValue, carbValue, fatValue))
    }
    private fun loadExercisesFragment(exercisesList: ArrayList<Exercises>, goalExercises: Double){
        var runValue = 0.0
        var walkValue = 0.0
        var cyclingValue = 0.0
        var otherValue = 0.0
        val run = resources.getString(R.string.run)
        val walk = resources.getString(R.string.walk)
        val cycle = resources.getString(R.string.cycle)
        for(exercises in exercisesList){
            if(exercises.type == run){
                runValue += exercises.cals
            }
            else if(exercises.type == walk){
                walkValue += exercises.cals
            }
            else if(exercises.type == cycle){
                cyclingValue += exercises.cals
            }
            else{
                otherValue += exercises.cals
            }
        }
        val total = runValue+walkValue+cyclingValue+otherValue
        if(total_calories != null&& circularProgressBar!= null){
            total_calories.text = String.format("%.1f", total)
            setAnimation((total/goalExercises)*100)
        }

        loadFragment(ExercisesRecord.newInstance(cyclingValue, runValue, walkValue, otherValue))
    }
    private fun loadFragment(fragment: Fragment){
        val childFragment = childFragmentManager
        val transaction = childFragment.beginTransaction()
        transaction.replace(R.id.container_record, fragment)
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }
    class ExercisesRecord : Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? = inflater.inflate(R.layout.exercises_record_dialog, container, false)

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            walk.text = arguments?.getString(ARG_WALK)
            run.text = arguments?.getString(ARG_RUN)
            cycle.text = arguments?.getString(ARG_CYCLE)
            other.text = arguments?.getString(ARG_OTHER)
        }
        companion object{
            private const val ARG_WALK = "walk"
            private const val ARG_RUN = "run"
            private const val ARG_CYCLE = "cycle"
            private const val ARG_OTHER = "other"
            fun newInstance(cycle: Double, run : Double, walk: Double, other: Double): ExercisesRecord{
                val fragment = ExercisesRecord()
                val args = Bundle().apply {
                    putString(ARG_WALK, String.format("%.1f", walk))
                    putString(ARG_RUN, String.format("%.1f", run))
                    putString(ARG_CYCLE, String.format("%.1f", cycle))
                    putString(ARG_OTHER, String.format("%.1f", other))
                }
                fragment.arguments = args
                return fragment
            }

        }
    }
    class FoodRecord : Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? = inflater.inflate(R.layout.food_record_dialog, container, false)

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val cv = carb
            val pv = protein
            val fv = fat
            cv.text = arguments?.getString(ARG_CARB)
            pv.text = arguments?.getString(ARG_PROTEIN)
            fv.text = arguments?.getString(ARG_FAT)
        }
        companion object{
            private const val ARG_CARB = "carb"
            private const val ARG_PROTEIN = "protein"
            private const val ARG_FAT = "fat"
            fun newInstance(carb: Double, protein: Double, fat: Double): FoodRecord {
                val fragment = FoodRecord()
                val args = Bundle().apply {
                    putString(ARG_CARB, String.format("%.1f", carb))
                    putString(ARG_PROTEIN, String.format("%.1f", protein))
                    putString(ARG_FAT, String.format("%.1f", fat))
                }
                fragment.arguments = args
                return fragment
            }
        }
    }
    companion object{
        lateinit var exercisesList: List<Exercises>
        lateinit var foodList: List<Food>
        fun newInstance(foods: List<Food>, exercises: List<Exercises>): RecordActivity{
            foodList = foods
            exercisesList = exercises
            return RecordActivity()
        }
    }
}