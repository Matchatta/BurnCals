package com.example.wireless_project.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.wireless_project.Injection
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Food
import com.example.wireless_project.database.entity.User
import com.example.wireless_project.ui.model.FoodModelFactory
import com.example.wireless_project.ui.model.FoodViewModel
import com.example.wireless_project.ui.model.UserViewModel
import com.example.wireless_project.ui.model.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MainActivity : AppCompatActivity(){

    companion object{
        lateinit var viewModel : FoodViewModel
        var userInformation : User? =null
        fun getLaunchIntent(from: Context, userInfo: User?) : Intent {
            userInformation = userInfo
            return Intent(from, MainActivity::class.java)
        }
        fun getFoodDataSource(): FoodViewModel{
            return viewModel
        }
    }
    private lateinit var viewFoodModelFactory: FoodModelFactory
    private val viewFoodModel: FoodViewModel by viewModels{viewFoodModelFactory}



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewFoodModelFactory = Injection.provideFoodViewModelFactory(this)
        viewModel = viewFoodModel
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
                    RecordActivity.newInstance()
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



}
