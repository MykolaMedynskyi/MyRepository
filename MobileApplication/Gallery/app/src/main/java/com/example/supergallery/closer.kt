package com.example.supergallery


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_closer.*


class closer : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_closer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity?.intent != null) {
            val extra = activity!!.intent.getStringExtra("Data")
            if (extra == "yes") {
                val name = activity!!.intent.getStringExtra("name")
                val description = activity!!.intent.getStringExtra("description")
                val rating = activity!!.intent.getStringExtra("rating").toInt()
                val image = Image(0, name, description, rating)

                showImage(image)

                activity!!.intent.removeExtra("Data")
            }
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageView2.layoutParams.height = 380
            showImage(image_list.imageList.get(0))
        }

        button_delete.setOnClickListener { onDeleteClick(it) }
        button_rate.setOnClickListener { onRateClick(it) }

    }

    fun showImage(image: Image) {
        imageView2.setImageDrawable(context!!.resources.getDrawable(context!!.resources.getIdentifier(image.name, "drawable", context!!.packageName)))
        describeText.setText(image.description)

        button_delete.tag = image.name
        button_rate.tag = image.name

        if (image.rating != 6) {
            ratingBar.rating = image.rating.toFloat()
        } else {
            ratingBar.rating = 0.0f
        }
    }

    fun onDeleteClick(view: View) {

        val dbHelper = DBHelper(context!!.applicationContext)
        dbHelper.deleteItem(view.tag.toString())

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.activity!!.finish()
        } else {
            val frag = fragmentManager!!.findFragmentById(R.id.mainfragment) as image_list
            frag.fillData(true)
        }

    }

    fun onRateClick(view: View) {

        val dbHelper = DBHelper(context!!.applicationContext)
        dbHelper.updateItem(view.tag.toString(), ratingBar.rating.toInt(), describeText.text.toString())

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.activity!!.finish()
        } else {
            val frag = fragmentManager!!.findFragmentById(R.id.mainfragment) as image_list
            frag.fillData(false)
        }
    }

}
