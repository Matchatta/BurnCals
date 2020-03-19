package com.example.wireless_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_record.*
import kotlin.jvm.internal.Intrinsics


class UserActivity : Fragment(){
    companion object{
        fun newInstance(): UserActivity = UserActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_user, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

    }

    private fun setupUI(){
        sign_out_button.setOnClickListener {
            signOut()
        }
    }
    private fun signOut(){
        val activity = activity
        FirebaseAuth.getInstance().signOut()
        startActivity(activity?.let { LoginActivity.getLaunchIntent(it) })

    }
}