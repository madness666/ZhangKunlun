package com.kunlun.breadtrip.recommend;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class BannerAdapter extends PagerAdapter {
    private List<ImageView> imageViewsList;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public BannerAdapter() {
    }

    public void setImageViewsList(List<ImageView> imageViewsList) {
        this.imageViewsList = imageViewsList;
        notifyDataSetChanged();
    }

    //删除页卡
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = imageViewsList.get(position);
        container.removeView(v);
    }

    //增加页卡
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imageViewsList.get(position);

        //加载图片
        imageLoader.displayImage(imageView.getTag() + "", imageView);

        ((ViewPager) container).addView(imageViewsList.get(position));
        return imageViewsList.get(position);
    }

    // 获得viewpager的页数
    @Override
    public int getCount() {
        return imageViewsList == null ? 0 : imageViewsList.size();

    }

    //如果视图与Object有关联
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }


    @Override
    public Parcelable saveState() {
        return null;
    }


}