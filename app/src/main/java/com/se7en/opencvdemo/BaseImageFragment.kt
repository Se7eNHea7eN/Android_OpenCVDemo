package com.se7en.opencvdemo

import androidx.fragment.app.Fragment
import org.opencv.core.Mat

abstract class BaseImageFragment : Fragment() {
    protected var width: Int = 0
    protected var height: Int = 0
    protected var rotation: Int = 0

    init {
        arguments?.apply {
            width = getInt("width")
            height = getInt("width")
            rotation = getInt("rotation")
        }
    }

    abstract fun ProcessImage(rgba: Mat, gray: Mat): Mat
}