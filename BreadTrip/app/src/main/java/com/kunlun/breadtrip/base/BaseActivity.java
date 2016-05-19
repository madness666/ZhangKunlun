package com.kunlun.breadtrip.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;


public abstract class BaseActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(getLayout());
        initView();
        initData();
    }

    /**
     * 这是加载布局的抽象方法
     */
    protected abstract int getLayout();


    /**
     * 这是加载组件的方法
     */
    protected abstract void initView();


    /**
     * 这是加载数据的方法
     */


    /**
     * 这个方法使组件实例化不需要转型
     * <p/>
     * 使用方式:
     * TextView textView = bindView(R.id.tv);
     * 这样使用这个方法的时候是不需要强转的
     */
    protected <T extends View> T bindView(int id) {
        return (T) findViewById(id);
    }


    protected abstract void initData();


}