#include "ImageProcessor_jni.h"
#include <opencv2/core.hpp>
#include <string>
#include <vector>

#include <android/log.h>
#include <opencv2/imgproc/imgproc_c.h>

#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))
using namespace std;
using namespace cv;

void
Java_com_se7en_opencvdemo_ImageProcessor_nativeImageAdd(JNIEnv *, jclass, jlong src1, jlong src2, jlong dst) {
    add(*((Mat *) src1), *((Mat *) src2), *((Mat *) dst));
}

void
Java_com_se7en_opencvdemo_ImageProcessor_nativeImageInverse(JNIEnv *, jclass, jlong src, jlong dst) {
    CvMat srcMat = *((Mat *) src);
    CvMat dstMat = *((Mat *) dst);
    cvNot(&srcMat, &dstMat);
}


void
Java_com_se7en_opencvdemo_ImageProcessor_nativeCanny(JNIEnv *, jclass, jlong src, jlong dst,
                                                     jdouble threshold1, jdouble threshold2, jint apertureSize) {
    CvMat srcMat = *((Mat *) src);
    CvMat dstMat = *((Mat *) dst);
    cvCanny(&srcMat, &dstMat, threshold1, threshold2, apertureSize);
}