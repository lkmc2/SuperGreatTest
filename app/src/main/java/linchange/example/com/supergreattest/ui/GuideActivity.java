package linchange.example.com.supergreattest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.utils.LogUtils;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GuideActivity";

    //页面滑动器
    private ViewPager mViewPager;
    //存储页面的列表
    private List<View> mList = new ArrayList<>();
    //页面1,2,3
    private View view1;
    private View view2;
    private View view3;
    //小圆点1,2,3
    private ImageView point1;
    private ImageView point2;
    private ImageView point3;
    //点击后跳出GuideActivity的TextView
    private TextView tv_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();//初始化View
    }

    //初始化View
    private void initView() {
        //绑定视图
        mViewPager = (ViewPager) findViewById(R.id.mViewPage);
        tv_pass = (TextView) findViewById(R.id.tv_pass);
        point1 = (ImageView) findViewById(R.id.point1);
        point2 = (ImageView) findViewById(R.id.point2);
        point3 = (ImageView) findViewById(R.id.point3);



        //设置小圆点默认背景图
        setPointImg(true, false, false);

        //生成页面
        view1 = View.inflate(this, R.layout.pager_item_one, null);
        view2 = View.inflate(this, R.layout.pager_item_two, null);
        view3 = View.inflate(this, R.layout.pager_item_three, null);

        //为跳出GuideActivity的TextView设置点击事件（进入主页）
        tv_pass.setOnClickListener(this);

        //为页面3中的按钮btn_start设置点击事件（进入主页）
        view3.findViewById(R.id.btn_start).setOnClickListener(this);

        //将页面加入列表容器
        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        //设置适配器
        mViewPager.setAdapter(new GuideAdapter());

        //监听ViewPager的滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.i(TAG, "onPageSelected:position=" + position);
                switch (position) {
                    case 0:
                        setPointImg(true, false, false);
                        tv_pass.setVisibility(View.VISIBLE); //跳过选项出现
                        break;
                    case 1:
                        setPointImg(false, true, false);
                        tv_pass.setVisibility(View.VISIBLE); //跳过选项出现
                        break;
                    case 2:
                        setPointImg(false, false, true);
                        tv_pass.setVisibility(View.GONE); //跳过选项消失
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
            case R.id.tv_pass:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }
    }

    class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //实例化item
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //将生成的View添加进ViewPager
            ((ViewPager) container).addView(mList.get(position));
            //返回生成的View
            return mList.get(position);
        }

        //销毁item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mList.get(position));
//            super.destroyItem(container, position, object);
        }
    }

    /**
     * 设置三个小圆点的背景图
     * @param isCheck1 第一个圆点是否选中
     * @param isCheck2 第二个圆点是否选中
     * @param isCheck3 第三个圆点是否选中
     */
    private void setPointImg(boolean isCheck1, boolean isCheck2, boolean isCheck3) {
        boolean[] checked = {isCheck1, isCheck2, isCheck3}; //是否选中数组
        ImageView[] points = {point1, point2, point3}; //对应的绑定视图

        //循环设置背景图
        for (int i = 0; i < checked.length; i++) {
            if (checked[i]) {
                points[i].setBackgroundResource(R.drawable.ic_small_point_on);
            } else {
                points[i].setBackgroundResource(R.drawable.ic_small_point_off);
            }
        }
    }
}
