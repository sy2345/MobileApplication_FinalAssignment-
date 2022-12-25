package com.example.myapplication.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.example.myapplication.service.GetWeatherService;

//创建小组件第一步：创建Provider，扩写原本的Widget方法
//Github_David
public class MyAppWidgetProvider extends AppWidgetProvider {

    /**
     * 当第1个 widget 的实例被创建时触发。
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

    }

    /**
     * 接收到任意广播时触发，并且会在上述的方法之前被调用。
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        /**
         *  如果你的Widget要求并不是很特殊，一般情况下，在此接收广播，即可处理相应事务，但在此就放弃该方法了。
         */
    }

    /**
     * 当 widget 更新时被执行。每次创建Widget，或者刚开机等情况下，都会被执行。
     */
    //创建小组件第二步：更新时创建自定义更新服务Service
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        /**
         *  由于Widget本身不支持执行太过耗时的操作，所以，为了实现开机刷新和每次添加的时候刷新数据，开启服务，
         *  让数据在后台下载，等下载完成之后，在进行更新Widget的操作。
         */
        Intent intent = new Intent(context, GetWeatherService.class);
        intent.putExtra("appWidgetIds", appWidgetIds);
        context.startService(intent);
    }

    /**
     * 当 widget 被删除时被触发。
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        //回收小组件的Service
        super.onDeleted(context, appWidgetIds);
        Intent intent = new Intent(context, GetWeatherService.class);
        context.stopService(intent);
    }

    /**
     * 当最后1个 widget 的实例被删除时触发。
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
