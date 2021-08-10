# JNI 学习项目

## 一、如何新建一个支持 C++的项目

- 官方文档：https://developer.android.com/studio/projects/add-native-code?hl=zh-cn

### 步骤

- 打开 Android，进入向导页面（应该关闭所有项目）
- 然后选择创建新项目
  ![xVWT0LE](https://i.imgur.com/xVWT0LE.jpg)

- 滑动到最下面，选择 native C++

  ![WIaPIwO](https://i.imgur.com/WIaPIwO.jpg)

- 选择 Toolchain Default 可使用默认的 CMake 设置，或者使用 C++11 标准，点击 finish 完成创建。

  ![pNMxxpl](https://i.imgur.com/pNMxxpl.jpg)

gradle 的 build 操作会需要一点时间，因为需要联网下载一些资源来进行 c++ 的编译操作，可能会下载最新版本的 NDK 工具包，这个时候最好挂一个网速比较好的代理：
![uAQQruN](https://i.imgur.com/uAQQruN.jpg)
![kHr9xdq](https://i.imgur.com/kHr9xdq.jpg)

选择 Android 视图，可以看到自动生成了 `cpp`文件夹，文件夹下面会有一个`CMakeLists.txt`文件

![S8Kiq1s](https://i.imgur.com/S8Kiq1s.jpg)

点击运行，就可以在主界面看到结果“hello from c++”

## 二、如何打包 so 库

有两种方式，具体可以参考：
https://developer.android.com/ndk/guides/build

### 1.使用 ndk-build 构建

需要配合`Android.mk`、`Application.mk` 文件

这种方式要用命令行执行`ndk-build xxxx`

### 2.使用 cmake 构建

这种方式必须要有一个`CMakeLists.txt`文件

![AaszCJifMcXdNu8](https://i.loli.net/2021/08/10/AaszCJifMcXdNu8.jpg)

可以用命令行执行`cmake xxxx`

也可以在 Android studio 的 build.gradle 里配置

```
    externalNativeBuild {
        cmake {
            path file('src/main/cpp/CMakeLists.txt')
            version '3.18.1'
        }
    }
```

![CisqyWwUG7TBAVz](https://i.loli.net/2021/08/10/CisqyWwUG7TBAVz.jpg)

这样使用 gradle 打包的时候，系统会自动使用 CMake 工具链文件，等同于命令行执行的效果。

## 二、如何编写 JNI 代码

首先明白，JNI 代码是在 C++层的

大致方法为：Java 层首先定义一个 JNI 的 class，里面都是一些以 native 开头的方法，c++层就是 jni 代码，以很奇怪的名字开头，与 Java 里面定义的方法相对应，这样 Java 就可以调用 c++的方法了

### 定义 Java 接口

为了方便调用，一般都是统一做一个 Java 类，用来调用 native 的方法，这个类作为统一调用入口，在全局内单例使用

- 新建`com.licoba.learnjni.MyJni.java`，然后定义一个 native 方法
  ![XVMfrB6IjCPQD7u](https://i.loli.net/2021/08/10/XVMfrB6IjCPQD7u.jpg)

有红色不要紧没问题，因为它现在还找不到对应的 JNI 方法

### 生成.h 头文件

首先在设置页面添加一个 External Tools

- Name: `java`
- Description: `javah`
- Program: `javah`
- Arguments: `-classpath . -jni -d $SourcepathEntry$/../jni $FileClass$`
- Working directory: `$SourcepathEntry$`

  ![oDZ7FaRyBWO3Tth](https://i.loli.net/2021/08/10/oDZ7FaRyBWO3Tth.jpg)

然后在 com.licoba.learnjni.MyJni.java 上右键
![xl9TXvGAE6t2Lg5](https://i.loli.net/2021/08/10/xl9TXvGAE6t2Lg5.jpg)

看到控制台打印 就成功了
![9PjOT1r5MKLVY84](https://i.loli.net/2021/08/10/9PjOT1r5MKLVY84.jpg)

生成的头文件在`src/main/jni`目录下
![yqxTOJIQGBiCEko](https://i.loli.net/2021/08/10/yqxTOJIQGBiCEko.jpg)

### 实现.cpp 文件

我们先把头文件里面定义的两个函数实现，新建一个同名的 cpp 文件`com_licoba_learnjni_MyJni.cpp`，实现代码：

```
#include "com_licoba_learnjni_MyJni.h"
#include <string>

extern "C" jstring JNICALL Java_com_licoba_learnjni_MyJni_getMyHelloString
(JNIEnv *env, jobject){
    std::string hello = "I am a test string";
    return env->NewStringUTF(hello.c_str());
}

extern "C" jint JNICALL Java_com_licoba_learnjni_MyJni_add
(JNIEnv *env, jobject, jint a, jint b){
    return a+b ;
}
```

这个时候有报错，不要慌，提示这个.h 和.m 文件并不包含在工作目录里面，也就是我们根本就没有用到，我们需要用 Android.mk 和 application.mk 去配置
，上面有提到，可以用 `gradle+cmakelist.txt` 或者 `android.mk+application.mk` 两种方式进行配置
![RNbVE37pAmP9Kkt](https://i.loli.net/2021/08/10/RNbVE37pAmP9Kkt.jpg)

### 配置 Android.mk 和 Application.mk

在 jni 目录下：

**新建 Android.mk 文件**
官方文档
https://developer.android.com/ndk/guides/android_mk

```
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := myjni # 指定lib的名称
LOCAL_SRC_FILES := com_licoba_learnjni_MyJni.cpp
include $(BUILD_SHARED_LIBRARY)

```

官方文档
https://developer.android.com/ndk/guides/application_mk

**新建 Application.mk 文件**

```
APP_ABI := armeabi-v7a arm64-v8a
APP_PLATFORM := android-24
APP_CPPFLAGS := -fPIC -std=c++11
APP_STL := c++_static
#APP_STL := c++_shared
APP_BUILD_SCRIPT := Android.mk

```

### 最终使用

在 build.gradle 里面配置，使用 Android.mk 文件进行编译

```
    externalNativeBuild {
        ndkBuild {
            path file('src/main/jni/Android.mk')
        }
    }
```

最后在 MainActivity 里面使用：

```
    MyJni myJni = new MyJni();
    tv.setText(myJni.getMyHelloString());
    tv.setText(String.valueOf(myJni.add(5,8)) );
```

可以看到运行结果，为 5+8 = 13

![1rGCnpiYSHw48vy](https://i.loli.net/2021/08/10/1rGCnpiYSHw48vy.jpg)

## so 库的打包和使用

### 打包

cd 到 jni 目录，然后直接执行 ndk-build 命令
![Es8f2uqtdShyNGU](https://i.loli.net/2021/08/10/Es8f2uqtdShyNGU.jpg)

就会生成 libs 和 obj 文件夹，我们要的 so 库就在 libs 文件夹下
![ja2hdtmYMOJxNBR](https://i.loli.net/2021/08/10/ja2hdtmYMOJxNBR.jpg)

### 使用

在 app 的 build.gradle 的 android 下直接添加

```
    sourceSets {
        main {
            jniLibs.srcDirs = ['src/main/libs']
        }
    }
```

然后注释掉 externalNativeBuild 的源代码编译相关部分

```

    externalNativeBuild {
//        cmake {
//            path file('src/main/cpp/CMakeLists.txt')
//            version '3.18.1'
//        }

//        ndkBuild {
//            path file('src/main/jni/Android.mk')
//        }
    }
```

这样就和使用源码编译达到同样的效果了，运行 App 可以发现结果一致，如果删掉 v8 文件夹下面的 so 库，运行 app 直接崩溃，说明确实是使用编译出来的 so 文件，而不是源代码。
