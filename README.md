# 松果
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15) [![Build Status](https://travis-ci.org/drunlin/guokr-android.svg?branch=master)](https://travis-ci.org/drunlin/guokr-android)  

## 这是一个[果壳网](http://guokr.com)第三方Android客户端。[下载](https://github.com/drunlin/guokr-android/releases)

# 截图
<img src="/res/screenshot0.png" width=761 height=480>  
<img src="/res/screenshot1.png" width=761 height=480>  
<img src="/res/screenshot2.png" width=761 height=480>  
<img src="/res/screenshot3.png" width=761 height=476>  
<img src="/res/screenshot4.png" width=761 height=476>  

# 功能
* 浏览主题站，小组，问答。
* 查看和回复文章，帖子，问题。
* 搜索文章，帖子，问题。
* 查看通知和站内信。
* 适配手机和平板。
* 支持夜间模式。

# 新版变化
* 更新主题颜色。
* 更改内容字体大小为14sp。
* 修复不能搜索多个关键词。
* 修复其它的小问题。

# 计划
* 发帖和提问。
* 查看个人信息。
* 优化超长图片的显示。
* 修复部分弹窗转屏会消失。

# 编译
* 设置环境变量JAVA_HOME为Oracle JDK 8的路径，JAVA7_HOME为Oracle JDK 7的路径。更多请看[gradle-retrolambda](https://github.com/evant/gradle-retrolambda)。
* 设置环境变量ANDROID_HOME为Android SKD的路径，用Android Studio可以忽略。
* 对UI的测试需要注意[UiTestCase](/app/src/androidTest/java/com/github/drunlin/guokr/test/UiTestCase.java)这个类。
* ```$ ./gradlew assembleDebug```  

# 特别感谢
* 小组帖子[Guokr 出API了!?](http://www.guokr.com/post/459700/)。
* 果壳日志[Guokr数据API](http://www.guokr.com/blog/482101/)。
* 另一个开源的果壳三方客户端[SourceWall](https://github.com/NashLegend/SourceWall)。  

# License
    The MIT License (MIT)

    Copyright (c) 2016 drunlin@outlook.com

    Permission is hereby granted, free of charge, to any person obtaining a copy of
    this software and associated documentation files (the "Software"), to deal in
    the Software without restriction, including without limitation the rights to
    use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
    the Software, and to permit persons to whom the Software is furnished to do so,
    subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
    FOR A PresICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
    COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
    IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
    CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
