package com.se7en.opencvdemo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        init {
            System.loadLibrary("opencv_java3");
            System.loadLibrary("opencv_java");
            System.loadLibrary("native-lib")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        drawer_layout.openDrawer(GravityCompat.START)

        nav_view.setNavigationItemSelectedListener(this)

//        replaceFragment(TextureMappingFragment())
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    fun replaceFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction().replace(id.fragmentContainer, fragment).commit()
//    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            id.navigation_gles10 -> replaceFragment(GLES10Fragment())
//            id.navigation_customizeshader -> replaceFragment(ShaderFragment())
//            id.navigation_texture -> replaceFragment(TextureFragment())
//            id.navigation_3d -> replaceFragment(CubeFragment())
//            id.navigation_texture_mapping -> replaceFragment(TextureMappingFragment())
//            id.navigation_lighting -> replaceFragment(LightingFragment())
//        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
