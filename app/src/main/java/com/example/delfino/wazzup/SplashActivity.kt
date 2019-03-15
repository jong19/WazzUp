package com.example.delfino.wazzup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timer().schedule( object : TimerTask(){
            override fun run() {
                val to_login = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(to_login)
                finish()

            }

        },  3000L)
    }
}
