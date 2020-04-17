package com.example.wireless_project.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.wireless_project.Injection
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.User
import com.example.wireless_project.ui.model.ExercisesModelFactory
import com.example.wireless_project.ui.model.ExercisesViewModel
import com.example.wireless_project.ui.model.FoodModelFactory
import com.example.wireless_project.ui.model.FoodViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import java.io.IOException

class MainActivity : AppCompatActivity(){

    companion object{
        lateinit var foodViewModel : FoodViewModel
        lateinit var exercisesViewModel : ExercisesViewModel
        lateinit var context: Context
        var userInformation : User? =null
        fun getLaunchIntent(from: Context, userInfo: User?) : Intent {
            userInformation = userInfo
            return Intent(from, MainActivity::class.java)
        }
        fun getFoodDataSource(): FoodViewModel{
            return foodViewModel
        }
        fun getExercisesSource(): ExercisesViewModel{
            return exercisesViewModel
        }
        fun getJSONData(): Configuration{
            var conf = Configuration()
            try{
                val json= context.assets?.open("configuration.json")?.bufferedReader().use {
                    it?.readText()
                }
                val gson = Gson()
                conf = gson.fromJson(json, Configuration::class.java)


            }catch (exception: IOException){
                exception.printStackTrace()
            }
            return conf
        }
    }
    private lateinit var viewFoodModelFactory: FoodModelFactory
    private lateinit var viewExercisesModelFactory: ExercisesModelFactory
    private val viewFoodModel: FoodViewModel by viewModels{viewFoodModelFactory}
    private val viewExercisesModel: ExercisesViewModel by viewModels{viewExercisesModelFactory}



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewFoodModelFactory = Injection.provideFoodViewModelFactory(this)
        viewExercisesModelFactory = Injection.provideExercisesViewModelFactory(this)
        foodViewModel = viewFoodModel
        exercisesViewModel = viewExercisesModel
        context = this
        setContentView(R.layout.activity_main)
        val healthActivity = HealthActivity.newInstance()
        openFragment(healthActivity)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_health -> {
                val healthActivity = HealthActivity.newInstance()
                openFragment(healthActivity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_exercises -> {
                val exercisesActivity = ExercisesActivity.newInstance()
                openFragment(exercisesActivity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_food -> {
                val foodActivity = FoodActivity.newInstance()
                openFragment(foodActivity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_record -> {
                val recordActivity =
                    RecordActivity.newInstance(userInformation!!.email)
                openFragment(recordActivity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_user -> {
                val weight : String? = userInformation?.weight.toString()
                val height : String? = userInformation?.height.toString()
                val image : String? = userInformation?.image.toString()
                val name = userInformation?.first_name+" "+ userInformation?.last_name
                val gender: String? = userInformation?.gender
                val dob :String? = userInformation?.dob
                val userActivity =
                    UserActivity.newInstance(name, weight, height, image, gender, dob)
                openFragment(userActivity)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment){

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    class Configuration(){
        private var maxEating: Double = 0.0
        private var goalExercises: Double = 0.0
        fun getMaxEating() = maxEating
        fun getGoalExercises() = goalExercises
        fun setGoalExercises(goal: Double){
            goalExercises = goal
        }
        fun setMaxEating(max: Double){
            maxEating = max
        }
    }

}
