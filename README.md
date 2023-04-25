[![](https://jitpack.io/v/angelmmg90/AutoScrollContent.svg)](https://jitpack.io/#angelmmg90/AutoScrollContent) 
[![](https://jitpack.io/v/angelmmg90/AutoScrollContent/month.svg)](https://jitpack.io/#angelmmg90/AutoScrollContent)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AutoScrollContent-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/8364)

# AutoScrollContent ✅

<img  align="right" src="https://user-images.githubusercontent.com/24268167/150117040-a3657dfd-d637-4243-9321-35d0fbd6ba13.gif" width="350">

* **Loop mode**
* **Reverse content when loop mode is enabled**
* **Horizontal/Vertical display of content when loop mode is enabled**
* **It is possible to manually slide when swiping automatically**
* **Custom speed**
 
# Installation :hammer_and_wrench:

### Step 1. Add the JitPack repository to your build file 

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

### Step 2. Add the dependency
  
```groovy
dependencies {
	implementation 'com.github.angelmmg90:AutoScrollContent:0.1.0'
}
```

# Usage :feet:

### Step 1. Put this on layout file

```xml
<com.teseo.studios.autoscrollcontent.AutoScrollContent
   android:id="@+id/rv_auto_scroll_content"
   android:layout_width="match_parent"
   android:layout_height="wrap_content"/>
```

### Step 2. Add this code to your activity or else...
  
```
You must configure your recycler view
```
  
#### setLoop
```kotlin
binding.rvAutoScrollContent.setLoopEnabled(true)
```

#### setSpeed and setReverse
```kotlin
binding.rvAutoScrollContent.openAutoScroll(speed = 40, reverse = false)
```
#### setCanTouch
It is possible to manually slide when swiping automatically
```kotlin
binding.rvAutoScrollContent.setCanTouch(true)
```


 
 
