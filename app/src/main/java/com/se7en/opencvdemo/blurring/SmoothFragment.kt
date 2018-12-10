package com.se7en.opencvdemo.blurring

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se7en.opencvdemo.BaseImageFragment
import com.se7en.opencvdemo.databinding.SmoothBinding
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


class SmoothFragment : BaseImageFragment() {
    enum class SmoothType {
        Normalize,
        Gauss
    }

    private var type: SmoothType = SmoothType.Normalize
        set(value) {
            if (field != value) {
                field = value
                kernal = generateKernal(value, kSize)
            }
        }
    lateinit var binding: SmoothBinding

    var kSize: Int = 23
        set(value) {
            if (value % 2 == 0) field = value + 1
            else field = value
            kernal = generateKernal(type, value)
        }
    var sigma: Float = 9f

    private var kernal: Mat? = null

    private fun generateKernal(type: SmoothType, ksize: Int): Mat {
        when (type) {
            SmoothFragment.SmoothType.Normalize -> {
                val factor: Float = 1f / (ksize * ksize)
                return Mat(ksize, ksize, CvType.CV_32F).apply {
                    put(
                        0, 0,
                        FloatArray(ksize * ksize, { factor })
                    )
                }
            }
            SmoothType.Gauss -> {
                return Imgproc.getGaussianKernel(kSize, sigma.toDouble())
            }
            else -> {
                return Imgproc.getGaussianKernel(kSize, sigma.toDouble())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SmoothBinding.inflate(inflater, container, false)
        binding.blurring = this
        kernal = generateKernal(SmoothType.Normalize, kSize)
        binding.kernalSelect.apply {
            setOnClickListener {
                val builder = AlertDialog.Builder(activity)
                val items = arrayOf(
                    SmoothType.Normalize, SmoothType.Gauss
                )
                builder.setItems(
                    items.map { it.toString() }.toTypedArray()
                ) { _, which ->
                    type = items[which]
                    text = items[which].toString()
                }
                builder.show()
            }
        }
        return binding.root
    }

    override fun ProcessImage(rgba: Mat, gray: Mat): Mat {
        kernal?.apply {
            Imgproc.filter2D(rgba, rgba, -1, this)
        }
        return rgba
    }
}