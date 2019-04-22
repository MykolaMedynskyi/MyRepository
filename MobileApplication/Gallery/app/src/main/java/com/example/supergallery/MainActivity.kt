package com.example.supergallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.fragment_image_list.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        noImages()

        getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    fun noImages() {
        var image: Image
        val dbHelper = DBHelper(this)

        if (dbHelper.count() != 0) {
            return
        }
        println("going to add some pics")
        image = Image(0, "banana", "Just banana :)", 3)
        dbHelper.insertImage(image)
        image = Image(0, "bulb", "There is no light", 3)
        dbHelper.insertImage(image)
        image = Image(0, "chicago", "Where is my pizza??", 3)
        dbHelper.insertImage(image)
        image = Image(0, "fire", "Do you like to play with fire?", 4)
        dbHelper.insertImage(image)
        image = Image(0, "graffiti", "It's a man?", 4)
        dbHelper.insertImage(image)
        image = Image(0, "hellscream", "FOR THE HORDE!", 5)
        dbHelper.insertImage(image)
        image = Image(0, "jeremy", "No actual idea on this one", 5)
        dbHelper.insertImage(image)
        image = Image(0, "may", "Ohh.. C**k", 5)
        dbHelper.insertImage(image)
        image = Image(0, "pineapple", "Not a bomb", 2)
        dbHelper.insertImage(image)
        image = Image(0, "pink", "Bubble gum", 3)
        dbHelper.insertImage(image)
        image = Image(0, "ram", "RAM", 3)
        dbHelper.insertImage(image)
        image = Image(0, "richard", "He is not small", 4)
        dbHelper.insertImage(image)
        image = Image(0, "smoke", "Do not smoke", 4)
        dbHelper.insertImage(image)
        image = Image(0, "typewriter", "What is it???", 2)
        dbHelper.insertImage(image)
    }

}
