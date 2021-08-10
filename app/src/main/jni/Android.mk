LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := myjni # 指定lib的名称
LOCAL_SRC_FILES := com_licoba_learnjni_MyJni.cpp
include $(BUILD_SHARED_LIBRARY)