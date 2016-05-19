package com.kunlun.breadtrip.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kunlun.breadtrip.base.MyApplication;


/**
 * Created by dllo on 16/5/16.
 */
public class VolleySingle {
    private static Context mContext;
    private RequestQueue queue;
    private static VolleySingle ourInstance = new VolleySingle();

    //获取单例的对象
    public static VolleySingle getInstance() {
        if (ourInstance == null) {
            synchronized (VolleySingle.class) {
                if (ourInstance == null) {
                    ourInstance = new VolleySingle();
                }
            }
        }

        return ourInstance;
    }

    private VolleySingle() {
        //获取Application里面的context
        mContext = MyApplication.context;
        queue = getQueue();//初始化我的请求队列
    }

    //提供一个方法新建请求队列
    public RequestQueue getQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(mContext);
        }
        return queue;
    }

    public static final String TAG = "VolleySingleton";

    //添加请求
    public <T> void _addRequest(Request<T> request) {
        request.setTag(TAG);//为我的请求添加标签,方便管理
        queue.add(request);//将请求添加到队列当中
    }

    public <T> void _addRequest(Request<T> request, Object tag) {
        request.setTag(tag);
        queue.add(request);
    }

    public void _addRequest(String url,
                            //这里为我的成功时候回调加上String类型的泛型
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        //创建StringRequest 三个参数分别是
        // url接口网址
        //成功时候的回调,失败时候的回调
        //将请求加入到队列
        StringRequest stringRequest = new StringRequest(url, listener, errorListener);
        _addRequest(stringRequest);
    }

    public <T> void _addRequest(String url,
                                Response.ErrorListener errorListener,
                                Response.Listener<T> listener, Class<T> mClass) {

        GsonRequest gsonRequest = new GsonRequest(Request.Method.GET,
                url, errorListener, listener, mClass);
        _addRequest(gsonRequest);

    }

    public void removeRequest(Object tag) {
        queue.cancelAll(tag);// 根据不同的tag移除出队列
    }


    public static void addRequest(String url,
                                  Response.Listener<String> listener,
                                  Response.ErrorListener errorListener) {
        getInstance()._addRequest(url, listener, errorListener);

    }

    public static <T> void addRequest(String url,
                                      Response.ErrorListener errorListener,
                                      Response.Listener<T> listener,
                                      Class<T> mClass) {
        getInstance()._addRequest(url, errorListener, listener, mClass);
    }
}
