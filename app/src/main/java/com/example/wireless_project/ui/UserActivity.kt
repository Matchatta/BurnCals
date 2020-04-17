package com.example.wireless_project.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.wireless_project.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_user.*


class UserActivity : Fragment(){

    private var name: String? = null
    companion object{
        const val ARG_NAME = "name"
        const val ARG_WEIGHT = "weight"
        const val ARG_HEIGHT = "height"
        const val ARG_IMAGE = "image"
        const val ARG_GENDER = "gender"
        const val ARG_DOB = "dob"
        fun newInstance(name: String?, weight: String?, height: String?, image: String?, gender: String?, dob: String?): UserActivity{
            val fragment = UserActivity()
            val bundle = Bundle().apply {
                putString(ARG_NAME, name)
                putString(ARG_WEIGHT, weight)
                putString(ARG_HEIGHT, height)
                putString(ARG_IMAGE, image)
                putString(ARG_GENDER, gender)
                putString(ARG_DOB, dob)
            }
            fragment.arguments = bundle
            return fragment
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
        setupUI()
    }

    private fun setupUI(){
        sign_out_button.setOnClickListener {
            signOut()
        }
        Glide.with(activity).load(arguments?.getString(ARG_IMAGE)).into(userImage)
        userName.text = arguments?.getString(ARG_NAME)
        height.text = arguments?.getString(ARG_HEIGHT)
        weight.text = arguments?.getString(ARG_WEIGHT)
        gender.text = arguments?.getString(ARG_GENDER)
        dob.text = getFormatDate()
    }
    private fun getFormatDate(): String {
        val MONTH = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val dob = arguments?.getString(ARG_DOB)?.split("/")
        val day = dob?.get(0)
        val numMonth = dob?.get(1)?.toInt()
        val month = MONTH[numMonth?.minus(1)!!]
        val year = dob?.get(2)
        return ("$month $day, $year")
    }
    private fun signOut(){
        val mainActivity = activity
        startActivity(mainActivity?.let { LoginActivity.getLaunchIntent(it) })
    }
}
