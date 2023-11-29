package com.porfirio.androidprojectpdg.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.porfirio.androidprojectpdg.R
import com.porfirio.androidprojectpdg.databinding.ActivityMainBinding
import com.porfirio.androidprojectpdg.ui.fragments.PropiedadesListFragment

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PropiedadesListFragment())
                .commit()
        }

    }
}