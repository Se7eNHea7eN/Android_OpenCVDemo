package com.se7en.opencvdemo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Surface
import android.view.WindowManager
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FdActivity : Activity(), CvCameraViewListener2 {
    companion object {
        private val TAG = "OCVSample::Activity"
        private val FACE_RECT_COLOR = Scalar(0.0, 255.0, 0.0, 255.0)
        val JAVA_DETECTOR = 1
        val NATIVE_DETECTOR = 0

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("opencv_java3");
            System.loadLibrary("opencv_java");
            System.loadLibrary("native-lib")
        }
    }

    private var mItemFace50: MenuItem? = null
    private var mItemFace40: MenuItem? = null
    private var mItemFace30: MenuItem? = null
    private var mItemFace20: MenuItem? = null
    private var mItemType: MenuItem? = null

    private var mRgba: Mat? = null
    private var mGray: Mat? = null
    private var Matlin : Mat? = null
    private var gMatlin : Mat? = null
    private var mCascadeFile: File? = null
    private var mJavaDetector: CascadeClassifier? = null
    private var mNativeDetector: DetectionBasedTracker? = null

    private var mDetectorType = JAVA_DETECTOR
    private val mDetectorName: Array<String>

    private var mRelativeFaceSize = 0.2f
    private var mAbsoluteFaceSize = 0

    private var mOpenCvCameraView: CameraBridgeViewBase? = null

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    Log.i(TAG, "OpenCV loaded successfully")
                    try {
                        // load cascade file from application resources
                        val `is` = resources.openRawResource(R.raw.lbpcascade_frontalface)
                        val cascadeDir = getDir("cascade", Context.MODE_PRIVATE)
                        mCascadeFile = File(cascadeDir, "lbpcascade_frontalface.xml")
                        val os = FileOutputStream(mCascadeFile)

                        val buffer = ByteArray(4096)
                        var bytesRead: Int
                        while ((let {
                                bytesRead = `is`.read(buffer)
                                bytesRead
                            }) != -1) {
                            os.write(buffer, 0, bytesRead)
                        }
                        `is`.close()
                        os.close()

                        mJavaDetector = CascadeClassifier(mCascadeFile!!.absolutePath)
                        if (mJavaDetector!!.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier")
                            mJavaDetector = null
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile!!.absolutePath)

                        mNativeDetector =
                                DetectionBasedTracker(mCascadeFile!!.absolutePath, 0)

                        cascadeDir.delete()

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Log.e(TAG, "Failed to load cascade. Exception thrown: $e")
                    }

                    mOpenCvCameraView!!.enableView()
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    init {
        mDetectorName = arrayOf("Java", "Native (tracking)")

        Log.i(TAG, "Instantiated new " + this.javaClass)
    }

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "called onCreate")
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.face_detect_surface_view)

        mOpenCvCameraView = findViewById(R.id.fd_activity_surface_view)
        mOpenCvCameraView!!.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT)
        mOpenCvCameraView!!.visibility = CameraBridgeViewBase.VISIBLE
        mOpenCvCameraView!!.setCvCameraViewListener(this)
    }

    public override fun onPause() {
        super.onPause()
        if (mOpenCvCameraView != null)
            mOpenCvCameraView!!.disableView()
    }

    public override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        mOpenCvCameraView!!.disableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mGray = Mat(height, width, CvType.CV_8UC4)
        mRgba = Mat(width, height, CvType.CV_8UC4)
        gMatlin = Mat(width, height, CvType.CV_8UC4)
        Matlin = Mat(width, height, CvType.CV_8UC4)
        mAbsoluteFaceSize = (height * 0.2f).toInt()
    }

    override fun onCameraViewStopped() {
        mGray?.release()
        mRgba?.release()
    }

    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat? {
        mGray = inputFrame.gray();
        mRgba = inputFrame.rgba();

        if (mAbsoluteFaceSize == 0) {
            val height = mGray!!.rows()
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize)
            }
            mNativeDetector!!.setMinFaceSize(mAbsoluteFaceSize)
        }

        val rotation = mOpenCvCameraView!!.getDisplay().getRotation()

        //使前置的图像也是正的
//        if (camera_scene == CAMERA_FRONT) {
            Core.flip(mRgba, mRgba, 1);
            Core.flip(mGray, mGray, 1);
//        }

        //MatOfRect faces = new MatOfRect();

        if (rotation == Surface.ROTATION_0) {
            val faces = MatOfRect()
            Core.rotate(mGray, gMatlin, Core.ROTATE_90_CLOCKWISE);
            Core.rotate(mRgba, Matlin, Core.ROTATE_90_CLOCKWISE);
            mNativeDetector!!.detect(gMatlin, faces)

            val faceArray = faces.toArray();
            faceArray.forEach {
                Imgproc.rectangle(Matlin, it.tl(), it.br(), FACE_RECT_COLOR, 2);
            }

            Core.rotate(Matlin, mRgba, Core.ROTATE_90_COUNTERCLOCKWISE);

        } else {
            val faces = MatOfRect()
            mNativeDetector!!.detect(mGray,faces)
            faces.toArray().forEach {
                Imgproc.rectangle(mRgba, it.tl(), it.br(),FACE_RECT_COLOR, 2);

            }
        }

        return mRgba
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.i(TAG, "called onCreateOptionsMenu")
        mItemFace50 = menu.add("Face size 50%")
        mItemFace40 = menu.add("Face size 40%")
        mItemFace30 = menu.add("Face size 30%")
        mItemFace20 = menu.add("Face size 20%")
        mItemType = menu.add(mDetectorName[mDetectorType])
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i(TAG, "called onOptionsItemSelected; selected item: $item")
        if (item === mItemFace50)
            setMinFaceSize(0.5f)
        else if (item === mItemFace40)
            setMinFaceSize(0.4f)
        else if (item === mItemFace30)
            setMinFaceSize(0.3f)
        else if (item === mItemFace20)
            setMinFaceSize(0.2f)
        else if (item === mItemType) {
            val tmpDetectorType = (mDetectorType + 1) % mDetectorName.size
            item.title = mDetectorName[tmpDetectorType]
            setDetectorType(tmpDetectorType)
        }
        return true
    }

    private fun setMinFaceSize(faceSize: Float) {
        mRelativeFaceSize = faceSize
        mAbsoluteFaceSize = 0
    }

    private fun setDetectorType(type: Int) {
        if (mDetectorType != type) {
            mDetectorType = type

            if (type == NATIVE_DETECTOR) {
                Log.i(TAG, "Detection Based Tracker enabled")
                mNativeDetector!!.start()
            } else {
                Log.i(TAG, "Cascade detector enabled")
                mNativeDetector!!.stop()
            }
        }
    }


}
