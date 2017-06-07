package linchange.example.com.supergreattest.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.service.SmsService;
import linchange.example.com.supergreattest.utils.LogUtils;
import linchange.example.com.supergreattest.utils.ShareUtils;
import linchange.example.com.supergreattest.utils.StaticClass;


//设置界面
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SettingActivity";

    private Switch sw_speak; //语音播报开关
    private Switch sw_sms; //接收短信开关

    private LinearLayout ll_update; //版本更新选项布局
    private LinearLayout ll_scan; //扫一扫选项布局
    private LinearLayout ll_qr_code; //分享二维码选项布局
    private LinearLayout ll_ll_my_locate; //我的位置选项布局
    private LinearLayout ll_about; //关于软件选项布局

    private TextView tv_version; //版本更新选项的标题
    private TextView tv_scan_result; //扫一扫选项的结果

    private String versionName; //版本名称
    private int versionCode; //版本号
    private String url; //下载新版本app的网址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView(); //初始化View
    }

    //初始化View
    private void initView() {
        //绑定视图
        sw_speak = (Switch) findViewById(R.id.sw_speak);
        sw_sms = (Switch) findViewById(R.id.sw_sms);
        ll_update = (LinearLayout) findViewById(R.id.ll_update);
        ll_scan = (LinearLayout) findViewById(R.id.ll_scan);
        ll_qr_code = (LinearLayout) findViewById(R.id.ll_qr_code);
        ll_ll_my_locate = (LinearLayout) findViewById(R.id.ll_my_locate);
        ll_about = (LinearLayout) findViewById(R.id.ll_about);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_scan_result = (TextView) findViewById(R.id.tv_scan_result);

        //设置点击事件
        sw_speak.setOnClickListener(this);
        sw_sms.setOnClickListener(this);
        ll_update.setOnClickListener(this);
        ll_scan.setOnClickListener(this);
        ll_qr_code.setOnClickListener(this);
        ll_ll_my_locate.setOnClickListener(this);
        ll_about.setOnClickListener(this);

        //获取上一次保存的语音播报开关状态
        boolean isSpeak = ShareUtils.getBoolean(this, "isSpeak", false);
        //设置语音播报开关的状态
        sw_speak.setChecked(isSpeak);

        //获取上一次保存的接收短信开关状态
        boolean isSms = ShareUtils.getBoolean(this, "isSms", false);
        //设置接收短信开关的状态
        sw_sms.setChecked(isSms);

        try {
            getVersionNameCode(); //获取版本名称和版本号/Code
            tv_version.setText(getString(R.string.check_version_point) + versionName); //设置版本选项标题
        } catch (PackageManager.NameNotFoundException e) {
            tv_version.setText(R.string.check_version); //设置版本选项标题
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sw_speak: //点击语音播报开关
                sw_speak.setSelected(!sw_speak.isSelected()); //切换相反的选择值
                ShareUtils.putBoolean(this, "isSpeak", sw_speak.isChecked()); //保存语音播报开关的状态
                break;

            case R.id.sw_sms: //点击接收短信开关
                sw_sms.setSelected(!sw_sms.isSelected()); //切换相反的选择值
                ShareUtils.putBoolean(this, "isSms", sw_sms.isChecked()); //保存接收短信开关的状态

                if (sw_sms.isChecked()) { //接收短信开关被选中
                    startService(new Intent(this, SmsService.class)); //动态开启服务
                } else {
                    stopService(new Intent(this, SmsService.class)); //动态关闭服务
                }
                break;

            case R.id.ll_update: //点击版本更新选项
                LogUtils.i(TAG, "ll_update click");
                /**
                 * 步骤：
                 * 1.请求服务器的配置文件，拿到versionCode
                 * 2.比较服务器版本与当前版本的versionCode
                 * 3.弹出Dialog提示
                 * 4.跳转到更新界面，并把url传递过去
                 */
                //获取网络数据
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        LogUtils.i(TAG, "json=" + t);
                        parsingJson(t); //解析json数据
                    }
                });
                break;

            case R.id.ll_scan: //点击扫一扫选项
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;

            case R.id.ll_qr_code: //分享二维码选项
                startActivity(new Intent(getApplicationContext(), QrCodeActivity.class));
                break;

            case R.id.ll_my_locate: //我的位置选项
                startActivity(new Intent(getApplicationContext(), LocationActivity.class));
                break;

            case R.id.ll_about: //关于软件选项
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
        }
    }

    /**
     * 解析json数据
     * @param t json数据
     */
    private void parsingJson(String t) {
        try {
            t = new String(t.getBytes(), "utf-8"); //文字编码转换

//            LogUtils.i(TAG, "parsingJson json=" + t);

            JSONObject jsonObject = new JSONObject(t); //获取json根对象
            int code = jsonObject.getInt("versionCode"); //获取服务区上的版本号

            if (code > versionCode) { //服务区上的版本号大于当前的版本号
                url = jsonObject.getString("url"); //复制给新版本app下载网址
                showUpdateDialog(jsonObject.getString("content")); //弹出升级提示窗（参数：最新版本更新的内容）
            } else { //无更新的版本
                Toast.makeText(this, R.string.now_is_lastest_version, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 弹出升级提示窗
     * @param content 最新版本更新的内容
     */
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this) //新建弹出的对话框
                .setTitle(R.string.now_have_new_version) //设置提示窗标题
                .setMessage(content) //设置提示内容（最新版本更新的内容）
                .setPositiveButton(R.string.text_update, new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), UpdateActivity.class); //新建意图
                        intent.putExtra("url", url); //存入新版本app下载网址
                        startActivity(intent); //启动新界面
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //什么都不做也会执行dismiss方法
                    }
                }).show();
    }

    //获取版本名称和版本号/Code
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager packageManager = getPackageManager(); //获取包管理器
        PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0); //获取包信息
        versionName = info.versionName; //设置版本名称
        versionCode = info.versionCode; //设置版本号
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            tv_scan_result.setText(scanResult);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult)); //新建目的
            startActivity(intent); //启动浏览器
        }
    }
}
