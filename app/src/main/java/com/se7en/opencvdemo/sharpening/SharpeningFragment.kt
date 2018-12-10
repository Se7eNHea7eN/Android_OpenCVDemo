package com.se7en.opencvdemo.sharpening

import com.se7en.opencvdemo.BaseImageFragment
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class SharpeningFragment: BaseImageFragment() {
    private val laplace = Mat(3, 3, CvType.CV_32F).apply {
        put(
            0, 0,
            floatArrayOf(
                -1f, -1f, -1f,
                -1f, 9f, -1f,
                -1f, -1f, -1f
            )
        )
    }
    override fun ProcessImage(rgba: Mat, gray: Mat): Mat {
        Imgproc.filter2D(rgba,rgba,-1,laplace)
        return rgba
    }
}