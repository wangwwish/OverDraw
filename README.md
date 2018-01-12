# OverDraw
过渡渲染检测
Overdraw(过度绘制)描述的是屏幕上的某个像素在同一帧的时间内被绘制了多次。在多层次重叠的UI结构里面，如果不可见的UI也在做绘制的操作，会导致某些像素区域被绘制了多次。这样就会浪费大量的CPU以及GPU资源。
我们可以通过手机设置里面的开发者选项，打开Show GPU Overdraw的选项，可以观察UI上的Overdraw情况。

<img src="screenshots/708649-c4892afea64dbc6c.png" width = "600" />
蓝色，淡绿，淡红，深红代表了4种不同程度的Overdraw情况，我们的目标就是尽量减少红色Overdraw，看到更多的蓝色区域。
产生的主要原因是：

不必要的背景颜色或背景图片
被遮挡的不可见部分

这个小工具不是如何优化过渡渲染。是要把每个页面过渡渲染值取出来，统计汇总。


http://tools.oesf.biz/android-4.4.4_r1.0/xref/frameworks/base/core/java/android/view/HardwareRenderer.java
