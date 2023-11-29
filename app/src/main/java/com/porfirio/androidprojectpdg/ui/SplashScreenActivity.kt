package com.porfirio.androidprojectpdg.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import java.util.TimerTask

class SplashScreenActivity: AppCompatActivity() {
    private val SPLASH_DURATION: Long = 3000    //Duración de la SplashScreen en milisegundos

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        Timer().schedule(object : TimerTask(){
            override fun run() {
                //Despues de SPLASH_DURATION se iniciará la actividad principal (MainActivity)
                val intent = Intent(this@SplashScreenActivity, Login::class.java)
                startActivity(intent)
                finish()
            }
        }, SPLASH_DURATION)
    }
}