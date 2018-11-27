package com.se7en.opencvdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.opencv.core.Mat

abstract class BaseImageFragment : Fragment() {
    protected var width: Int = 0
    protected var height: Int = 0
    protected var rotation: Int = 0
    var isPressing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            width = getInt("width")
            height = getInt("width")
            rotation = getInt("rotation")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View(context!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                ACTION_DOWN ->{
                    isPressing = true
                    return@setOnTouchListener true
                }
                ACTION_UP, ACTION_CANCEL -> {
                    isPressing = false
                    return@setOnTouchListener true
                }
            }
            false
        }
        super.onViewCreated(view, savedInstanceState)
    }

    abstract fun ProcessImage(rgba: Mat, gray: Mat): Mat

    open fun OriginalImage(rgba: Mat, gray: Mat): Mat = rgba
}