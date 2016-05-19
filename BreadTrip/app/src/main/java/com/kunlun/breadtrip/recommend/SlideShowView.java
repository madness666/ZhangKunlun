package com.kunlun.breadtrip.recommend;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.kunlun.breadtrip.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * ViewPager实现的轮播图广告自定义视图;
 * 既支持自动轮播页面也支持手势滑动切换页面
 */

/**
 * Created by dllo on 16/5/9.
 */
public class SlideShowView extends FrameLayout {
    //使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader-1.8.6-with-sources.jar

    //自动轮播启用开关
    private final static boolean isAutoPlay = true;


    //自定义轮播图的资源
    private String[] imageUrls;

    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;

    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;


    //设置当前viepager当前页数
    private int currentItem = 0;

    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;

    private BannerAdapter bannerAdapter;


    //  这里我定义了一个Handler来处理ViewPager的轮播
    // 最后看看接收message的handler:
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }
    };

    public SlideShowView(Context context) {
        this(context, null);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        initImageLoader(context);

        initData();
        if (isAutoPlay) {
            startPlay();
        }
    }


    /**
     * 开始轮播切换
     */
    // startPlay方法创建了单线程化的线程池，并延时发送message来不断切换ViewPager的item
    private void startPlay() {

        //  scheduleAtFixedRate(TimerTask task,long delay,long period)
        //task--这是被调度的任务。
        //delay--这是以毫秒为单位的延迟之前的任务执行。
        //period--这是在连续执行任务之间的毫秒的时间。
        //创建只有一条线程的线程池,他可以在指定延迟后执行线程任务
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 2, 6, TimeUnit.SECONDS);


    }


    /**
     * 停止轮播图切换
     */
    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    private void initData() {

        imageViewsList = new ArrayList<>();
        dotViewsList = new ArrayList<>();
        bannerAdapter = new BannerAdapter();
        //异步任务获取图片
        new GetListTask().execute("");
    }


    //  初始化Views等UI
    private void initUI(final Context context) {
        if (imageUrls == null || imageUrls.length == 0)
            return;

        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);

        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();


        for (int i = 0; i < imageUrls.length; i++) {
            ImageView view = new ImageView(context);
            //加Tag区分
            view.setTag(imageUrls[i]);
            view.setScaleType(ScaleType.FIT_XY);
            imageViewsList.add(view);

            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }


        viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.setFocusable(true);
        bannerAdapter.setImageViewsList(imageViewsList);
        viewPager.setAdapter(bannerAdapter);
        viewPager.setOnPageChangeListener(new MyPageChangeListener());

    }


    private class MyPageChangeListener implements OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:// 空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int pos) {

            currentItem = pos;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos) {
                    ((View) dotViewsList.get(pos)).setBackgroundResource(R.mipmap.dot_focus);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.mipmap.dot_blur);
                }
            }
        }

    }


    /**
     * 执行轮播图切换任务
     */

    // SlideShowTask中不断的发送message：
    private class SlideShowTask implements Runnable {
        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }


    /**
     * 异步任务,获取数据
     */
    class GetListTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {

                imageUrls = new String[]{

                        "http://photos.breadtrip.com/covers_2016_05_10_29ac1e87c0d0d9f1771bb21ec2fcf563.png?imageView/2/w/960/",
                        "http://photos.breadtrip.com/covers_2016_05_16_8b3589210c867d0ad78c7ca0cf7c3124.png?imageView/2/w/960/",
                        "http://photos.breadtrip.com/covers_2016_05_12_97f32cfddef532a8866144cc7bdaafda.png?imageView/2/w/960/",
                        "http://photos.breadtrip.com/covers_2016_05_13_334cc457635619bb8513deeeb229a38e.png?imageView/2/w/960/",
                        "http://photos.breadtrip.com/covers_2016_04_17_13b4e370794446258aff31bf8747b65b.png",
                        "http://photos.breadtrip.com/covers_2016_04_16_6c5f70ca4f16e8e1b37a5c84c41c30bb.png?imageView/2/w/960"
                };
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                initUI(context);
            }
        }
    }


    /**
     * 图片组件初始化
     */
    private static void initImageLoader(Context context) {

        //  ImageLoader 具体下载图片，缓存图片，显示图片的具体执行类，
        //  ImageLoaderConfiguration：图片缓存的全局配置，主要有线程类、缓存大小、磁盘大小、图片下载与解析、日志方面的配置
//线程优先级threadPriority
//下载和显示的工作队列排序  QueueProcessingType.LIFO

        //
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).
                threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().
                discCacheFileNameGenerator(new Md5FileNameGenerator()).
                tasksProcessingOrder(QueueProcessingType.FIFO).writeDebugLogs().build();

        ImageLoader.getInstance().init(config);
    }

}
