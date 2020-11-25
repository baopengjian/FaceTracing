# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)



add_library(
             native-lib

             SHARED

             src/main/cpp/native-lib.cpp )

include_directories(src/main/cpp/include)


set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}")

target_link_libraries(   native-lib log opencv_java3 android )