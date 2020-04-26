package com.example.wireless_project.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.wireless_project.Injection
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.database.entity.Food
import com.example.wireless_project.database.entity.User
import com.example.wireless_project.ui.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import java.io.*

class MainActivity : AppCompatActivity(){

    companion object{
        lateinit var foodViewModel : FoodViewModel
        lateinit var exercisesViewModel : ExercisesViewModel
        lateinit var userViewModel : UserViewModel
        lateinit var context: Context
        private val gson = Gson()
        private lateinit var conf : Configuration
        var foodList: MutableList<Food> = mutableListOf()
        var exercisesList : MutableList<Exercises> = mutableListOf()
        var userInformation : User? =null
        fun getLaunchIntent(from: Context, userInfo: User?, foods: MutableList<Food>, exercises: MutableList<Exercises>) : Intent {
            foodList = foods
            exercisesList = exercises
            userInformation = userInfo
            return Intent(from, MainActivity::class.java)
        }
        fun getFoodDataSource(): FoodViewModel{
            return foodViewModel
        }
        fun getExercisesSource(): ExercisesViewModel{
            return exercisesViewModel
        }
        fun getUserSource(): UserViewModel{
            return userViewModel
        }
        fun getJSONData(): Configuration{
            conf = Configuration()
           if(conf.getGoalExercises() ==0.0 && conf.getMaxEating()==0.0){
               try {
                   val file = File(context.filesDir, "configuration.json")
                   val data: String
                   data = if (file.exists()) {
                       file.bufferedReader().use {
                           it.readText()
                       }
                   } else {
                       context.assets?.open("configuration.json")?.bufferedReader().use {
                           it!!.readText()
                       }
                   }
                   conf = gson.fromJson(data, Configuration::class.java)

               } catch (exception: IOException) {
                   exception.printStackTrace()
               }
           }
            return conf
        }
        fun setJSONData(goal: Double, max: Double){
            conf?.setConfiguration(goal, max)
            try{
                val file = File(context.filesDir, "configuration.json")
                val data = gson.toJson(conf)
                val outputStream = FileOutputStream(file)
                val bufferWriter = BufferedWriter(OutputStreamWriter(outputStream))
                bufferWriter.write(data)
                bufferWriter.close()
            }catch (exception: IOException){
                exception.printStackTrace()
            }
        }
    }
    private lateinit var viewFoodModelFactory: FoodModelFactory
    private lateinit var viewExercisesModelFactory: ExercisesModelFactory
    private lateinit var viewUserModelFactory: ViewModelFactory
    private val viewFoodModel: FoodViewModel by viewModels{viewFoodModelFactory}
    private val viewExercisesModel: ExercisesViewModel by viewModels{viewExercisesModelFactory}
    private val viewUserModel: UserViewModel by viewModels{viewUserModelFactory}
    private val disposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewFoodModelFactory = Injection.provideFoodViewModelFactory(this)
        viewExercisesModelFactory = Injection.provideExercisesViewModelFactory(this)
        viewUserModelFactory = Injection.provideViewModelFactory(this)
        foodViewModel = viewFoodModel
        exercisesViewModel = viewExercisesModel
        userViewModel = viewUserModel
        context = this
        setContentView(R.layout.activity_main)
        val healthActivity = HealthActivity.newInstance(foodList, exercisesList,
            userInformation!!.weight!!, userInformation!!.height!!
        )
        openFragment(healthActivity)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_health -> {
                val healthActivity = HealthActivity.newInstance(foodList, exercisesList,
                    userInformation!!.weight!!, userInformation!!.height!!
                )
                openFragment(healthActivity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_exercises -> {
                val exercisesActivity = ExercisesActivity.newInstance(exercisesList)
                openFragment(exercisesActivity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_food -> {
                val foodActivity = FoodActivity.newInstance(foodList)
                openFragment(foodActivity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_record -> {
                val recordActivity =
                    RecordActivity.newInstance(foodList.toList(), exercisesList.toList())
                openFragment(recordActivity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_user -> {
                val userActivity =
                    UserActivity.newInstance(userInformation)
                openFragment(userActivity)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment){

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commitAllowingStateLoss()
    }

    class Configuration {
        private var maxEating: Double = 0.0
        private var goalExercises: Double = 0.0
        fun getMaxEating() = maxEating
        fun getGoalExercises() = goalExercises
        fun setConfiguration(goal: Double, max: Double){
            goalExercises = goal
            maxEating = max
        }
    }


}
