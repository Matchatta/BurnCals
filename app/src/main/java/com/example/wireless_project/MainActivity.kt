package com.example.wireless_project

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object{
        fun getLaunchIntent(from: Context) : Intent {
            return Intent(from, MainActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                val recordActivity = RecordActivity.newInstance()
                openFragment(recordActivity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_user -> {
                val userActivity = UserActivity.newInstance()
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
