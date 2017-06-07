package linchange.example.com.supergreattest.utils;

/**
 * Created by Lin Change on 2017-02-16.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import linchange.example.com.supergreattest.R;

/**
 * 工具类
 */
public class UtilTools {

    /**
     * 设置字体
     * @param mContext 上下文
     * @param textView 字体显示控件
     */
    public static void setFont(Context mContext, TextView textView) {
        Typeface fontType = Typeface.createFromAsset(mContext.getAssets(), "fonts/test.ttf");
        textView.setTypeface(fontType);
    }

    /**
     * 保存头像控件的图片到ShareUtils下
     * @param mContext 上下文
     * @param imageView 圆形头像控件
     */
    public static void putImageToShare(Context mContext, ImageView imageView) {
        //获取头像上的BitmapDrawable图片
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        //BitmapDrawable图片转换成位图
        Bitmap bitmap = drawable.getBitmap();
        //第一步：将Bitmap压缩成字节数组输出流
        ByteArrayOutputStream byStream = new ByteArrayOutputStream(); //新建字节数组输出流
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byStream); //使用位图的压缩方法（参数分别为图片格式，图片质量，字节数组输出流）
        //第二步：利用Base64将字节数组输出流转换成String
        byte[] byteArray = byStream.toByteArray(); //将字节数组输出流变成byte数组
        String imgString = Base64.encodeToString(byteArray, Base64.DEFAULT); //将byte数组变成String
        //第三步：将String保存到ShareUtils
        ShareUtils.putString(mContext, "circle_image", imgString);
    }

    /**
     * 将储存在ShareUtils中的头像字符串转换成图片
     * @param mContext 上下文
     * @param imageView 圆形头像控件
     */
    public static void getImageToShare(Context mContext, ImageView imageView) {
        String imgString = ShareUtils.getString(mContext, "circle_image", ""); //1.获取字符串
        if (!imgString.equals("")) { //若图片数据不为空
            //2.利用Base64将String转换成字节数组输入流
            byte[] byteArray = Base64.decode(imgString, Base64.DEFAULT); //将String编码成byte数组
            ByteArrayInputStream byStream = new ByteArrayInputStream(byteArray); //新建字节数组输入流
            //3.生成Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byStream); //将字节数组输入流编码成Bitmap
            imageView.setImageBitmap(bitmap); //将Bitmap位图设置到圆形头像组件
        }
    }

    /**
     * 获取版本号
     * @param mContext 上下文
     * @return 版本号
     */
    public static String getVersion(Context mContext) {
        PackageManager packageManager = mContext.getPackageManager(); //获取包管理器
        try {
            PackageInfo info = packageManager.getPackageInfo(mContext.getPackageName(), 0); //获取包信息
            return info.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            return mContext.getString(R.string.unknow);
        }
    }
}
