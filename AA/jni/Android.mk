LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := plana
LOCAL_SRC_FILES := plana.cpp

include $(BUILD_SHARED_LIBRARY)
