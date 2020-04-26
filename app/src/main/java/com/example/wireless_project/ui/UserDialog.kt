package com.example.wireless_project.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.user_dialog.*
import java.util.*

class UserDialog: DialogFragment() {
    private val disposable = CompositeDisposable()
    companion object{
        lateinit var user: User
        fun newInstance(user: User): UserDialog {
            this.user = user
            return UserDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.user_dialog, container, false)
    override fun onStart() {
        super.onStart()
        setUI()
    }
    private fun setUI(){
        val date = user.dob!!.split("/")
        val calendar =android.icu.util.Calendar.getInstance()
        calendar.set(date[2].toInt(), date[1].toInt().minus(1), date[0].toInt())
        dob.text = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
        var gen = getString(R.string.male)
        if(user.gender == 1){
            gen = getString(R.string.female)
        }
        genderVal.text = gen
        height.setText((user.height.toString()))
        weight.setText(user.weight.toString())
        setOnClick()
    }
    private fun setOnClick(){
        genderVal.setOnClickListener {
            loadDialog()
        }
        dob.setOnClickListener {
            datePicker()
        }
        cancel.setOnClickListener {
            dismiss()
        }
        save.setOnClickListener {
            var gender : Int? = 0
            if(genderVal.text.toString() == getString(R.string.female)){
                gender = 1
            }
            val heightVal = height.text.toString().toDouble()
            val weightVal = weight.text.toString().toDouble()
            user.gender = gender
            user.dob = dob.text.toString()
            user.height = heightVal
            user.weight = weightVal
            disposable.add(MainActivity.getUserSource().updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    UserActivity.updateUser(user)
                    dismiss()})
        }
    }
    private fun datePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DATE)
        val dateOfBirth = context?.let {
            DatePickerDialog(
                it, R.style.DialogTheme,
                DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, dayOfMonth ->
                    calendar.set(mYear, mMonth, dayOfMonth)
                    dob.text = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
                    //dob.text = "$dayOfMonth/${mMonth+1}/$mYear"
                }, year, month, day)
        }
        dateOfBirth!!.show()

    }
    private fun loadDialog(){
        val dialog = context?.let { it1 -> Dialog(it1) }
        if (dialog != null) {
            dialog.setContentView(R.layout.gender_dialogfragment)
            dialog.window?.apply {
                setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT)
                setGravity(Gravity.CENTER)
            }
            val cancel = dialog.findViewById<Button>(R.id.cancel)
            val summit = dialog.findViewById<Button>(R.id.summit)
            val gen = dialog.findViewById<RadioGroup>(R.id.gender)
            summit.setOnClickListener {
                val radioId = gen.checkedRadioButtonId
                val radioButton = dialog.findViewById<RadioButton>(radioId)
                if (radioButton != null) {
                    genderVal.text = radioButton.text.toString()
                }
                dialog.dismiss()
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun onStop() {
        super.onStop()
        dialog?.dismiss()
    }
}