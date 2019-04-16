package com.example.todo


import android.R.attr.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_add_task.*
import java.util.*
import android.R.id




class add_task : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    var day: Int? = null
    var month: Int? = null
    var year: Int? = null
    var hour: Int? = null
    var minute: Int? = null
    var finalday: Int? = null
    var finalmonth: Int? = null
    var finalyear: Int? = null
    var finalhour: Int? = null
    var finalminute: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addTaskButton.setOnClickListener { onClick(it) }
        dateTime.setOnClickListener { onDateTimeClicked(it) }
    }

    fun onClick(view: View){
        if (dateTime.text == "Date, time") {
            Toast.makeText(this.context, "pick date and time", Toast.LENGTH_SHORT).show()
            return
        }
        val title = titleText.text.toString()
        val description =  describeText.text.toString()
        val color = getView()!!.findViewById<RadioButton>(priorityGroup.checkedRadioButtonId).tag.toString()

        val name = getView()!!.findViewById<RadioButton>(picGroup.checkedRadioButtonId).tag.toString()
        val id = resources.getIdentifier(name, "drawable", activity?.packageName)
        val drawable = resources.getDrawable(id)

        val time = dateTime.text.toString().substring(0, dateTime.text.toString().indexOf(' '))

        val date = dateTime.text.toString().substring(dateTime.text.toString().indexOf(' ')).replace(" ","")

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val myintent = Intent(activity, MainActivity::class.java)
            myintent.putExtra("Data", "yes")
            myintent.putExtra("title", title)
            myintent.putExtra("description", description)
            myintent.putExtra("time", time)
            myintent.putExtra("date", date)
            myintent.putExtra("id", id.toString())
            myintent.putExtra("color", color)
            myintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(myintent)
            this.activity!!.finish()
        } else {
            val frag = fragmentManager!!.findFragmentById(R.id.mainfragment) as fragmet_list
            frag.addItem(title, description, time, date, drawable, color)
            radioGreen.isChecked = true
            radioFood.isChecked = true
            titleText.setText("")
            describeText.setText("")
            dateTime.text = "Date, time"
        }
    }

    fun onDateTimeClicked(view: View) {
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)

        val datePickedDialog = DatePickerDialog(this.context!!, this, year!!, month!!, day!!)
        datePickedDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        finalyear = year
        finalmonth = month + 1
        finalday = dayOfMonth

        val c = Calendar.getInstance()
        hour = c.get(Calendar.HOUR_OF_DAY)
        minute = c.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this.context!!, this, hour!!, minute!!, DateFormat.is24HourFormat(this.context))
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        finalhour = hourOfDay
        finalminute = minute
        val result  = "$finalhour:$finalminute   $finalday.$finalmonth.$finalyear"
        dateTime.text = result
    }

}
