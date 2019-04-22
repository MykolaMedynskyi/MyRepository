package com.example.supergallery


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_image_list.*


class image_list : Fragment() {

    companion object {
        var imageList: ArrayList<Image> = ArrayList()

    }
    var myAdapter: ImageAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        gridView.setOnItemClickListener { _, _, index, _ ->
            openCloser(index)
        }

        fillData(false)

    }

    override fun onResume() {
        super.onResume()
        fillData(false)
    }

    fun fillData(deleted: Boolean) {
        myAdapter = ImageAdapter(requireContext(), imageList)
        gridView.adapter = myAdapter
        val dbHelper = DBHelper(activity!!.applicationContext)
        val images = dbHelper.allImages()

        imageList.clear()
        myAdapter?.notifyDataSetChanged()
        images.forEach { addItem(it) }

        if (deleted) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val frag = fragmentManager!!.findFragmentById(R.id.main2fragment) as closer
                frag.showImage(image_list.imageList.get(0))
            }
        }
    }

    private fun openCloser(index: Int) {
        val image: Image = imageList.get(index)
        val name = image.name
        val description =  image.description
        val rating = image.rating
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val myintent = Intent(activity, Main2Activity::class.java)
            myintent.putExtra("Data", "yes")
            myintent.putExtra("name", name)
            myintent.putExtra("description", description)
            myintent.putExtra("rating", rating.toString())

            startActivity(myintent)
        } else {
            val frag = fragmentManager!!.findFragmentById(R.id.main2fragment) as closer
            frag.showImage(image)
        }
    }

    fun addItem(image: Image) {

        imageList.add(image)
        myAdapter?.notifyDataSetChanged()

    }

}
