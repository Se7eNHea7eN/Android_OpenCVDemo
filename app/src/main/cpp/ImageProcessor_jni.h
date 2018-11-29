#include <jni.h>
#ifndef _Included_Smooth
#define _Included_Smooth
#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT void JNICALL Java_com_se7en_opencvdemo_ImageProcessor_nativeImageAdd
        (JNIEnv *, jclass, jlong, jlong, jlong);

JNIEXPORT void JNICALL Java_com_se7en_opencvdemo_ImageProcessor_nativeImageInverse
        (JNIEnv *, jclass, jlong,jlong);
#ifdef __cplusplus
}
#endif
#endif
