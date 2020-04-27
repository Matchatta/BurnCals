package com.example.wireless_project.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.example.wireless_project.R
import kotlinx.android.synthetic.main.gender_dialogfragment.*

class GenderDialog: DialogFragment() {
    //This class use to create a custom dialog and send back data, that user pick, to the caller
    interface OnInputListener {
        fun sendGender(input: String?)
    }

    var mOnInputGenderListener: OnInputListener? = null
    //set dialog size
    override fun onStart() {
        super.onStart()
        setUp()
        val width = ViewGroup.LayoutParams.WRAP_CONTENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.gender_dialogfragment, container, false)

    private fun setUp(){
        //summit button
        summit.setOnClickListener {
            //get data from radioButton
            val radioId = gender.checkedRadioButtonId
            val radioButton = view?.findViewById<RadioButton>(radioId)
            if (radioButton != null) {
                mOnInputGenderListener?.sendGender(radioButton.text.toString())
            }
            dismiss()
        }
        cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnInputGenderListener = activity as OnInputListener
    }
}