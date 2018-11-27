#include "Smooth.h"
#include <opencv2/core.hpp>
#include <string>
#include <vector>

#include <android/log.h>
#include <opencv2/imgproc/imgproc_c.h>

#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))
using namespace std;
using namespace cv;
void Java_com_se7en_opencvdemo_blurring_BlurringFragment_nativeSmooth(JNIEnv *, jclass,jlong src,jlong dst) {
    CvMat cvsrcMat=*((Mat*)src);

    CvMat cvdstMat=*((Mat*)dst);

    cvSmooth(&cvsrcMat,&cvdstMat, CV_GAUSSIAN, 11, 0, 0, 0);
}
