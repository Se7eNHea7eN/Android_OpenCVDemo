#include "Smooth.h"
#include <opencv2/core.hpp>
#include <string>
#include <vector>

#include <android/log.h>
#include <opencv2/imgproc/imgproc_c.h>

#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))
using namespace std;
using namespace cv;

void
Java_com_se7en_opencvdemo_blurring_BlurringFragment_nativeSmooth(JNIEnv *, jclass, jlong src, jlong dst, jint param1,
                                                                 jint param2, jdouble param3, jdouble param4) {
    CvMat cvsrcMat = *((Mat *) src);

    CvMat cvdstMat = *((Mat *) dst);
//    uchar kernalData[4] = {1, 0,
//                          0, -1};
//    CvMat kernal = cvMat(2, 2, CV_8U, kernalData);
//
//    cvFilter2D(&cvsrcMat, &cvdstMat, &kernal);

    cvSmooth(&cvsrcMat, &cvdstMat, CV_GAUSSIAN, param1, param2, param3, param4);
}
