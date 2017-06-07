package linchange.example.com.supergreattest.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.utils.LogUtils;
import linchange.example.com.supergreattest.utils.StaticClass;
import linchange.example.com.supergreattest.view.DispatchLinearLayout;

/**
 * Created by Lin Change on 2017-02-25.
 */
//监听短信服务
public class SmsService extends Service implements View.OnClickListener {
    private static final String TAG = "SmsService";

    private SmsReceiver smsReceiver; //短信广播
    private HomeWatchReceiver mHomeWatchReceiver; //监听Home键的广播

    private WindowManager windowManager; //窗口管理器

    private WindowManager.LayoutParams layoutParams; //窗口布局参数
    private DispatchLinearLayout mView; //自定义View

    private String smsPhone; //发件人号码
    private String smsContent; //短信内容

    private TextView tv_phone; //发件人号码显示组件
    private TextView tv_content; //短信内容显示组件
    private Button btn_send_sms; //发送短信按钮

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        init(); //初始化服务
    }

    //初始化服务
    private void init() {
        LogUtils.i(TAG, "init service");

        //动态注册短信广播
        smsReceiver = new SmsReceiver(); //新建广播
        IntentFilter intent = new IntentFilter(); //新建目的过滤器
        intent.addAction(StaticClass.SMS_ACTION); //添加动作
        intent.setPriority(Integer.MAX_VALUE); //设置权限

        registerReceiver(smsReceiver, intent); //注册短信广播

        //动态注册Home键广播
        mHomeWatchReceiver = new HomeWatchReceiver(); //新建广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); //新建目的过滤器
        registerReceiver(mHomeWatchReceiver, intentFilter); //注册Home键广播
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, "stop service");

        unregisterReceiver(smsReceiver); //取消注册短信广播
        unregisterReceiver(mHomeWatchReceiver); //取消注册Home键广播
    }


    //接收短信广播的内部类
    public class SmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction(); //获取当前动作

            if (StaticClass.SMS_ACTION.equals(action)) { //如果当前动作等于短信到达
                LogUtils.i(TAG, "来短信了");

                //获取短信内容，返回的是一个object数组
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                //遍历数组得到相关数据
                for (Object obj: objs) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj); //把数组元素转换成短信对象
                    smsPhone = sms.getOriginatingAddress(); //发件人
                    smsContent = sms.getMessageBody(); //短信内容
                    LogUtils.i(TAG, "短信的内容：" + smsPhone + ":" + smsContent);

                    showWindow(); //弹出短信弹窗
                }
            }
        }
    }

    //弹出短信弹窗
    private void showWindow() {
        //获取系统服务
        windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);

        //新建布局参数
        layoutParams = new WindowManager.LayoutParams();
        //定义布局宽高
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定于布局标记（屏幕常亮，外部可触摸）
        layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式（半透明）
        layoutParams.format = PixelFormat.TRANSLUCENT;
        //定义类型
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;

        //加载布局
        mView = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.sms_item, null);

        //绑定视图
        tv_phone = (TextView) mView.findViewById(R.id.tv_phone);
        tv_content = (TextView) mView.findViewById(R.id.tv_content);
        btn_send_sms = (Button) mView.findViewById(R.id.btn_send_sms);

        //设置点击事件
        btn_send_sms.setOnClickListener(this);

        //设置文字
        tv_phone.setText(getString(R.string.text_email_sender) + smsPhone); //发件人号码
        tv_content.setText(smsContent); //短信内容

        //添加View到布局管理器
        windowManager.addView(mView, layoutParams);

        //给短信弹窗设置键盘点击分发事件监听器
        mView.setDispatchKeyEventListener(mDispatchKeyEventListener);
    }

    //键盘点击分发事件监听器
    private DispatchLinearLayout.DispatchKeyEventListener mDispatchKeyEventListener
            = new DispatchLinearLayout.DispatchKeyEventListener() {
        //如果返回值为true交给service处理，为false则此事件自己处理
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            //判断是否按下返回键
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                LogUtils.i(TAG, "已按下返回键");
                if (mView.getParent() != null) { //如果父级图层不为空（预防空指针）
                    windowManager.removeView(mView); //移除mView弹窗（自定义短信弹窗消失）
                }
            }
            return false;
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_sms: //点击发送短信按钮
                sendSms(); //回复短信
                //设置窗口消失
                if (mView.getParent() != null) { //如果父级图层不为空（预防空指针）
                    windowManager.removeView(mView); //移除mView弹窗（自定义短信弹窗消失）
                }
                break;
        }
    }

    //回复短信
    private void sendSms() {
        Uri uri = Uri.parse("smsto:" + smsPhone); //设置发送的目标号码
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);  //新建意图
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //设置意图（Activity新实例）
        intent.putExtra("sms_body", ""); //存放数据
        startActivity(intent); //启动intent的活动
    }

    public static final String SYSTEM_DIALOGS_REASON_KEY = "reason"; //提示框key
    public static final String SYSTEM_DIALOGS_HOME_KEY = "homekey"; //Home键key

    //监听Home键的广播
    class HomeWatchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction(); //获取动作
            //如果当前动作等于关闭系统弹窗
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOGS_REASON_KEY); //获取intent中携带的字符串

                //设置窗口消失
                if (SYSTEM_DIALOGS_HOME_KEY.equals(reason)) { //如果等于intent中携带的字符串等于Home键的key
                    LogUtils.i(TAG, "我点击了Home键");
                    if (mView.getParent() != null) { //如果父级图层不为空（预防空指针）
                        windowManager.removeView(mView); //移除mView弹窗（自定义短信弹窗消失）
                    }
                }
            }
        }
    }
}
