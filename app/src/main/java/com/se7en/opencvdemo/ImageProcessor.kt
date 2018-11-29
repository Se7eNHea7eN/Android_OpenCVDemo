package com.se7en.opencvdemo

import org.opencv.core.Mat

object ImageProcessor {
    fun imageAdd(src1: Mat, src2: Mat, dst: Mat) {
        nativeImageAdd(src1.nativeObj, src2.nativeObj, dst.nativeObj)
    }

    fun imageInverse(src:Mat,dst:Mat) = nativeImageInverse(src.nativeObj,dst.nativeObj)

    private external fun nativeImageAdd(src1: Long, src2: Long, dst: Long)
    private external fun nativeImageInverse(src:Long,dst:Long)
}