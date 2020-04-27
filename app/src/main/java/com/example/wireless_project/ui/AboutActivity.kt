package com.example.wireless_project.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wireless_project.R
import kotlinx.android.synthetic.main.fragment_setting.*

class AboutActivity: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_about, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragment = activity?.supportFragmentManager
        //Go back to previous activity(Fragment)
        back.setOnClickListener {
            fragment?.popBackStack()
        }
    }
}