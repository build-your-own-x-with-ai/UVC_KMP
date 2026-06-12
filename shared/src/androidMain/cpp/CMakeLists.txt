cmake_minimum_required(VERSION 3.22.1)
project(libuvc_jni)

set(CMAKE_CXX_STANDARD 17)

add_library(uvc_jni SHARED
    uvc_jni.cpp
)

find_library(log-lib log)

target_link_libraries(uvc_jni
    ${log-lib}
)
