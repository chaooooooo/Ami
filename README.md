# Ami
使用方法：
[![](https://jitpack.io/v/chaooooooo/Ami.svg)](https://jitpack.io/#chaooooooo/Ami)

第一步: 在根build.gradle 添加jitpack仓库:
```
   allprojects {
	    repositories {
			...
			maven { url 'https://jitpack.io' }
		  }
	}
```
第二步: 添加Ami依赖
```
   dependencies {
      compile 'com.github.chaooooooo:Ami:v0.0.2'
   }
```

第三步: 在Application中添加：
```
   Ami.init(this ,R.raw.drawer)
```

this 是你的Application
drawer 是一个xml文件，用来定义抽屉的内容，详见工程demo



