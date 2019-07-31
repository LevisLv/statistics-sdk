## Statistics SDK(统计 SDK)
![](https://jitpack.io/v/LevisLv/statistics-sdk.svg)

* 该 SDK 需要配合 [Android 全埋点插件](https://github.com/LevisLv/statistics-gradle-plugin) 使用，且两者版本号需一致。
* 内部使用 [Countly SDK](https://github.com/Countly/countly-sdk-android) 实现，所以版本号与之一致，你也可改用其他方式。
* 可 build [DEMO](https://github.com/LevisLv/statistics)，查看 app 模块的 build/intermediates/transforms/StatisticsTransform 目录

## 一、[说明](https://github.com/LevisLv/statistics-gradle-plugin#%E4%B8%80%E8%AF%B4%E6%98%8E)

## 二、配置
### 1、添加 maven 地址及 classpath（build.gradle in project）
```groovy
buildscript {
    repositories {
        ······
        maven { url 'https://www.jitpack.io' }
    }

    dependencies {
        ······
        // 要求gradle插件版本最低3.1.0
        classpath 'com.github.LevisLv:statistics-gradle-plugin:19.02.3'
    }
}
```

### 2、引用插件并添加依赖（build.gradle in every module）
<font color='red'>所有的 Android Application 和 Android Library 模块都需要加如下配置</font>

```groovy
······
apply plugin: 'com.levislv.statistics'

statistics {
    enableCompileLog false // 是否打印编译日志（默认false）
    enableHeatMap true // 是否开启热力图功能（默认true）
    enableViewOnTouch false // 是否允许view的onTouch回调全埋点（默认false）
}

android {
    ······
    // 全埋点插件需要 Java 8
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    ······
    implementation 'com.github.LevisLv:statistics-sdk:19.02.3'
}
```

## 三、[使用说明（必须遵循使用规则）](https://github.com/LevisLv/statistics-gradle-plugin#%E4%B8%89%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E5%BF%85%E9%A1%BB%E9%81%B5%E5%BE%AA%E4%BD%BF%E7%94%A8%E8%A7%84%E5%88%99)
