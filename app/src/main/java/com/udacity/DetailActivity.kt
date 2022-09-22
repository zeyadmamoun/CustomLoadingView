package com.udacity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val fileName = intent.extras?.getString("fileName")
        val status = intent.extras?.getString("status")

        val filenameTv = findViewById<TextView>(R.id.file_name)
        val statusTv = findViewById<TextView>(R.id.status)
        val motionLayout = findViewById<MotionLayout>(R.id.motion_layout)

        filenameTv.text = fileName
        statusTv.text = status

        motionLayout.setTransitionListener(object :MotionLayout.TransitionListener{
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                val intent = Intent(this@DetailActivity,MainActivity::class.java)
                startActivity(intent)
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

        })
    }

}
