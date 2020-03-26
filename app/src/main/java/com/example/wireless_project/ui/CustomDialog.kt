package com.example.wireless_project.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.wireless_project.R
import kotlinx.android.synthetic.main.dialogfragment.*


class CustomDialog : DialogFragment(){
    interface OnInputListener {
        fun sendInput(input: String?)
    }

    var mOnInputListener: OnInputListener? = null

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
    ): View? = inflater.inflate(R.layout.dialogfragment, container, false)

    private fun setUp(){
        summit.setOnClickListener {
            var input : String = text.text.toString()
            mOnInputListener?.sendInput(input)
            dismiss()
        }
        cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnInputListener = activity as OnInputListener
    }


}