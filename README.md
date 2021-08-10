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
