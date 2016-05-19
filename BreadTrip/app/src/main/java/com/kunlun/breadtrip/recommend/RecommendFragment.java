package com.kunlun.breadtrip.recommend;


import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kunlun.breadtrip.R;
import com.kunlun.breadtrip.base.BaseFragment;
import com.kunlun.breadtrip.bean.RecommendBean;
import com.kunlun.breadtrip.utils.VolleySingle;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dllo on 16/5/10.
 */
public class RecommendFragment extends BaseFragment {
    private ListView listView;
    private ListAdapter listAdapter;
    private List<RecommendBean> beanList;//设置数据的集合
    private int i = 2;


    private PullToRefreshListView mPullToRefreshListView;


    @Override
    public int initLayout() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void initView(View view) {
        mPullToRefreshListView = bindView(R.id.recommend_lv);


    }

    @Override
    public void initData() {

        listAdapter = new ListAdapter(context);
        listView = mPullToRefreshListView.getRefreshableView();
        //给ListView加载头布局
        LinearLayout headViewLayout = (LinearLayout) LayoutInflater.
                from(context).inflate(R.layout.head_view, null);

        listView.addHeaderView(headViewLayout);


        String str = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(), DateUtils.
                FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

        ILoadingLayout startLabels = mPullToRefreshListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setRefreshingLabel("正在刷新");
        startLabels.setReleaseLabel("释放开始刷新");
        startLabels.setLastUpdatedLabel("最后更新时间:" + str);
        ILoadingLayout startLabelsNext = mPullToRefreshListView
                .getLoadingLayoutProxy(false, true);
        startLabelsNext.setRefreshingLabel("正在加载");
        startLabelsNext.setPullLabel("上拉加载更多");
        beanList = new ArrayList<>();
        VolleySingle.addRequest("http://chanyouji.com/api/trips/featured.json?page=",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<RecommendBean>>() {
                        }.getType();
                        beanList = gson.fromJson(response, type);
                        listAdapter.setRecommendBeen(beanList);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        mPullToRefreshListView.setAdapter(listAdapter);
        //设置模式上拉加载,下拉刷新
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            // 下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(), DateUtils.
                        FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                ILoadingLayout startLabels = mPullToRefreshListView
                        .getLoadingLayoutProxy(true, false);
                startLabels.setLastUpdatedLabel("最后更新时间:" + str);
                Log.d("Sysout", "PullDown ");
                VolleySingle.addRequest("http://chanyouji.com/api/trips/featured.json?page",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Sysout", "pulldownnext");
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<RecommendBean>>() {
                                }.getType();
                                beanList = gson.fromJson(response, type);
                                listAdapter.setRecommendBeen(beanList);
                                listAdapter.notifyDataSetChanged();
                                Log.d("Sysout", "down");
                                mPullToRefreshListView.onRefreshComplete();
                                //重新定义i值
                                i = 2;
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

            }

            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.d("Sysout", "Pullup");
                VolleySingle.addRequest("http://chanyouji.com/api/trips/featured.json?page=" + String.valueOf(i),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Sysout", "PullupNext");
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<RecommendBean>>() {
                                }.getType();
                                ArrayList<RecommendBean> beanList1;
                                beanList1 = gson.fromJson(response, type);
                                beanList.addAll(beanList1);
                                listAdapter.setRecommendBeen(beanList);
                                listAdapter.notifyDataSetChanged();
                                i++;
                                Log.d("Sy", "onResponse: " + i);
                                Log.d("Sysout", "up");
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Sysout", "onErrorResponse: " + error);
                            }
                        });
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }
}
