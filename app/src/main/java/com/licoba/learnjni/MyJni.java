package com.licoba.learnjni;

// 将所有的native方法都定义在这里
public class MyJni {

    // 加载本地库文件
    static {
        System.loadLibrary("myjni");
    }

    public native String getMyHelloString();

    public native int add(int a, int b);
}
