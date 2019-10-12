# ItemTakeTurnHelper
仿小米下载热榜，ViewPager嵌套RecyclerView实现RecyclerView，切换页面时RecyclerView的Item轮流入场。

支持左右轮流进出场。

相关博客：[仿小米下载热榜，RecyclerView item轮流入场](https://www.jianshu.com/p/3f740637189f)

添加依赖：
```gradle
allprojects {
    repositories {
        ......
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    ......
    implementation 'com.github.BigDevilS:ItemTakeTurnHelper:v1.2'
}
```

小米热榜：

 ![image](https://github.com/BigDevilS/ItemTakeTurnHelper/blob/master/previews/xiaomirebang.gif)
 https://github.com/BigDevilS/ItemTakeTurnHelper/blob/master/previews/fang.gif

仿：

 ![image](https://github.com/BigDevilS/ItemTakeTurnHelper/blob/master/previews/fang.gif)
 
使用范例：
```java
    TakeTurnHelper helper = TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position);
    helper.setSupportMode(TakeTurnHelper.Mode.IN);
    helper.setSupportScrollDirection(TakeTurnHelper.ScrollDirection.LEFT);
    helper.setTargetRecyclerView(recyclerView);
```
PagerAdapter中：
```java
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position).onDestroy();
    }
```
页面销毁时：
```
    public void onDestroy() {
      super.onDestroy();
      TakeTurnHelperProvider.onParentDestroy(viewPager);
    }
```
 
Demo下载：[app-debug](https://github.com/BigDevilS/ItemTakeTurnHelper/raw/master/previews/app-debug.apk)

## APIs

TakeTurnHelperProvider

Method|Description
--|--
getHelper(Context context, ViewPager parent, int position)|获取TakeTurnHelper实例
onParentDestroy(ViewPager parent)|页面销毁时调用

TakeTurnHelper

Method|Description
--|--
setParent(ViewPager parent)|设置外部ViewPager
setSupportScrollDirection(ScrollDirection scrollDirection)|设置支持的滑动方向（左侧，右侧）
setSupportMode(Mode mode)|设置支持的模式（入场，出场）
setTargetRecyclerView(RecyclerView targetRecyclerView)|设置内部RecyclerView
onDestroy()|页面销毁/ViewPager回收item时调用
