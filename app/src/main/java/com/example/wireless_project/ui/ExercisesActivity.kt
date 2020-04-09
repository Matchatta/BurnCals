package com.example.wireless_project.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.database.entity.Food
import com.example.wireless_project.ui.recycle_view_adapter.ExercisesAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_exercises.*
import java.util.*
import kotlin.collections.ArrayList

class ExercisesActivity : Fragment(){
    private val disposable = CompositeDisposable()
    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DATE)
    private lateinit var exercisesList: List<Exercises>
    private var addDate = "$day/$month/$year"
    private lateinit var exercisesContext: Context
    private lateinit var exercisesAdapter : ExercisesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_exercises, container, false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        exercisesContext = context
        setRecycleView()
    }

    private fun setOnClick() {
        add_exercises.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, AddExercises.newInstance())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        date_picker.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1, R.style.DialogTheme,
                    DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, dayOfMonth ->
                        val month = mMonth+1
                        addDate = ("$dayOfMonth/$mMonth/$mYear")
                        val filter = exercisesList.filter { it.addedDate == addDate }
                        date.text = ("$dayOfMonth/$month/$mYear")
                        setData(ArrayList(filter))
                    }, year, month, day
                )
            }?.show()
        }
    }
    private fun setRecycleView(){
        val viewModel = MainActivity.getExercisesSource()

        val email = MainActivity.userInformation?.email.toString()
        disposable.add(viewModel.getExercises(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d("ERROR62", it.toString()) }
            .subscribe {
                exercisesList = it
                setData(ArrayList(exercisesList.filter { it1 -> it1.addedDate== addDate}))
            })

    }
    private fun setData(exercisesList: ArrayList<Exercises>){
        exercisesAdapter = ExercisesAdapter(exercisesList)
        try {
            recycleView.apply {
                layoutManager = GridLayoutManager(exercisesContext, 1, GridLayoutManager.VERTICAL, false)
                isNestedScrollingEnabled = false
                adapter = exercisesAdapter
                onFlingListener= null
            }
        }
        catch (e: Exception){ }
        exercisesAdapter.notifyDataSetChanged()
    }
    companion object{
        fun newInstance(): ExercisesActivity =
            ExercisesActivity()
    }
}