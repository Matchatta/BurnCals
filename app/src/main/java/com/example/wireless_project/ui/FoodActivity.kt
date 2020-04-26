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
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_food.*
import java.util.*
import kotlin.collections.ArrayList

class FoodActivity : Fragment(){
    private val disposable = CompositeDisposable()
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
        act = this
        setOnClick()
        setRecycleView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        foodContext = context
    }
    private fun setOnClick() {
        add_food.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, AddFood.newInstance())
            transaction?.addToBackStack(null)
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
                        //setData(ArrayList(filter))
                        setRecycleView()
                    }, year, month, day
                )
            }?.show()
        }
    }

    private fun setRecycleView(){
        foodAdapter = FoodAdapter(ArrayList(foodList.filter { it.addedDate == addDate }))
        try {
            recycleView.apply {
                layoutManager = GridLayoutManager(foodContext, 1, GridLayoutManager.VERTICAL, false)
                isNestedScrollingEnabled = false
                adapter = foodAdapter
                onFlingListener= null
                foodAdapter.notifyDataSetChanged()
            }
        }
        catch (e: Exception){ }

    }

    companion object{
        lateinit var foodList: MutableList<Food>
        lateinit var act: FoodActivity
        fun newInstance(list: MutableList<Food>): FoodActivity {
            foodList = list
            return FoodActivity()
        }
        fun deleteData(food: Food){
            val index = foodList.indexOf(food)
            foodList.removeAt(index)
            act.foodAdapter.notifyItemRemoved(index)
            act.setRecycleView()
        }
    }
}
