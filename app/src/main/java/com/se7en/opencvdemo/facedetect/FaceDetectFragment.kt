package com.se7en.opencvdemo.facedetect

import android.content.Context
import android.util.Log
import android.view.Surface
import com.se7en.opencvdemo.BaseImageFragment
import com.se7en.opencvdemo.R
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FaceDetectFragment : BaseImageFragment() {
    val TAG = "FaceDetect"
    private var Matlin: Mat? = null
    private var gMatlin: Mat? = null
    private var mCascadeFile: File? = null
    private var mNativeDetector: DetectionBasedTracker? = null

    private var mRelativeFaceSize = 0.2f
    private var mAbsoluteFaceSize = 0
    private val FACE_RECT_COLOR = Scalar(0.0, 255.0, 0.0, 255.0)

    init {
        Log.i(TAG, "OpenCV loaded successfully")
        try {
            // load cascade file from application resources
            val inputStream = resources.openRawResource(R.raw.lbpcascade_frontalface)
            val cascadeDir = context!!.getDir("cascade", Context.MODE_PRIVATE)
            mCascadeFile = File(cascadeDir, "lbpcascade_frontalface.xml")
            val os = FileOutputStream(mCascadeFile)

            val buffer = ByteArray(4096)
            var bytesRead: Int
            while ((let {
                    bytesRead = inputStream.read(buffer)
                    bytesRead
                }) != -1) {
                os.write(buffer, 0, bytesRead)
            }
            inputStream.close()
            os.close()

            mNativeDetector =
                    DetectionBasedTracker(mCascadeFile!!.absolutePath, 0)

            cascadeDir.delete()

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "Failed to load cascade. Exception thrown: $e")
        }

        gMatlin = Mat(width, height, CvType.CV_8UC4)
        Matlin = Mat(width, height, CvType.CV_8UC4)
        mAbsoluteFaceSize = (height * 0.2f).toInt()
    }

    override fun ProcessImage(rgba: Mat, gray: Mat): Mat {
        if (mAbsoluteFaceSize == 0) {
            val height = gray.rows()
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize)
            }
            mNativeDetector!!.setMinFaceSize(mAbsoluteFaceSize)
        }

        if (rotation == Surface.ROTATION_0) {
            val faces = MatOfRect()
            Core.rotate(gray, gMatlin, Core.ROTATE_90_CLOCKWISE);
            Core.rotate(rgba, Matlin, Core.ROTATE_90_CLOCKWISE);
            mNativeDetector!!.detect(gMatlin!!, faces)

            val faceArray = faces.toArray();
            faceArray.forEach {
                Imgproc.rectangle(Matlin, it.tl(), it.br(), FACE_RECT_COLOR, 2);
            }

            Core.rotate(Matlin, rgba, Core.ROTATE_90_COUNTERCLOCKWISE);

        } else {
            val faces = MatOfRect()
            mNativeDetector!!.detect(gray, faces)
            faces.toArray().forEach {
                Imgproc.rectangle(rgba, it.tl(), it.br(), FACE_RECT_COLOR, 2);
            }
        }
        return rgba;
    }
}