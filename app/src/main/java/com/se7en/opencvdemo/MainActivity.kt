package com.se7en.opencvdemo

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.se7en.opencvdemo.blurring.SmoothFragment
import com.se7en.opencvdemo.edgedetect.EdgeDetectFragment
import com.se7en.opencvdemo.facedetect.FaceDetectFragment
import com.se7en.opencvdemo.sharpening.SharpeningFragment
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

    private lateinit var mRgba: Mat
    private lateinit var mGray: Mat
    private var width: Int = 0
    private var height: Int = 0

    private var currentImageFragment: BaseImageFragment? = null

    private var lastFrameDrawTime: Long = 0
    private var frameRate: Float = 0f
        set(value) {
            field = value
            fps.post {
                fps.text = "%.1f".format(frameRate)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        drawer_layout.openDrawer(GravityCompat.START)

        nav_view.setNavigationItemSelectedListener(this)
        cameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT)
        cameraView.visibility = CameraBridgeViewBase.VISIBLE
        cameraView.setCvCameraViewListener(this)
        cameraView.enableView()
        lastFrameDrawTime = System.currentTimeMillis()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //
    fun replaceFragment(fragment: BaseImageFragment): BaseImageFragment {
        fragment.arguments = Bundle().apply {
            putInt("width", width)
            putInt("height", height)
            putInt("rotation", cameraView.display.rotation)
        }
        currentImageFragment = fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
        return fragment
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.blurring -> {
                replaceFragment(SmoothFragment())
            }
            R.id.edgedetect -> {
                replaceFragment(EdgeDetectFragment())
            }
            R.id.sharpening -> {
                replaceFragment(SharpeningFragment())
            }
            R.id.facedetect -> {
                replaceFragment(FaceDetectFragment())
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        this.width = width
        this.height = height
        mRgba = Mat(width, height, CvType.CV_8UC4)
        mGray = Mat(height, width, CvType.CV_8UC4)

//        if(currentImageFragment == null)
//            replaceFragment(SmoothFragment())
    }

    override fun onCameraViewStopped() {
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        Core.flip(inputFrame.rgba(), mRgba, 1);
        Core.flip(inputFrame.gray(), mGray, 1);
        val current = System.currentTimeMillis()
        frameRate = 1000f / (current - lastFrameDrawTime)
        Log.d("frameRate", frameRate.toString())
        lastFrameDrawTime = current
        if (currentImageFragment != null) {
            return if (currentImageFragment!!.isPressing)
                currentImageFragment!!.OriginalImage(mRgba, mGray)
            else
                currentImageFragment!!.ProcessImage(mRgba, mGray)
        }

        return mRgba
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
