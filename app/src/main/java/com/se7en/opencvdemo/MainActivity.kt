package com.se7en.opencvdemo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    CameraBridgeViewBase.CvCameraViewListener2 {

    companion object {
        init {
            System.loadLibrary("opencv_java3");
            System.loadLibrary("opencv_java");
            System.loadLibrary("native-lib")
        }
    }
    private var mRgba: Mat? = null
    private var mGray: Mat? = null
    private var mRelativeFaceSize = 0.2f
    private var mAbsoluteFaceSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        drawer_layout.openDrawer(GravityCompat.START)

        nav_view.setNavigationItemSelectedListener(this)
        cameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT)
        cameraView.visibility = CameraBridgeViewBase.VISIBLE
        cameraView.setCvCameraViewListener(this)
        cameraView.enableView()

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

//        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(width, height, CvType.CV_8UC4);
        mGray = Mat(height, width, CvType.CV_8UC4);
    }

    override fun onCameraViewStopped() {
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        Core.flip(inputFrame.rgba(), mRgba, 1);
        Core.flip(inputFrame.gray(), mGray, 1);

        return mRgba!!
    }

    public override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        cameraView.disableView()
    }

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }


}
