package linchange.example.com.supergreattest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import linchange.example.com.supergreattest.fragment.ButlerFragment;
import linchange.example.com.supergreattest.fragment.GirlFragment;
import linchange.example.com.supergreattest.fragment.UserFragment;
import linchange.example.com.supergreattest.fragment.WechatFragment;
import linchange.example.com.supergreattest.ui.SettingActivity;
import linchange.example.com.supergreattest.utils.LogUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    //mTabLayout
    private TabLayout mTabLayout;
    //ViewPage
    private ViewPager mViewPage;
    //ViewPage的标题
    private List<String> mTitle;
    //Fragment页面（数据源）
    private List<Fragment> mFragment;
    //悬浮按钮
    private FloatingActionButton fab_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //去掉Actionbar和TabLayout之间的阴影
        getSupportActionBar().setElevation(0);

        initData(); //初始化数据
        initView(); //初始化View

//        CrashReport.testJavaCrash(); //Bugly测试
    }

    //初始化数据
    private void initData() {
        //初始化标题内容
        mTitle = new ArrayList<>();
        mTitle.add(getString(R.string.service_butler));
        mTitle.add(getString(R.string.wechat_jingxuan));
        mTitle.add(getString(R.string.girl_center));
        mTitle.add(getString(R.string.user_center));

        //初始化四个Fragment页面
        mFragment = new ArrayList<>();
        mFragment.add(new ButlerFragment());
        mFragment.add(new WechatFragment());
        mFragment.add(new GirlFragment());
        mFragment.add(new UserFragment());
    }

    //初始化View
    private void initView() {
        //为组件找到相应的ID
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPage = (ViewPager) findViewById(R.id.mViewPage);

        //为浮动按钮添加点击事件
        fab_setting.setOnClickListener(this);
        //默认隐藏浮动按钮
        fab_setting.setVisibility(View.GONE);

        //预加载页面
        mViewPage.setOffscreenPageLimit(mFragment.size());

        //添加页面滑动监听器
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.i(TAG, "onPageSelected: position:" + position);
                if (position == 0) {
                    fab_setting.setVisibility(View.GONE);
                } else {
                    fab_setting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置适配器
        mViewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });

        //TabLayout绑定ViewPager
        mTabLayout.setupWithViewPager(mViewPage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_setting:
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                break;
        }
    }

}
