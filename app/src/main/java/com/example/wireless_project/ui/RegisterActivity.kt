package com.example.wireless_project.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
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
import java.util.*

class RegisterActivity : AppCompatActivity(), CustomDialog.OnInputListener, GenderDialog.OnInputListener{
    private lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: UserViewModel by viewModels{ viewModelFactory}
    private val disposable = CompositeDisposable()
    private var dateOfBirth =""
    companion object{
        var authFire : FirebaseAuth? = null
        fun getLaunchIntent(from: Context, auth: FirebaseAuth) : Intent {
            authFire = auth
            return Intent(from, RegisterActivity::class.java)
        }
    }
    var clickText = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModelFactory = Injection.provideViewModelFactory(this)
        setUp()
        }
    private fun setUp(){
        textView6.setOnClickListener {
            clickText = 6
            openDialog(CustomDialog())
        }
        textView8.setOnClickListener {
            clickText = 8
            openDialog(CustomDialog())
        }
        gender.setOnClickListener {
            openDialog(GenderDialog())
        }
        dob.setOnClickListener {
            datePicker()
        }
        register.setOnClickListener {
            setData()
        }
    }
    private fun setData(){

        val firstName : String? = authFire?.currentUser?.displayName?.split(" ")?.get(0)
        val lastName : String? = authFire?.currentUser?.displayName?.split(" ")?.get(1)
        val email : String? = authFire?.currentUser?.email
        val image : String? = authFire?.currentUser?.photoUrl.toString()
        val height :Double= textView6.text.toString().toDouble()
        val weight :Double= textView8.text.toString().toDouble()
        val gender : String? = gender.text.toString()
        val dob: String? = dateOfBirth
        val newUser = User(email.toString(), firstName, lastName, weight, height, image, gender, dob)
        disposable.add(viewModel.insertUser(newUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({startActivity(MainActivity.getLaunchIntent(this, newUser))}, {
                    error -> Log.e("ErrorAtRegister", "Unable to update username", error)
            }))
    }
    private fun datePicker(){
        val MONTH = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val calendar =Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DATE)
        val dateOfBirth = DatePickerDialog(this, R.style.DialogTheme,DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, dayOfMonth ->
            val textMonth = MONTH[mMonth-1]
            this.dateOfBirth = ("$dayOfMonth/$mMonth/$mYear")
            dob.text = ("$textMonth $dayOfMonth, $mYear")
        }, year, month, day)
        dateOfBirth.show()

    }
    private fun openDialog(dialog : DialogFragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        dialog.show(fragmentTransaction, dialog.javaClass::getSimpleName.toString())
    }
    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    override fun sendInput(input: String?) {
        if(clickText==6){
            textView6.text = input
        }
        else if(clickText ==8){
            textView8.text = input
        }
    }

    override fun sendGender(input: String?) {
        gender.text = input
    }

}