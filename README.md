# OverDraw
过渡渲染检测
Overdraw(过度绘制)描述的是屏幕上的某个像素在同一帧的时间内被绘制了多次。在多层次重叠的UI结构里面，如果不可见的UI也在做绘制的操作，会导致某些像素区域被绘制了多次。这样就会浪费大量的CPU以及GPU资源。
我们可以通过手机设置里面的开发者选项，打开Show GPU Overdraw的选项，可以观察UI上的Overdraw情况。

<img src="screenshots/708649-c4892afea64dbc6c.png" width = "350" />


