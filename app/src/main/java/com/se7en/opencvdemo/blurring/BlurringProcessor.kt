package com.se7en.opencvdemo.blurring

import com.se7en.opencvdemo.BaseImageProcessor
import org.opencv.core.Mat

class BlurringProcessor : BaseImageProcessor {
    override fun ProcessImage(rgba: Mat, gray: Mat): Mat = rgba
}