package linchange.example.com.supergreattest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import linchange.example.com.supergreattest.MainActivity;
import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.entity.MyUser;
import linchange.example.com.supergreattest.utils.ShareUtils;
import linchange.example.com.supergreattest.utils.StaticClass;
import linchange.example.com.supergreattest.utils.UtilTools;

//启动页（闪屏页）
public class SplashActivity extends AppCompatActivity {
    /**
     * 1.延时2000ms
     * 2.判断程序是否第一次运行
     * 3.自定义字体
     * 4.Activity全屏主题
     */

    private TextView tv_splash;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否是第一次运行
                    if (isFirst()) {
                        //跳转到引导页
                        startActivity(new Intent(getApplicationContext(), GuideActivity.class));
                        finish(); //结束启动页
                    } else { //不是第一次运行

                        //获取上一次记住密码的选中状态
                        boolean isCheck = ShareUtils.getBoolean(getApplicationContext(), "keeppass", false);

                        if (isCheck) {//如果上一次已选择记住密码
                            String name = ShareUtils.getString(getApplicationContext(), "name", ""); //获取已储存的用户名
                            String password = ShareUtils.getString(getApplicationContext(), "password", ""); ///获取已储存的密码

                            //创建登陆对象
                            final MyUser user = new MyUser();
                            user.setUsername(name); //设置账户名
                            user.setPassword(password); //设置密码

                            //执行登陆操作
                            user.login(new SaveListener<MyUser>() {
                                @Override
                                public void done(MyUser myUser, BmobException e) {
                                    //判断结果
                                    if (e == null) { //未产生异常
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class)); //跳转到主操作界面
                                        finish();//结束启动页
                                    } else { //产生异常
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class)); //跳转到登陆界面
                                        finish();//结束启动页
                                    }
                                }
                            });
                        } else { //上次未选择记住密码
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); //跳转到登陆界面
                            finish();//结束启动页
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
    }

    //初始化View
    private void initView() {
        //延时2000秒发送空消息
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);
        //视图绑定
        tv_splash = (TextView) findViewById(R.id.tv_splash);

        //设置字体
        UtilTools.setFont(getApplicationContext(), tv_splash);

    }

    //判断程序是否是第一次行
    private boolean isFirst() {
        //若是第一次运行SHARE_IS_FIRST关键字不存在，返回默认值true
        boolean isFirst = ShareUtils.getBoolean(getApplicationContext(), StaticClass.SHARE_IS_FIRST, true);
        if (isFirst) {
            //是第一次运行则设置关键字SHARE_IS_FIRST等于false
            ShareUtils.putBoolean(getApplicationContext(), StaticClass.SHARE_IS_FIRST, false);
            return true;
        } else {
            return false;
        }
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
