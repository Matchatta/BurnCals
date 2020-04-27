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
        lateinit var disposable: CompositeDisposable
        private val gson = Gson()
        private lateinit var conf : Configuration
        var foodList: MutableList<Food> = mutableListOf()
        var exercisesList : MutableList<Exercises> = mutableListOf()
        var userInformation : User? =null
        //get intent for start this activity and set the data
        fun getLaunchIntent(from: Context, userInfo: User?, foods: MutableList<Food>, exercises: MutableList<Exercises>) : Intent {
            foodList = foods
            exercisesList = exercises
            userInformation = userInfo
            return Intent(from, MainActivity::class.java)
        }
        //return food data source for insert/ update/ delete
        fun getFoodDataSource(): FoodViewModel{
            return foodViewModel
        }
        //return exercises data source for insert/ update/ delete
        fun getExercisesSource(): ExercisesViewModel{
            return exercisesViewModel
        }
        //return user data source for insert/ update/ delete
        fun getUserSource(): UserViewModel{
            return userViewModel
        }
        //get exercises goal and eating limit from json file that is in asset or local storage
        fun getJSONData(): Configuration{
            conf = Configuration()
            //if this is first time using it will get data from asset else read json from local storage ** use gson libralies to serialize data to the class
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
        //save current json data to local storage
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
    //room database
    private lateinit var viewFoodModelFactory: FoodModelFactory
    private lateinit var viewExercisesModelFactory: ExercisesModelFactory
    private lateinit var viewUserModelFactory: ViewModelFactory
    private val viewFoodModel: FoodViewModel by viewModels{viewFoodModelFactory}
    private val viewExercisesModel: ExercisesViewModel by viewModels{viewExercisesModelFactory}
    private val viewUserModel: UserViewModel by viewModels{viewUserModelFactory}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set data source
        viewFoodModelFactory = Injection.provideFoodViewModelFactory(this)
        viewExercisesModelFactory = Injection.provideExercisesViewModelFactory(this)
        viewUserModelFactory = Injection.provideViewModelFactory(this)
        foodViewModel = viewFoodModel
        exercisesViewModel = viewExercisesModel
        userViewModel = viewUserModel
        //set disposable
        disposable = CompositeDisposable()
        context = this
        setContentView(R.layout.activity_main)
        //load health activity as default page
        val healthActivity = HealthActivity.newInstance(foodList, exercisesList,
            userInformation!!.weight!!, userInformation!!.height!!
        )
        openFragment(healthActivity)
        //set bottom navigation bar action
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            //go to health activity(Fragment)
            R.id.navigation_health -> {
                val healthActivity = HealthActivity.newInstance(foodList, exercisesList,
                    userInformation!!.weight!!, userInformation!!.height!!
                )
                openFragment(healthActivity)
                return@OnNavigationItemSelectedListener true
            }
            //go to exercises activity(Fragment)
            R.id.navigation_exercises -> {
                val exercisesActivity = ExercisesActivity.newInstance(exercisesList)
                openFragment(exercisesActivity)
                return@OnNavigationItemSelectedListener true
            }
            //go to food activity(Fragment)
            R.id.navigation_food -> {
                val foodActivity = FoodActivity.newInstance(foodList)
                openFragment(foodActivity)
                return@OnNavigationItemSelectedListener true
            }
            //go to record activity(Fragment)
            R.id.navigation_record -> {
                val recordActivity =
                    RecordActivity.newInstance(foodList.toList(), exercisesList.toList())
                openFragment(recordActivity)
                return@OnNavigationItemSelectedListener true
            }
            //go to user activity(Fragment)
            R.id.navigation_user -> {
                val userActivity =
                    UserActivity.newInstance(userInformation)
                openFragment(userActivity)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    //use to load new fragment
    private fun openFragment(fragment: Fragment){

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commitAllowingStateLoss()
    }
    //configuration class use to keep and edited exercises goal and eating limit
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
