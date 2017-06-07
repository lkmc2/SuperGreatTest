package linchange.example.com.supergreattest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import linchange.example.com.supergreattest.R;

/**
 * Created by Lin Change on 2017-02-23.
 */
//Picasso图片加载器封装工具类
public class PicassoUtils {

    /**
     * 默认加载图片
     * @param mContext 上下文
     * @param url 图片的网址
     * @param imageView 需要加载到的图片控件
     */
    public static void loadImageView(Context mContext, String url, ImageView imageView) {
        Picasso.with(mContext).load(url).into(imageView);
    }


    /**
     * 默认加载图片（指定大小）
     * @param mContext 上下文
     * @param url 图片的网址
     * @param width 指定图片的宽
     * @param height 指定图片的高
     * @param imageView 需要加载到的图片控件
     */
    public static void loadImageViewSize(Context mContext, String url, int width, int height, ImageView imageView) {
        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.ic_loding_pic)
                .error(R.drawable.ic_error)
                .resize(width, height)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 加载图片有默认显示图片和加载错误显示图片
     * @param mContext 上下文
     * @param url 图片的网址
     * @param loadImg 加载成功时显示的图片
     * @param errorImg 加载失败时显示的图片
     * @param imageView 需要加载到的图片控件
     */
    public static void loadImageViewHolder(Context mContext, String url, int loadImg, int errorImg, ImageView imageView) {
        Picasso.with(mContext)
                .load(url)
                .placeholder(loadImg)
                .error(errorImg)
                .into(imageView);
    }

    /**
     * 加载指定大小的drawable图片
     * @param mContext 上下文
     * @param loadImg 需要显示的资源图片
     * @param width 需要显示的宽
     * @param height 需要显示的高
     * @param imageView 需要加载到的图片控件
     */
    public static void loadImageViewResource(Context mContext, int loadImg,  int width, int height, ImageView imageView) {
        Picasso.with(mContext)
                .load(loadImg)
                .resize(width, height)
                .into(imageView);
    }

    /**
     * 加载裁剪的图片
     * @param mContext 上下文
     * @param url 图片的网址
     * @param imageView 需要加载到的图片控件
     */
    public static void loadImageViewCrop(Context mContext, String url, ImageView imageView) {
        Picasso.with(mContext)
                .load(url)
                .transform(new CropSquareTransformation())
                .into(imageView);
    }

    //按比例裁剪 矩形
    public static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle(); //回收
            }
            return result;
        }

        @Override
        public String key() { return "square()"; }
    }
}
