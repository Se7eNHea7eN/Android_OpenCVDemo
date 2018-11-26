package com.se7en.opencvdemo

import org.opencv.core.Mat

interface BaseImageProcessor{
    fun ProcessImage(rgba: Mat,gray:Mat):Mat
}