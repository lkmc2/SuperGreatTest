package linchange.example.com.supergreattest.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;

import linchange.example.com.supergreattest.R;



//生成二维码界面
public class QrCodeActivity extends BaseActivity {

    private ImageView iv_qr_code; //二维码显示组件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        initView(); //初始化View
    }

    //初始化View
    private void initView() {
        //绑定视图
        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);

        int width = getResources().getDisplayMetrics().widthPixels; //获取屏幕的宽

        //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode(getString(R.string.i_am_super_butler), width * 2 / 3, width * 2 / 3,
                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_boy));

        iv_qr_code.setImageBitmap(qrCodeBitmap); //将生成的二维码设置到二维码显示组件上
    }
}
