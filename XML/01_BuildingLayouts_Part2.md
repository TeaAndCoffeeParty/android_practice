# ViewGroup
包含2中格式
- LinearLayout：线性的布局是单个方向的
- RelativeLayout：这个可以子视图可以依据父视图和子视图来确定相对位置
例子：
```xml
<LinearLayout
    android:orientation="vertical"			//方向是垂直
    android:layout_width="wrap_content"
    android:layout_height="wrap_content">	//宽高自适应
 
    <TextView
        android:text="Guest List"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />	
 
    <TextView
        android:text="Kunal"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
 
</LinearLayout>		//设置了一个ViewGroup
```

## LinearLayout
### 布局朝向
可以设置朝向垂直，或者水平
```android:orientation="vertical"```

### 宽高大小新属性
可以在子视图中设置**mach_parent**属性值，有个这个属性，宽高会和父视图的大小一致。
例子：
```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/darker_gray">

    <TextView
        android:text="VIP List"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="#4CAF50"
        android:textSize="24sp" />

    <TextView
        android:text="Kunal"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="#4CAF50"
        android:textSize="24sp" />

    <TextView
        android:text="Kagure"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="#4CAF50"
        android:textSize="24sp" />

    <TextView
        android:text="Lyla"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="#4CAF50"
        android:textSize="24sp" />

</LinearLayout>
```

### 内容平均分填满整个父视图
使用属性```android:layout_weight```
```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ImageView
        android:src="@drawable/ocean"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:scaleType="centerCrop" />

    <TextView
        android:text="You're invited!"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="0"
        android:textColor="@android:color/white"
        android:textSize="54sp"
        android:background="#009688" />

    <TextView
        android:text="Bonfire at the beach"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="0"
        android:textColor="@android:color/white"
        android:textSize="34sp"
        android:background="#009688" />

</LinearLayout>
```

## RelativeLayout
### 相关于父视图的位置摆放属性
```xml
	android:layout_alignParentTop="true"
	android:layout_alignParentBottom="true"
	android:layout_alignParentLeft="true"
	android:layout_alignParentRight="true"
```
### 与兄弟视图的位置摆放属性
```xml
	android:layout_above="@id/lyla_text_view"
	android:layout_below="@id/lyla_text_view"
	android:layout_toLeftOf="@id/ben_text_view"
	android:layout_toRightOf="@id/lyla_text_view"
```
### 子视图id名字使用
```xml
<TextView
        android:id="@+id/amy_text_view"		//@+id表示设置id属性
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:layout_above="@id/omoju_text_view"	//@id表示引用id
        android:textSize="24sp"
        android:text="Amy" />
```