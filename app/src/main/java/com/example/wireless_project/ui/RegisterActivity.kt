package com.example.wireless_project.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.wireless_project.Injection
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.User
import com.example.wireless_project.ui.model.UserViewModel
import com.example.wireless_project.ui.model.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), GenderDialog.OnInputListener{
    private lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: UserViewModel by viewModels{ viewModelFactory}
    private val disposable = CompositeDisposable()
    //get current date
    val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DATE)
    private var dateOfBirth =SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
    companion object{
        var authFire : FirebaseAuth? = null
        fun getLaunchIntent(from: Context, auth: FirebaseAuth) : Intent {
            authFire = auth
            return Intent(from, RegisterActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //set user data source
        viewModelFactory = Injection.provideViewModelFactory(this)
        dob.text = SimpleDateFormat("MMMM dd, yyyy").format(calendar.time)
        setUp()
    }
    //set setOnClickListener for all button
    private fun setUp(){
        //show gender dialog
        gender.setOnClickListener {
            openDialog(GenderDialog())
        }
        //show date picker dialog
        dob.setOnClickListener {
            datePicker()
        }
        //insert new user to database
        register.setOnClickListener {
            setData()
        }
        //go back to login activity
        cancel.setOnClickListener {
            startActivity(LoginActivity.getLaunchIntent(this))
        }
    }
    //insert new user to database
    private fun setData(){
        //check that user fill all the field
        if (height.text.isEmpty()||weight.text.isEmpty()){
            if(height.text.isEmpty()){
                height.error = getString(R.string.empty_message)
                height.requestFocus()
            }
            if(weight.text.isEmpty()){
                weight.error = getString(R.string.empty_message)
                weight.requestFocus()
            }
        }
        //insert user to databse
        else{
            //retrieve user information from firebase authenication and edittext
            val firstName : String? = authFire?.currentUser?.displayName?.split(" ")?.get(0)
            val lastName : String? = authFire?.currentUser?.displayName?.split(" ")?.get(1)
            val email : String? = authFire?.currentUser?.email
            val image : String? = authFire?.currentUser?.photoUrl.toString()
            val height :Double= height.text.toString().toDouble()
            val weight :Double= weight.text.toString().toDouble()
            var genderVal : Int? = 0
            if(gender.text.toString() == getString(R.string.female)){
                genderVal = 1
            }
            val dob: String? = dateOfBirth
            val newUser = User(email.toString(), firstName, lastName, weight, height, image, genderVal, dob)
            //insert user to db and start main activity
            disposable.add(viewModel.insertUser(newUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({startActivity(MainActivity.getLaunchIntent(this, newUser, mutableListOf(), mutableListOf()))}, {
                        error -> Log.e("ErrorAtRegister", "Unable to update username", error)
                }))
        }

    }
    //datepicker dialog
    private fun datePicker(){

        val dateOfBirth = DatePickerDialog(this, R.style.DialogTheme,DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, dayOfMonth ->
            calendar.set(mYear, mMonth, dayOfMonth)
            val sto = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
            val display = SimpleDateFormat("MMMM dd, yyyy").format(calendar.time)
            this.dateOfBirth = sto
            dob.text = display
        }, year, month, day)
        dateOfBirth.show()
    }
    //use to open custon dialog
    private fun openDialog(dialog : DialogFragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        dialog.show(fragmentTransaction, dialog.javaClass::getSimpleName.toString())
    }
    //clear disposable
    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
    //get gender from gender dialog
    override fun sendGender(input: String?) {
        gender.text = input
    }

}