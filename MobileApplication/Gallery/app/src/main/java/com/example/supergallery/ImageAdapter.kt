package com.example.supergallery

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class ImageAdapter(var context: Context, var images: ArrayList<Image>) : BaseAdapter() {

    private class ViewHolder(row: View) {
        var image: ImageView

        init {
            this.image = row.findViewById(R.id.imageView) as ImageView
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.image_layout, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val image: Image = getItem(position) as Image

        val mydraw = context.resources.getDrawable(context.resources.getIdentifier(image.name, "drawable", context.packageName))

        viewHolder.image.setImageDrawable(mydraw)

        return view as View
    }

    override fun getItem(position: Int): Any {
        return images.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return images.count()
    }


}