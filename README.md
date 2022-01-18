[![](https://jitpack.io/v/angelmmg90/AutoScrollContent.svg)](https://jitpack.io/#angelmmg90/AutoScrollContent)

# AutoScrollContent ✅

*  **Loop mode**
* **Reverse content when loop mode is enabled**
* **Horizontal/Vertical display of content when loop mode is enabled**
*  **It is possible to manually slide when swiping automatically**
*  **Custom speed**

# Installation

### Step 1. Add the JitPack repository to your build file 

Add it in your root build.gradle at the end of repositories:


	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

  
  ### Step 2. Add the dependency
  
  	dependencies {
	        implementation 'com.github.angelmmg90:AutoScrollContent:Tag'
	  }


# Usage

### Step 1. Put this on layout file

     <com.teseo.studios.autoscrollcontent.AutoScrollContent
        android:id="@+id/rv_auto_scroll_content"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>


  ### Step 2. Add this code to your activity or else...
  
  ```
  You must configure your recycler view
  
 ```
 ## setLoop
 ```
 binding.rvAutoScrollContent.setLoopEnabled(true)
 ```
  ## setSpeed and setReverse
 ```
 binding.rvAutoScrollContent.openAutoScroll(speed = 40, reverse = false)

 ```
  ## setCanTouch
   It is possible to manually slide when swiping automatically
 ```
  binding.rvAutoScrollContent.setCanTouch(canTouch)

 ```


 
 
