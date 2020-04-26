package com.example.wireless_project.ui

import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.ui.recycle_view_adapter.ExercisesAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_exercises.*
import java.util.*
import kotlin.collections.ArrayList

class ExercisesActivity : Fragment(){
    private val disposable = CompositeDisposable()
    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DATE)
    private var addDate = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        exercisesContext = context
    }

    private fun setOnClick() {
        add_exercises.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, AddExercises.newInstance())
            transaction?.addToBackStack("exercises")
            transaction?.commit()
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
                        setRecycle()
                    }, year, month, day
                )
            }?.show()
        }
    }
    private fun setRecycle(){
        Log.d("POP", "HI")
        exercisesAdapter = ExercisesAdapter(ArrayList(exercisesList.filter { it1 -> it1.addedDate== addDate}))
        try {
            recycleView.apply {
                layoutManager = GridLayoutManager(exercisesContext, 1, GridLayoutManager.VERTICAL, false)
                isNestedScrollingEnabled = false
                adapter = exercisesAdapter
                onFlingListener= null
                exercisesAdapter.notifyDataSetChanged()
            }
        }
        catch (e: Exception){ }
    }
    companion object{
        lateinit var act : ExercisesActivity
        lateinit var exercisesList: MutableList<Exercises>
        fun newInstance(list: MutableList<Exercises>): ExercisesActivity {
            exercisesList = list
            return ExercisesActivity()
        }
        fun deleteData(exercises: Exercises){
            val index = exercisesList.indexOf(exercises)
            exercisesList.removeAt(index)
            act.exercisesAdapter.notifyItemRemoved(index)
            act.setRecycle()
        }
    }
}