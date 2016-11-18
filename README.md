# Android群英传学习笔记

## 第一章
本章主要讲述了Google的生态系统，Android系统架构和Android源代码与代码目录

### 系统架构
包括Linux内核层
Dalvik与ART虚拟机运行环境
Framework框架层
Standard libraries标准开发库
Application(包含NDK app, SDK app)

### 系统组件架构
四大组件协同工作，通过Intent通信
应用运行上下文Context（分为Application,Activity,Service)

### 源代码目录和系统目录
源代码目录
> Makefile(讲述了什么是Makefile,以及它的好处)  
bionic（bionic C库）  
bootable（启动引导相关代码）  
build（存放系统编译规则等基础开发包配置）  
cts（Google兼容测试标准）  
dalvik（dalvik虚拟机）  
development（应用程序开发相关）  
external（android使用的一些开源模块）  
frameworks（Framework核心框架）  
hardware（厂商硬件适配层HAL代码）  
out（编译完成后的代码输出包）  
packages（应用程序包）  
prebuilt（x86和arm架构下预编译资源）  
sdk（sdk及模拟器）  
system（底层文件系统库，应用及组件）  
vendor(厂商定制)  


系统目录
> /system/app/（系统的app）  
/sytem/bin/(Linux自带的组件)  
/system/build.prop（系统的属性信息）  
/system/fonts/（系统字体）  
/system/framework/（核心文件，框架层）  
/system/lib/（所有的共享库）  
/system/media/（系统的提示音、铃声）  
/system/media/audio/（系统默认的铃声）  
/system/media/audio/alarms(闹铃提醒)  
/system/media/audio/notifications(通知短息提醒)  
/system/media/audio/ringtones(来电铃声)  
/system/user/（用户的配置文件，如键盘布局、共享、时区文件）  
/data/（用户的大部分数据信息）  
/data/app/(用户安装的app或升级的app)  
/data/data/(app的数据信息、文件信息、数据库信息)  
/data/system/（手机的各项系统信息，如packages.list等）  
/data/misc/（wifi和vpn信息）  

android app studio工程开发文件目录略过

## 第二章
本章主要讲述了最新的android开发工具 Android Studio安装和配置、以及其使用技巧；adb命令使用技巧；Genymotion的使用
### Android Studio 下载、配置和使用
下载
> 官网(http://developer.android.com/sdk/installing/studio.html)  
AndroidDevTools(http://www.androiddevtools.cn)  

配置
> JDK安装  
start new project时因为Gradle的下载慢的问题，解决方案(从官网下载放在本地根目录的.gradle/wrapper/dists/gradle-版本号-all/奇怪数字文件夹下，studio会自动解压)  
如果将Eclipse工程导入到Studio中  
Studio主题配置  
常用配置（悬浮提示窗、Tip提示窗、代码auto complete等）  
更新SDK  

使用
> Studio的常用界面（Debug，系统信息，Cpu，Memory Monitor等）  
Studio的断点调试查看  
如何导入已经存在的Studio工程，个人经验（命令行先运行./gradlew下载gradle，然后./gradlew build命令行编译，然后通过studio open工程就没有问题了）  

### adb 命令的使用
> adb version  
adb shell  
adb list targets  
adb push local remote  
adb pull remote local  
adb shell df（系统盘符）  
adb shell pm（PM管理信息）  
adb shell am （AM管理信息）  
adb shell input keyevent（模拟点击）  
adb shell input touchscreen(模拟滑动)  
adb shell dumpsys （运行状态，细节可以通过adb shell dumpsys -l查看）  
adb shell screenrecord /sdcard/demo.mp4 （屏幕录制）  
adb reboot（重启）  

### adb 命令的来源
> /system/core/toolbox和/framework/base/cmds就是所有adb和shell命令的来源

### 模拟器安装和配置
> virtualbox的安装
genymotion的安装
启动报错的解决方法(http://blog.csdn.net/LKL9413/article/details/47447087)

## 第三章
本章主要讲述了Android控件架构，View的测量和绘制，ViewGroup的测量和绘制，自定义控件的方式，事件的拦截机制

### Andorid控件架构
- view视图树，没有视图树的顶部都有一个ViewParent对象，是整棵树的控制核心，所有的交互管理事件都是由它来统一调度和分配的  
- 每个Activity都有一个Window对象PhoneWindow  
- 所有View的监听事件都是通过WindowManagerService来接收的，并通过Activity做相应的回调  
- 通过requestWindowFeature设置Window的特性时，该方法的调用必须在setContentView方法之前  

### View的测量
- MeasureSpec的作用  
- 测量模式:EXACTLY（精确模式），AT_MOST（最大值模式）UNSPECIFIED（不确定模式）
- 如果自定义的View支持wrap_content属性，那么就必须重写onMeasure方法来指定wrap_content时的大小，这也是重写onMeasure方法的目的  
**参考Heroes的3.2**

### View的绘制
- 如何用Canvas在onDraw方法中绘制
- Canvas canvas = new Canvas(bitmap)；bitmap和canvas是紧密联系的，这个过程称之为装载画布

### ViewGroup的测量
- ViewGroup管理子View
- ViewGroup管理子View显示的大小
- ViewGroup宽高是wrap_content时通过子View的大小来确定自生大小  
**自定义ViewGroup** 通常要重写onLayout来控制子View的显示位置

### ViewGroup的绘制
- 通常不需要绘制
- 如果不指定background不会调用ViewGroup的onDraw方法
- 通过dispatchDraw方法来绘制子View

### 自定义View
自定义View就是设计图形，只有站在设计者的角度才能更好的创建自定义View

**自定义View的重要回调方法**  
- onFinishInflate()： 从XML加载组件后回调
- onSizeChanged(): 组件大小改变的时候回调
- onMeasure(): 回调该方法用来测量View
- onLayout(): 只有ViewGroup才有，用来确定子View显示的位置
- onTouchEvent(): 监听触摸事件时的回调

**通常的三种自定义控件方法**  
- 对现有的控件扩展（参考Heroes的3.6.1）
- 通过组合来实现新的控件（参考Heroes的3.6.2）
- 重写View来实现新的控件（参考Heroes的3.6.3）
