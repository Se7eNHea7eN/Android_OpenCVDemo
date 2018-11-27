#include <jni.h>
#ifndef _Included_Smooth
#define _Included_Smooth
#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT void JNICALL Java_com_se7en_opencvdemo_blurring_BlurringFragment_nativeSmooth
        (JNIEnv *, jclass, jlong, jlong);
#ifdef __cplusplus
}
#endif
#endif
