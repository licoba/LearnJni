#include "com_licoba_learnjni_MyJni.h"
#include <string>

extern "C" {
    jstring JNICALL Java_com_licoba_learnjni_MyJni_getMyHelloString
        (JNIEnv *env, jobject) {
        std::string hello = "I am a test string";
        return env->NewStringUTF(hello.c_str());
    }

    jint JNICALL Java_com_licoba_learnjni_MyJni_add
            (JNIEnv *env, jobject, jint a, jint b) {
        return a + b;
    }
}