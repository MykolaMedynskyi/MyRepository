package com.example.todo

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomAdapter(var context: Context, var task: ArrayList<Task>) : BaseAdapter() {

    private class ViewHolder(row: View) {
        var title: TextView
        var describe: TextView
        var time: TextView
        var date: TextView
        var image: ImageView
        var color: String

        init {
            this.title = row.findViewById(R.id.titleView) as TextView
            this.describe = row.findViewById(R.id.describeView) as TextView
            this.time = row.findViewById(R.id.timeView) as TextView
            this.date = row.findViewById(R.id.dateView) as TextView
            this.image = row.findViewById(R.id.imageView) as ImageView
            this.color = ""
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.task_layout, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val task: Task = getItem(position) as Task
        viewHolder.title.text = task.title
        viewHolder.describe.text = task.describe
        viewHolder.time.text = task.time
        viewHolder.date.text = task.date
        viewHolder.image.setImageDrawable(task.image)
        viewHolder.image.setBackgroundColor(Color.parseColor(task.color))

        return view as View
    }

    override fun getItem(position: Int): Any {
        return task.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return task.count()
    }

}