package com.example.supergallery

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity2_main)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT) {
            val myintent = Intent(this, MainActivity::class.java)
            startActivity(myintent)
            this.finish()
        }
    }

}