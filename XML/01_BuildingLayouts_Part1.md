# 一些基本的控件
- TextView
- ImageView
- Button

# XML介绍
例子：
```xml
<TextView		//格式的开始
	android:text = "Happy Birthday"		//显示的文本
	android:background = "@android:color/darker_gray"	//灰色
	android:layout_width = "150dp"		//长宽的大小
	android:layout_height = "75dp" />		//格式结束
```
## XML语法
TextView的开始示例：
```xml
<TextView
	..... />
//或者
<TextView
	..... >
	<TextView
		...... />
	<TextView
		...... />
</TextView>
```

[测试页面](https://labs.udacity.com/android-visualizer/#/android/text-view)

## TextView
### TextView自动匹配
使用下面的**wrap_content**属性可以让width、height匹配到text。
```android:layout_width="wrap_content"```
```android:layout_height="wrap_content"```
```xml
<TextView
    android:text="Hello, This is my firt line text.Hello, This is my firt line text.Hello, This is my firt line text.Hello, This is my firt line text.Hello, This is my firt line text.Hello, This is my firt line text."
    android:background="@android:color/darker_gray"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

### TextView设置字体大小
```android:textSize="55sp"```
```android:textAppearance="?android:textAppearanceSmall"```

### TextView设置字体和背景颜色
```android:background="#2196F3"```
```android:textColor="#AED581"```
```android:background="@android:color/darker_gray"```


## ImageView
### 属性
```xml
<ImageView
    android:src="@drawable/cake"		//@表示引用
    android:layout_width="wrap_content"	
    android:layout_height="wrap_content"
    android:scaleType="center"/>	//scaleType表示图片的显示布局，centerCop就可以无边框显示
```

[android开发者网站](developer.android.com/index.html) 