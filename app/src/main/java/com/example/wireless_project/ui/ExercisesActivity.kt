package com.example.wireless_project.ui

import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.ui.recycle_view_adapter.ExercisesAdapter
import kotlinx.android.synthetic.main.fragment_exercises.*
import java.util.*
import kotlin.collections.ArrayList

class ExercisesActivity : Fragment(){
    //get current date
    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DATE)
    private var addDate = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
    //set context
    private lateinit var exercisesContext: Context
    private lateinit var exercisesAdapter : ExercisesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_exercises, container, false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act = this
        setOnClick()
        setRecycle()
    }
    //get context from this fragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        exercisesContext = context
    }
    //set up onClickListener for all button
    private fun setOnClick() {
        //add exercises button
        add_exercises.setOnClickListener {
            //go to add exercises activity(Fragment)
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, AddExercises.newInstance())
            transaction?.addToBackStack("exercises")
            transaction?.commit()
        }
        //date picker button
        date_picker.setOnClickListener {
            //call date picker dialog to choose the new date
            context?.let { it1 ->
                DatePickerDialog(
                    it1, R.style.DialogTheme,
                    DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, dayOfMonth ->
                        calendar.set(mYear, mMonth, dayOfMonth)
                        addDate = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
                        //val filter = foodList.filter { it.addedDate == addDate }
                        date.text = SimpleDateFormat("MMMM dd, yyyy").format(calendar.time)
                        setRecycle()
                    }, year, month, day
                )
            }?.show()
        }
    }
    private fun setRecycle(){
        //call recycleView adapter to retrieve data and generate layout for recycle view
        exercisesAdapter = ExercisesAdapter(ArrayList(exercisesList.filter { it1 -> it1.addedDate== addDate}))
        recycleView.apply {
            layoutManager = GridLayoutManager(exercisesContext, 1, GridLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            adapter = exercisesAdapter
            onFlingListener= null
            exercisesAdapter.notifyDataSetChanged()
        }
    }
    companion object{
        lateinit var act : ExercisesActivity
        lateinit var exercisesList: MutableList<Exercises>
        //use to return fragment for calling this page
        fun newInstance(list: MutableList<Exercises>): ExercisesActivity {
            exercisesList = list
            return ExercisesActivity()
        }
        //delete exercises data from the exercises list in main activity and refresh recycle view
        fun deleteData(exercises: Exercises){
            val index = exercisesList.indexOf(exercises)
            exercisesList.removeAt(index)
            act.exercisesAdapter.notifyItemRemoved(index)
            act.setRecycle()
        }
    }
}