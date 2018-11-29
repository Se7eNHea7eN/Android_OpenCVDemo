package com.se7en.opencvdemo.edgedetect

import android.os.Bundle
import com.se7en.opencvdemo.BaseImageFragment
import com.se7en.opencvdemo.ImageProcessor
import org.opencv.core.CvType
import org.opencv.core.CvType.CV_8UC4
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class EdgeDetectFragment : BaseImageFragment() {

    private val sobel1 = Mat(3, 3, CvType.CV_32F).apply {
        put(
            0, 0,
            floatArrayOf(
                1f, 2f, 1f,
                0f, 0f, 0f,
                -1f, -2f, -1f
            )
        )
    }

    private val sobel2 = Mat(3, 3, CvType.CV_32F).apply {
        put(
            0, 0,
            floatArrayOf(
                1f, 0f, -1f,
                2f, 0f, -2f,
                1f, 0f, -1f
            )
        )
    }

    private lateinit var sobel1Mat: Mat
    private lateinit var sobel2Mat: Mat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sobel1Mat = Mat(width, height, CV_8UC4)
        sobel2Mat = Mat(width, height, CV_8UC4)
    }

    override fun ProcessImage(rgba: Mat, gray: Mat): Mat {
        Imgproc.filter2D(rgba, sobel1Mat, -1, sobel1)
        Imgproc.filter2D(rgba, sobel2Mat, -1, sobel2)
        ImageProcessor.imageAdd(sobel1Mat, sobel2Mat,rgba)
        return rgba
    }

    override fun OriginalImage(rgba: Mat, gray: Mat): Mat {
        Imgproc.filter2D(rgba, sobel1Mat, -1, sobel1)
        return sobel1Mat
    }

}