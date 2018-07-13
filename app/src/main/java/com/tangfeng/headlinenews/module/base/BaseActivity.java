package com.tangfeng.headlinenews.module.base;

import android.app.ActivityManager;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.afollestad.materialdialogs.color.CircleView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.tangfeng.headlinenews.Constant;
import com.tangfeng.headlinenews.R;
import com.tangfeng.headlinenews.util.SettingUtil;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

/**
 * Date :2018/7/10
 * Time :16:51
 * author:moyihen
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected SlidrInterface slidrInterface;
    private int iconType;
    private Context mContext;

    /**
     * 初始化Toolbar
     */
    protected void initToolbar(Toolbar toolbar,boolean homeAsUpEnable,String title){
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnable);
    }

    /**
     * 初始化滑动返回
     */
    protected void initSlidable(){
        int isSlidable = SettingUtil.getInstance().getSlidable();
        if (isSlidable != Constant.SLIDABLE_DISABLE) {
            SlidrConfig config = new SlidrConfig.Builder()
                    .edge(isSlidable == Constant.SLIDABLE_EDGE)
                    .build();
            slidrInterface = Slidr.attach(this, config);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.iconType = SettingUtil.getInstance().getCustomIconValue();
        this.mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int color = SettingUtil.getInstance().getColor();
        int drawable = Constant.ICONS_DRAWABLES[SettingUtil.getInstance().getCustomIconValue()];

        if (getSupportActionBar()!=null)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {   //>=5.0
            getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
            //最近任务栏上色
            ActivityManager.TaskDescription  tDesc = new ActivityManager.TaskDescription( getString(R.string.app_name),
                    BitmapFactory.decodeResource(getResources(), drawable),
                    color);
            setTaskDescription(tDesc);

            if (SettingUtil.getInstance().getNavBar()){
                getWindow().setNavigationBarColor(CircleView.shiftColorDown(color));
            }else {
                getWindow().setNavigationBarColor(Color.BLACK);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //Fragment 逐个出栈
        //获取回退栈fragment个数
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0){
            super.onBackPressed();
        }else {
            getSupportFragmentManager().popBackStack();
        }

    }

    public <X> AutoDisposeConverter<X> bindAutoDispose(){
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY));
    }
}
