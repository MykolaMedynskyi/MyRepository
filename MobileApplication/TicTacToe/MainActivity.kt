package com.example.kik

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var board = Board()
    private val BUTTON_IDS = intArrayOf(
        R.id.button00, R.id.button01, R.id.button02, R.id.button03, R.id.button04,
        R.id.button10, R.id.button11, R.id.button12, R.id.button13, R.id.button14,
        R.id.button20, R.id.button21, R.id.button22, R.id.button23, R.id.button24,
        R.id.button30, R.id.button31, R.id.button32, R.id.button33, R.id.button34,
        R.id.button40, R.id.button41, R.id.button42, R.id.button43, R.id.button44
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        setButtons()
    }

    fun setButtons(){
        textView.setTextColor(Color.parseColor("#FF002E"))
        buttonRestart.visibility = View.INVISIBLE
        for (id in BUTTON_IDS) {
            val button = findViewById<View>(id) as Button
            button.setTextSize(1, 40F)
            button.setOnClickListener {

                button.text = board.turnToString()
                button.isEnabled = false
                if (board.turnToString() == "X") {
                    button.setTextColor(Color.parseColor("#FF002E"))
                    textView.setTextColor(Color.parseColor("#3003CF"))
                    textView.text = "O"
                }
                else {
                    button.setTextColor(Color.parseColor("#3003CF"))
                    textView.setTextColor(Color.parseColor("#FF002E"))
                    textView.text = "X"
                }
                board.put(button.transitionName)

                board.check4Win()
                if (board.result != 0) {
                    blockButtons()
                    textView.text = ""
                    buttonRestart.visibility = View.VISIBLE
                    if (board.result == 2) {
                        resultView.text = "TIE"
                    } else if (board.result == 1) {
                        resultView.text = "WINNER X"
                    } else {
                        resultView.text = "WINNER O"
                    }
                }

            }
        }
    }

    fun restart(view: View) {
        board.restart()
        view.visibility = View.INVISIBLE
        for (id in BUTTON_IDS) {
            val button = findViewById<View>(id) as Button
            button.isEnabled = true
            button.text = ""
        }
        textView.setTextColor(Color.parseColor("#FF002E"))
        textView.text = "X"
        resultView.text = ""
    }

    fun blockButtons() {
        for (id in BUTTON_IDS) {
            val button = findViewById<View>(id) as Button
            button.isEnabled = false
        }
    }

}
