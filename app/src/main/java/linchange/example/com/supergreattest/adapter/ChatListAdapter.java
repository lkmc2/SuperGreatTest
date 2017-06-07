package linchange.example.com.supergreattest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.entity.ChatListData;

/**
 * Created by Lin Change on 2017-02-22.
 */
//对话框适配器
public class ChatListAdapter extends BaseAdapter {
    private static final String TAG = "ChatListAdapter";

    public static final int VALUE_LEFT_TEXT = 1; //左对话框类型
    public static final int VALUE_RIGHT_TEXT = 2; //右对话框类型

    private Context mContext; //上下文
    private LayoutInflater inflater; //布局加载器
    private ChatListData data; //对话框内容实体类
    private List<ChatListData> mList; //对话框内容实体类列表

    public ChatListAdapter(Context mContext, List<ChatListData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //获取布局加载器服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        //获取当前要显示的item，根据这个type来区分数据的加载
        int type = getItemViewType(position); //获取item的类型

        if (convertView == null) { //如果item内容为空，则新建并保存到convertView中
            viewHolder = new ViewHolder(); //新建对话框的ViewHolder
            switch (type) { //根据对话框类型判断
                case VALUE_LEFT_TEXT: //如果是左对话框类型
                    //新建左对话框的ViewHolder
                    convertView = inflater.inflate(R.layout.left_item, null); //加载左对话框对应的布局
                    viewHolder.tv_text = (TextView) convertView.findViewById(R.id.tv_left_text); //找到左对话框对应的组件
                    break;
                case VALUE_RIGHT_TEXT: //如果是右对话框类型
                    convertView = inflater.inflate(R.layout.right_item, null); //加载右对话框对应的布局
                    viewHolder.tv_text = (TextView) convertView.findViewById(R.id.tv_right_text); //找到右对话框对应的组件
                    break;
            }
            convertView.setTag(viewHolder); //将viewHolder保存在convertView中
        } else {
            viewHolder = (ViewHolder) convertView.getTag(); //从convertView取出viewHolder
        }

        ChatListData data = mList.get(position); //获取当前位置的聊天内容实体类
        viewHolder.tv_text.setText(data.getText()); //给聊天对话框组件设置内容

        return convertView; //返回生成的item
    }

    //根据数据源的position来返回要显示的item
    @Override
    public int getItemViewType(int position) {
        ChatListData data = mList.get(position); //获取当前位置的数据
        return data.getType(); //返回当前数据的类型
    }

    //获取视窗类型的数量
    @Override
    public int getViewTypeCount() {
        return 3; //左右两种类型加上View本身
    }

    class ViewHolder{ //ViewHolder的父类
        private TextView tv_text;
    }

}


