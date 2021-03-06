package com.example.wireless_project.ui

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.User
import kotlinx.android.synthetic.main.fragment_user.*


class UserActivity : Fragment(){

    private var name: String? = null

    companion object{
        private lateinit var act : UserActivity
        lateinit var user: User
        fun newInstance(user: User?): UserActivity{
            if (user != null) {
                this.user = user
            }
            return UserActivity()
        }
        fun updateUser(user: User){
            this.user =user
            act.setupUI()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act = this
        setupUI()
    }
    //set up layout action
    private fun setupUI(){
        //sign out button
        sign_out_button.setOnClickListener {
            signOut()
        }
        //set user picture by retrieve picture from url by using glide library
        Glide.with(activity).load(user.image).into(userImage)
        //set user information to textview
        userName.text = (user.first_name +" "+user.last_name)
        height.text = String.format("%.1f", user.height)
        weight.text = String.format("%.1f", user.weight)
        var gen = getString(R.string.male)
        if(user.gender == 1){
            gen = getString(R.string.female)
        }
        gender.text = gen
        dob.text = getFormatDate()
        //edit button
        edit.setOnClickListener {
            //open edit dialog to edit user information
            val fragmentTransaction = fragmentManager?.beginTransaction()
            val dialog: DialogFragment = UserDialog.newInstance(user)
            dialog.show(fragmentTransaction!!, dialog.javaClass::getSimpleName.toString())
        }
        //setting button
        setting_button.setOnClickListener {
            //open setting activity
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, SettingActivity())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        //about button
        about_button.setOnClickListener {
            //show application information
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, AboutActivity())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
    }
    //get formatted date by SimpleDateFormat
    private fun getFormatDate(): String {
        val dob = user.dob?.split("/")
        val day = dob?.get(0)!!.toInt()
        val numMonth = dob[1].toInt().minus(1)
        val year = dob[2].toInt()
        val calendar = Calendar.getInstance()
        calendar.set(year, numMonth, day)
        return SimpleDateFormat("MMMM dd, yyyy").format(calendar.time)
    }
    //go back to login activity
    private fun signOut(){
        val mainActivity = activity
        startActivity(mainActivity?.let { LoginActivity.getLaunchIntent(it) })
    }
}
