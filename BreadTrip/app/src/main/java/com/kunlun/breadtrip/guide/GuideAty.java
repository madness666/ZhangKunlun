package com.kunlun.breadtrip.guide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kunlun.breadtrip.R;
import com.kunlun.breadtrip.base.BaseActivity;
import com.kunlun.breadtrip.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dllo on 16/5/11.
 */
public class GuideAty extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    //定义ViewPager对象
    private ViewPager viewPager;
    //定义适配器
    private GuideViewPagerAdapter viewPagerAdapter;

    private List<View> views;

    //引导图片资源
    private static final int[] pics = {R.mipmap.user_guide_01_bg, R.mipmap.user_guide_02_bg,
            R.mipmap.user_guide_03_bg, R.mipmap.user_guide_04_bg, R.mipmap.user_guide_05_bg};

    //底部图片
    private ImageView[] points;
    //记录当前选中位置
    private int currentIndex;

    private ImageView guideIv;
    private ImageButton enterBtn;

    private static final String SHAREDPREFERENCES_NAME = "first_pref";


    @Override
    protected int getLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initView() {
        views = new ArrayList<>();

        enterBtn = bindView(R.id.guide_image_btn);
        viewPager = bindView(R.id.guide_vp);
        viewPagerAdapter = new GuideViewPagerAdapter(views);


    }

    @Override
    protected void initData() {
        //定义一个布局设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        //初始化引导图片
        for (int i = 0; i < pics.length; i++) {
            guideIv = new ImageView(this);
            guideIv.setLayoutParams(mParams);
            guideIv.setImageResource(pics[i]);
            views.add(guideIv);
        }

        viewPager.setAdapter(viewPagerAdapter);
        //设置监听
        viewPager.addOnPageChangeListener(this);

        // 初始化底部小点
        initPoint();


    }


    private void initPoint() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.guide_linearLayout);

        points = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            points[i] = (ImageView) linearLayout.getChildAt(i);
            // 默认都设为灰色
            points[i].setEnabled(true);
            // 给每个小点设置监听
            points[i].setOnClickListener(this);
            // 设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }

        // 设置当面默认的位置
        currentIndex = 0;
        // 设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                enterBtn.setVisibility(View.GONE);
                break;
            case 4:
                enterBtn.setVisibility(View.VISIBLE);
                enterBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setGuide();

                        Intent intent = new Intent(GuideAty.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                break;
        }

    }

    private void setGuide() {
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // 存入数据
        editor.putBoolean("isFirstIn", false);
        // 提交修改
        editor.commit();


    }

    @Override
    public void onPageSelected(int position) {
        // 设置底部小点选中状态
        setCurDot(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 通过点击事件来切换当前的页面
     */
    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);

    }


    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position) {
        // 排除异常情况
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        // 排除异常情况
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }
        points[positon].setEnabled(false);
        points[currentIndex].setEnabled(true);

        currentIndex = positon;
    }


}
