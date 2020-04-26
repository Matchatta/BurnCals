package com.example.wireless_project.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.wireless_project.R
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingActivity : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_setting, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }
    private fun setUI() {
        val conf = MainActivity.getJSONData()
        val fragment = activity?.supportFragmentManager
        val exSeekBar = ex_seek_bar
        val foodSeekBar = food_seek_bar
        val exVal = ex
        val foodVal = food
        exSeekBar.progress = conf.getGoalExercises().toInt()
        exVal.text = exSeekBar.progress.toString()
        foodSeekBar.progress = conf.getMaxEating().toInt()
        foodVal.text = foodSeekBar.progress.toString()

        back.setOnClickListener {
            fragment?.popBackStack()
        }
        exSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                exVal.text = seek.progress.toString()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
                Toast.makeText(
                    activity, "Value is: " + seek.progress + "kcals"
                    , Toast.LENGTH_SHORT
                ).show()
            }
        })
        foodSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                foodVal.text = seek.progress.toString()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
                Toast.makeText(
                    activity, "Value is: " + seek.progress + "kcals"
                    , Toast.LENGTH_SHORT
                ).show()
            }
        })
        save.setOnClickListener {
            val exGoal = exVal.text.toString().toDouble()
            val limit = foodVal.text.toString().toDouble()
            MainActivity.setJSONData(exGoal, limit)
            Toast.makeText(
                activity, "Saved"
                , Toast.LENGTH_SHORT
            ).show()
        }
    }
}