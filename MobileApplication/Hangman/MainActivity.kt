package com.example.hangman

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent



class MainActivity : AppCompatActivity() {

    protected lateinit var array: Array<String>
    protected lateinit var realWord: String
    protected lateinit var word: String
    var count: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        myInit()
    }

    fun myInit() {
        array = resources.getStringArray(R.array.words)
        imageView.setImageResource(R.drawable.hm1)
        realWord = array.get((0..(array.size-1)).random())
        fixWord('-')
        textView.text = word
        println(realWord)
    }

    fun fixWord(a: Char) {
        if (a == '-') {
            word = ""
            for (i in 1..realWord.length) {
                word += "?"
            }
            return
        }
        if (realWord.contains(a)) {
            var n : Int = 0
            for (letter in realWord) {
                if (letter == a) {
                    word = word.substring(0, n) + letter.toString() + word.substring(n+1)
                    println("super + $letter + $word")
                }
                n++
            }
            textView.text = word
            n = 0
            for (letter in word) {
                if (letter == '?') {
                    n++
                }
            }
            if (n==0) {
                count = 7
            }

        } else {
            count++
            if (count > 7) {
                return
            }
            val image = "hm" + count.toString()
            imageView.setImageDrawable(resources.getDrawable(resources.getIdentifier(image, "drawable", packageName)))
        }

    }

    fun restartPressed(view: View) {
//        count = 1
//        realWord = array.get((0..(array.size-1)).random())
//        textView.text = realWord
//        fixWord('-')
//        imageView.setImageResource(R.drawable.hm1)
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        this.finishAffinity()
    }

    fun buttonPressed(view: View) {
        if (count > 6) {
            return
        }
        var myButton = view as Button
        fixWord(myButton.text.get(0))
        myButton.visibility = View.INVISIBLE
    }

}
