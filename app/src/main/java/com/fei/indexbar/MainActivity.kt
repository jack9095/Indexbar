package com.fei.indexbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fei.indexbar.focus.FocusActivity
import com.fei.indexbar.quicksidebar.QuickActivity
import com.fei.indexbar.sidebarview.SideBarViewActivity
import com.fei.indexbar.wave.WaveActivity

/**
 * 网上搜 indexBar 或者 SideBar
 *
 * https://www.cnblogs.com/jasongaoh/p/7834234.html
 * https://www.jianshu.com/p/af807ab6029a
 *
 * https://search.gitee.com/?skin=rec&type=repository&q=SideBar&lang=java
 *
 * 和微信通讯录一摸一样的
 * https://gitee.com/ernestchang/Android-QuickSideBar?_from=gitee_search
 * https://gitee.com/djun100/WaveSideBar?_from=gitee_search
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

    fun onQuick(view: View) {
        startActivity(Intent(this@MainActivity, QuickActivity::class.java))
    }

    fun onWave(view: View) {
        startActivity(Intent(this@MainActivity, WaveActivity::class.java))
    }
}