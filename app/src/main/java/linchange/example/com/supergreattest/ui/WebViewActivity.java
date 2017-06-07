package linchange.example.com.supergreattest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.utils.LogUtils;

//新闻详情界面
public class WebViewActivity extends BaseActivity {
    private static final String TAG = "WebViewActivity";

    private ProgressBar mProgressBar; //圆形进度条
    private WebView mWebView; //网页显示窗口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView(); //初始化View
    }

    //初始化View
    private void initView() {
        //绑定视图
        mWebView = (WebView) findViewById(R.id.mWebView);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);

        Intent intent = getIntent(); //获取intent
        String title = intent.getStringExtra("title"); //获取文章标题
        final String url = intent.getStringExtra("url"); //获取文章详情网址

        LogUtils.i(TAG, "url=" + url);

        getSupportActionBar().setTitle(title); //在标题栏设置文章标题

        mWebView.getSettings().setJavaScriptEnabled(true); //开启JavaScript的支持
        //开启缩放支持
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        mWebView.setWebChromeClient(new WebViewClient()); //设置网页控制器
        mWebView.loadUrl(url); //加载网页

        //设置在本页面显示网页（而不是开启其他的浏览器）
        mWebView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true; //mWebView接受这个事件
            }
        });
    }

    //网页控制器
    public class WebViewClient extends WebChromeClient {
        //进度变化的监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) { //如果当前网页加载进度达到百分百
                mProgressBar.setVisibility(View.GONE); //进度条消失
            }
            super.onProgressChanged(view, newProgress);
        }
    }

}
