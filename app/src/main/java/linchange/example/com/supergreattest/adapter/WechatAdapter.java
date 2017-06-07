package linchange.example.com.supergreattest.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.entity.WechatData;
import linchange.example.com.supergreattest.utils.LogUtils;
import linchange.example.com.supergreattest.utils.PicassoUtils;

/**
 * Created by Lin Change on 2017-02-23.
 */
//微信文章适配器
public class WechatAdapter extends BaseAdapter {
    private static final String TAG = "WechatAdapter";

    private Context mContext; //上下文
    private LayoutInflater inflater; //布局加载器
    private List<WechatData> mList; //微信文章实体类列表
    private WechatData data; //微信文章实体类临时变量

    private int screenWidth; //屏幕的宽
    private int screenHeight; //屏幕的高
    private WindowManager windowManager; //窗口管理器

    public WechatAdapter(Context mContext, List<WechatData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //生成布局加载器
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //获取窗口管理器
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics(); //新建显示矩阵
        windowManager.getDefaultDisplay().getMetrics(metrics); //获取窗口管理器中的屏幕信息放到显示矩阵中
        screenWidth = metrics.widthPixels; //给屏幕的宽赋值
        screenHeight = metrics.heightPixels; //给屏幕的高赋值

        LogUtils.i(TAG, "screenWidth=" + screenWidth + ",screenHeight=" + screenHeight);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null; //用于绑定视图的内部类

        if (convertView == null) { //如果convertView没有内容
            viewHolder = new ViewHolder(); //新建视图绑定器
            convertView = inflater.inflate(R.layout.wechat_item, null); //加载布局

            viewHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img); //绑定视图
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_source = (TextView) convertView.findViewById(R.id.tv_source);

            convertView.setTag(viewHolder); //将viewHolder保存到convertView中
        } else { //如果convertView有内容
            viewHolder = (ViewHolder) convertView.getTag(); //从convertView取出viewHolder
        }

        data = mList.get(position); //获取当前位置的数据
        viewHolder.tv_title.setText(data.getTitle()); //设置新闻标题内容
        viewHolder.tv_source.setText(data.getSource()); //设置新闻来源内容

//        LogUtils.i(TAG, "position=" + position + "     data=" + data);

        if (!"".equals(data.getImgUrl())) { //当图片网页链接不为空时
            PicassoUtils.loadImageViewSize(mContext, data.getImgUrl(),
                    screenWidth / 3, screenHeight / 6, viewHolder.iv_img);//加载图片
        } else { //当图片网页链接不空时
            PicassoUtils.loadImageViewResource(mContext, R.drawable.ic_error,
                    screenWidth / 3, screenHeight / 6, viewHolder.iv_img); //加载错误图标
        }


        return convertView;
    }

    //用于绑定视图的内部类
    class ViewHolder {
        private ImageView iv_img; //新闻图片
        private TextView tv_title; //新闻标题
        private TextView tv_source; //新闻来源
    }

}
