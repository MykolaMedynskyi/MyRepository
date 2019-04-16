package com.example.todo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.fragment_add_task.*
import kotlinx.android.synthetic.main.fragment_fragmet_list.*

class MainActivity : AppCompatActivity() {


//    companion object {
//        var taskList: ArrayList<Task> = ArrayList()
//        //var myAdapter = CustomAdapter(, taskList)
//    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        list_item.adapter = myAdapter
//
//        list_item.setOnItemClickListener { _, _, index, _ ->
//            taskList.removeAt(index)
//            myAdapter.notifyDataSetChanged()
//        }


    }

}
