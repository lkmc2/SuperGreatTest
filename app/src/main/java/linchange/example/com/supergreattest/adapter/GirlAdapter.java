package linchange.example.com.supergreattest.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.entity.GirlData;
import linchange.example.com.supergreattest.utils.PicassoUtils;

/**
 * Created by Lin Change on 2017-02-23.
 */
//妹子图浏览适配器
public class GirlAdapter extends BaseAdapter {

    private Context mContext; //上下文
    private List<GirlData> mList; //妹子图实体类列表
    private LayoutInflater inflater; //布局加载器
    private GirlData data; //妹子图实体类临时变量

    private WindowManager windowManager; //窗口管理器
    private int screenWidth; //屏幕的宽
    private int screenHeight; //屏幕的高

    public GirlAdapter(Context mContext, List<GirlData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //生成布局加载器
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //获取窗口管理器
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics(); //新建显示矩阵
        windowManager.getDefaultDisplay().getMetrics(metrics); //从窗口管理器获取屏幕宽高放入显示矩阵

        screenWidth = metrics.widthPixels; //设置屏幕的宽
        screenHeight = metrics.heightPixels; //设置屏幕的高
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
        ViewHolder viewHolder = null;

        if (convertView == null) { //如果当前View为空
            viewHolder = new ViewHolder(); //新建ViewHolder

            convertView = inflater.inflate(R.layout.gril_item, null); //加载布局
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView); //绑定视图

            convertView.setTag(viewHolder); //保存viewHolder到convertView
        } else { //如果当前View非空
            viewHolder = (ViewHolder) convertView.getTag(); //从convertView取出viewHolder
        }

        data = mList.get(position); //获取当前位置的妹子图实体类数据
        String url = data.getImgUrl(); //获取图片网址

        //加载图片
        PicassoUtils.loadImageViewSize(mContext, url, screenWidth / 2, screenHeight / 5, viewHolder.imageView);

        return convertView;
    }

    //保存视图组件的内容类
    class ViewHolder {
        private ImageView imageView; //对应的图片控件
    }
}
