package com.se7en.opencvdemo.edgedetect

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.se7en.opencvdemo.BaseImageFragment
import com.se7en.opencvdemo.ImageProcessor
import com.se7en.opencvdemo.R
import org.opencv.core.CvType
import org.opencv.core.CvType.CV_8UC4
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


class EdgeDetectFragment : BaseImageFragment() {
    sealed class EdgeDetectStrategy {
        abstract fun process(gray: Mat): Mat

        class Sobel(val width: Int, val height: Int) : EdgeDetectStrategy() {
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
            private val sobel1Mat: Mat
            private val sobel2Mat: Mat

            init {
                sobel1Mat = Mat(width, height, CV_8UC4)
                sobel2Mat = Mat(width, height, CV_8UC4)
            }

            override fun process(gray: Mat): Mat {
                Imgproc.filter2D(gray, sobel1Mat, -1, sobel1)
                Imgproc.filter2D(gray, sobel2Mat, -1, sobel2)
                ImageProcessor.imageAdd(sobel1Mat, sobel2Mat, gray)
                return gray
            }
        }

        class Laplace: EdgeDetectStrategy() {
            private val laplace = Mat(3, 3, CvType.CV_32F).apply {
                put(
                    0, 0,
                    floatArrayOf(
                        1f, 1f, 1f,
                        1f, -8f, 1f,
                        1f, 1f, 1f
                    )
                )
            }

            override fun process(gray: Mat): Mat {
                Imgproc.filter2D(gray, gray, -1, laplace)
                return gray
            }
        }

        class Canny:EdgeDetectStrategy() {
            override fun process(gray: Mat): Mat {
                ImageProcessor.canny(gray,gray,60.0,220.0)
                return gray
            }
        }
    }

    var strategy: EdgeDetectStrategy? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        strategy = EdgeDetectStrategy.Sobel(width, height)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edgedetect, container, false).apply {
            findViewById<Button>(R.id.strategySelect).apply {
                setOnClickListener {
                    val builder = AlertDialog.Builder(activity)
                    val items = arrayOf(
                        EdgeDetectStrategy.Sobel::class.simpleName,
                        EdgeDetectStrategy.Laplace::class.simpleName,
                        EdgeDetectStrategy.Canny::class.simpleName
                    )
                    builder.setItems(
                        items
                    ) { _, which ->
                        strategy = when (which) {
                            0 -> EdgeDetectStrategy.Sobel(width, height)
                            1 -> EdgeDetectStrategy.Laplace()
                            2 -> EdgeDetectStrategy.Canny()
                            else -> EdgeDetectStrategy.Sobel(width, height)
                        }
                        text = items[which]
                    }
                    builder.show()
                }
            }
        }
    }

    override fun ProcessImage(rgba: Mat, gray: Mat): Mat {
        return strategy?.process(gray)?:gray
    }

//    override fun OriginalImage(rgba: Mat, gray: Mat): Mat {
//        ImageProcessor.imageInverse(ProcessImage(rgba, gray), gray)
//        return gray
//    }
}