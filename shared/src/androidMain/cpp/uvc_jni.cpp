#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "UVC_JNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_iosdevlog_uvc_platform_UVCNative_getVersion(JNIEnv* env, jobject) {
    return env->NewStringUTF("libuvc-jni-1.0");
}
