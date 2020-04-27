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
import com.example.wireless_project.database.entity.Food
import com.example.wireless_project.ui.recycle_view_adapter.FoodAdapter
import kotlinx.android.synthetic.main.fragment_food.*
import java.util.*
import kotlin.collections.ArrayList

class FoodActivity : Fragment(){
    //get current date
    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DATE)
    private var addDate = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
    private lateinit var foodAdapter : FoodAdapter
    private lateinit var foodContext: Context
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set act as this activity
        act = this
        setOnClick()
        setRecycleView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        foodContext = context
    }
    //set onClickListener for all button
    private fun setOnClick() {
        //add food button
        add_food.setOnClickListener {
            //go to add food activity page
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, AddFood.newInstance())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        //date picker button
        date_picker.setOnClickListener {
            //open date picker dialog and pick new date
            context?.let { it1 ->
                DatePickerDialog(
                    it1, R.style.DialogTheme,
                    DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, dayOfMonth ->
                        calendar.set(mYear, mMonth, dayOfMonth)
                        addDate = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
                        date.text = SimpleDateFormat("MMMM dd, yyyy").format(calendar.time)
                        setRecycleView()
                    }, year, month, day
                )
            }?.show()
        }
    }
    //call recycleView adapter to generate layout for RecycleView
    private fun setRecycleView(){
        foodAdapter = FoodAdapter(ArrayList(foodList.filter { it.addedDate == addDate }))
        recycleView.apply {
            layoutManager = GridLayoutManager(foodContext, 1, GridLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            adapter = foodAdapter
            onFlingListener= null
            foodAdapter.notifyDataSetChanged()
        }
    }

    companion object{
        lateinit var foodList: MutableList<Food>
        lateinit var act: FoodActivity
        //use to set food list that get from main activity and return this activity to load fragment
        fun newInstance(list: MutableList<Food>): FoodActivity {
            foodList = list
            return FoodActivity()
        }
        //delete food from list, that is in main activity and refresh recycle view
        fun deleteData(food: Food){
            val index = foodList.indexOf(food)
            foodList.removeAt(index)
            act.foodAdapter.notifyItemRemoved(index)
            act.setRecycleView()
        }
    }
}
