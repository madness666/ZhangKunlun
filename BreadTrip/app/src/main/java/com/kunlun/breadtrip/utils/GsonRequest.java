package com.kunlun.breadtrip.utils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;


/**
 * Created by dllo on 16/5/11.
 */
public class GsonRequest<T> extends Request<T> {
    Response.Listener<T> mListener;
    Gson mGson;
    Class<T> mClass;


    // 网络回应
    @Override
    //返回我们想要解析后的数据,在这里首先将response转化成
    // String类型的数据,
    // 然后通过Gson进行解析
    //转化
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            //成功之后,第一个参数我们将data直接解析,
            //第二个参数就是我们的缓存入口,这里的缓存是规定好的
            //我们直接将response请求作为缓存入口
            return Response.success(mGson.fromJson(data, mClass), HttpHeaderParser
                    .parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    // 对请求的一个反馈,反馈的就是我的上面定义好的接口对象
    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    // 构造方法中 我们传入的参数第一个 请求数据类型 ,第二个参数URL
    //第三个参数失败时候的回调,第四个成功时的回调.//第五个实体类
    public GsonRequest(int method, String url, Response.ErrorListener listener, Response.Listener<T> mListener, Class<T> mClass) {
        super(method, url, listener);
        this.mListener = mListener;
        this.mClass = mClass;
        this.mGson = new Gson();
    }


}
