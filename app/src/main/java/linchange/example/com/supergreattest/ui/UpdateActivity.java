package linchange.example.com.supergreattest.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;

import java.io.File;

import linchange.example.com.supergreattest.R;

//系统升级界面
public class UpdateActivity extends BaseActivity {
    private static final String TAG = "UpdateActivity";

    public static final int HANDLE_LODING  = 10001; //正在下载
    public static final int HANDLE_OK  = 10002; //下载完成
    public static final int HANDLE_FAILURE  = 10003; //下载失败

    private TextView tv_size; //当前已下载控件
    private NumberProgressBar number_progress_bar; //数字进度条

    private String url; //新版本app下载网址
    private String path; //新版本app下载物理路径

    //界面控制器
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) { //根据传入的消息进行判断
                case HANDLE_LODING: //正在下载
                    //实时更新进度
                    Bundle bundle = msg.getData(); //获取包裹
                    long transferredBytes = bundle.getLong("transferredBytes"); //获取当前进度
                    long totalSize = bundle.getLong("totalSize"); //获取总进度
                    tv_size.setText(transferredBytes + "/" + totalSize); //在当前已下载控件设置进度

                    //设置进度条进度
                    number_progress_bar.setProgress((int) ((float)transferredBytes / (float) totalSize * 100));
                    break;
                case HANDLE_OK: //下载完成
                    tv_size.setText(R.string.download_success); //在当前已下载控件设置成功提示
                    startInstallApk(); //启动安装新版本应用
                    break;
                case HANDLE_FAILURE: //下载失败
                    tv_size.setText(R.string.download_failure); //在当前已下载控件设置失败提示
                    break;
            }
        }
    };

    //启动安装新版本应用
    private void startInstallApk() {
        Intent intent = new Intent(); //新建意图
        intent.setAction(Intent.ACTION_VIEW); //设置动作
        intent.addCategory(Intent.CATEGORY_DEFAULT); //添加属性
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive"); //设置数据和类型

        startActivity(intent); //启动活动
        finish(); //结束当前页面
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initView(); //初始化View
    }

    //初始化View
    private void initView() {
        //绑定视图
        tv_size = (TextView) findViewById(R.id.tv_size);
        number_progress_bar = (NumberProgressBar) findViewById(R.id.number_progress_bar);

        number_progress_bar.setMax(100); //为数字进度条设置最大值

        //初始化下载路径
        path = FileUtils.getSDCardPath() + "/supertest" + System.currentTimeMillis() + ".apk";

        url = getIntent().getStringExtra("url"); //设置新版本app下载网址
        if (!TextUtils.isEmpty(url)) {
            //执行网络下载
            RxVolley.download(path, url, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
//                    LogUtils.i(TAG, "transferredBytes:" + transferredBytes + ",totalSize：" + totalSize);
                    Message message = new Message(); //新建消息
                    message.what = HANDLE_LODING; //设置消息等于加载中

                    Bundle bundle = new Bundle(); //新建包裹
                    bundle.putLong("transferredBytes", transferredBytes); //存入当前进度
                    bundle.putLong("totalSize", totalSize); //存入总进度

                    message.setData(bundle); //将包裹存入消息
                    handler.sendMessage(message); //发送消息
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
//                    LogUtils.i(TAG, "成功");
                    handler.sendEmptyMessage(HANDLE_OK); //发送成功空消息
                }

                @Override
                public void onFailure(VolleyError error) {
//                    LogUtils.i(TAG, "失败");
                    handler.sendEmptyMessage(HANDLE_FAILURE); //发送失败空消息
                }
            });
        }
    }
}
