package com.se7en.opencvdemo.blurring

import com.se7en.opencvdemo.BaseImageFragment
import org.opencv.core.CvType
import org.opencv.core.Mat

class BlurringFragment : BaseImageFragment() {
    val output = Mat(width,height,CvType.CV_8UC4)
    init{
    }
    override fun ProcessImage(rgba: Mat, gray: Mat): Mat {
        nativeSmooth(rgba.nativeObj,rgba.nativeObj)
        return rgba
    }

    private external fun nativeSmooth(src: Long,dst: Long)
}