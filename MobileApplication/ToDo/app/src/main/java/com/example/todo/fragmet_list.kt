package com.example.todo

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_add_task.*
import kotlinx.android.synthetic.main.fragment_fragmet_list.*


class fragmet_list : Fragment() {

    companion object {
        var taskList: ArrayList<Task> = ArrayList()

    }
    //var taskList: ArrayList<Task> = ArrayList()
    //var myAdapter = CustomAdapter(requireContext(), taskList)
    var myAdapter: CustomAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_fragmet_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        myAdapter = CustomAdapter(requireContext(), taskList)
        list_item.adapter = myAdapter


        list_item.setOnItemLongClickListener { _, _,index,_ ->
            taskList.removeAt(index)
            myAdapter?.notifyDataSetChanged()
            true
        }

        if (activity?.intent != null) {
            val extra = activity!!.intent.getStringExtra("Data")
            if (extra == "yes") {
                val title = activity!!.intent.getStringExtra("title")
                val description = activity!!.intent.getStringExtra("description")
                val time = activity!!.intent.getStringExtra("time")
                val date = activity!!.intent.getStringExtra("date")
                val id = activity!!.intent.getStringExtra("id").toInt()
                val drawable = resources.getDrawable(id)
                val color = activity!!.intent.getStringExtra("color")
                addItem(title, description, time, date, drawable, color)
                activity!!.intent.removeExtra("Data")
            }
        }

    }

    fun addItem(title: String, description: String, time: String, date: String, pic: Drawable, color: String) {

        taskList.add(
            Task(title, description, time, date, pic, color))
        myAdapter?.notifyDataSetChanged()

    }

}
