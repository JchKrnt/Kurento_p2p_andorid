LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := gpuimage-library
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \

LOCAL_SRC_FILES := \
	/Users/jch/Documents/workcSpace/android/Kurt_p2p_andorid/GPUImageLib/jni/yuv-decoder.c \

LOCAL_C_INCLUDES += /Users/jch/Documents/workcSpace/android/Kurt_p2p_andorid/GPUImageLib/jni
LOCAL_C_INCLUDES += /Users/jch/Documents/workcSpace/android/Kurt_p2p_andorid/GPUImageLib/src/debug/jni

include $(BUILD_SHARED_LIBRARY)
