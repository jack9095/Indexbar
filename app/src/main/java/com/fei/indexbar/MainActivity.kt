package com.fei.indexbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fei.indexbar.focus.FocusActivity
import com.fei.indexbar.sidebarview.SideBarViewActivity

/**
 * 网上搜 indexBar 或者 SideBar
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onFocus(view: View) {
        startActivity(Intent(this@MainActivity, FocusActivity::class.java))
    }

    fun onSideBarView(view: View) {
        startActivity(Intent(this@MainActivity, SideBarViewActivity::class.java))
    }
}