package com.kunlun.breadtrip.main;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.kunlun.breadtrip.R;
import com.kunlun.breadtrip.base.BaseActivity;
import com.kunlun.breadtrip.cityhunter.CityHunterFragment;
import com.kunlun.breadtrip.design.DesignFragment;
import com.kunlun.breadtrip.personal.PersonalFragment;
import com.kunlun.breadtrip.recommend.RecommendFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private RadioButton recommendRb;
    private RadioButton hunterRb;
    private RadioButton designRb;
    private RadioButton personalRb;

    private RecommendFragment recommendFragment;
    private CityHunterFragment hunterFragment;
    private DesignFragment designFragment;
    private PersonalFragment personalFragment;
    private FragmentManager manager;
    private FragmentTransaction trans;

    @Override
    protected int getLayout() {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.replace_view, new RecommendFragment()).commit();
        return R.layout.activity_main;
    }

    //初始化组件
    @Override
    protected void initView() {
        recommendRb = bindView(R.id.recommend_rb);
        hunterRb = bindView(R.id.cityhunter_rb);
        designRb = bindView(R.id.design_rb);
        personalRb = bindView(R.id.personal_rb);

    }

    @Override
    protected void initData() {
        recommendRb.setOnClickListener(this);
        hunterRb.setOnClickListener(this);
        designRb.setOnClickListener(this);
        personalRb.setOnClickListener(this);

        recommendFragment = new RecommendFragment();
        hunterFragment = new CityHunterFragment();
        designFragment = new DesignFragment();
        personalFragment = new PersonalFragment();


    }


    @Override
    public void onClick(View v) {
        manager = getSupportFragmentManager();
        trans = manager.beginTransaction();


        switch (v.getId()) {
            case R.id.recommend_rb:
                trans.replace(R.id.replace_view, recommendFragment);
                break;
            case R.id.cityhunter_rb:
                trans.replace(R.id.replace_view, hunterFragment);
                break;
            case R.id.design_rb:
                trans.replace(R.id.replace_view, designFragment);
                break;
            case R.id.personal_rb:
                trans.replace(R.id.replace_view, personalFragment);
                break;
        }
        trans.commit();
    }


}
