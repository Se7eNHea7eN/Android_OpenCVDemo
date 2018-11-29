package com.se7en.opencvdemo.blurring

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se7en.opencvdemo.BaseImageFragment
import com.se7en.opencvdemo.databinding.BlurringBinding
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


class BlurringFragment : BaseImageFragment() {
    lateinit var binding: BlurringBinding
    var kSize: Int = 11
    var sigma: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BlurringBinding.inflate(inflater, container, false)
        binding.blurring = this
        return binding.root
    }

    override fun ProcessImage(rgba: Mat, gray: Mat): Mat {
        if (kSize % 2 == 0) kSize++
        val gauss = Imgproc.getGaussianKernel(kSize, sigma.toDouble())
        Imgproc.filter2D(rgba, rgba, -1, gauss)
        return rgba
    }
}