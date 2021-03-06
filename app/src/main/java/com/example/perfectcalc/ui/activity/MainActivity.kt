package com.example.perfectcalc.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.perfectcalc.R
import com.example.perfectcalc.adapter.MainPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPagerMain.adapter = MainPagerAdapter(supportFragmentManager)
    }
}
