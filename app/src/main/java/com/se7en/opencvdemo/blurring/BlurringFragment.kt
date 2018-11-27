package com.se7en.opencvdemo.blurring

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se7en.opencvdemo.BaseImageFragment
import com.se7en.opencvdemo.databinding.BlurringBinding
import org.opencv.core.Mat


class BlurringFragment : BaseImageFragment() {
    lateinit var binding: BlurringBinding
    var kWidth: Int = 11
    var kHeight: Int = 11

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BlurringBinding.inflate(inflater, container, false)
        binding.blurring = this
        return binding.root
    }

    override fun ProcessImage(rgba: Mat, gray: Mat): Mat {
        if (kWidth % 2 == 0) kWidth++
        if (kHeight % 2 == 0) kHeight++
        nativeSmooth(rgba.nativeObj, rgba.nativeObj, kWidth, kHeight, 0.0, 0.0)

        return rgba
    }

    private external fun nativeSmooth(src: Long, dst: Long, param1: Int, param2: Int, param3: Double, param4: Double)
}