package linchange.example.com.supergreattest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.entity.CourierData;

/**
 * Created by Lin Change on 2017-02-21.
 */

//快递查询实体类
public class CourierAdapter extends BaseAdapter {
    private Context mContext; //上下文
    private List<CourierData> mList; //数据列表
    private LayoutInflater inflater; //布局加载器
    private CourierData data; //快递实体类临时变量


    public CourierAdapter(Context mContext, List<CourierData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        if (convertView == null) { //第一次加载
            viewHolder = new ViewHolder(); //新建ViewHolder
            convertView = inflater.inflate(R.layout.layout_courier_item, null); //加载布局

            //为ViewHolder中的组件绑定布局
            viewHolder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
            viewHolder.tv_zone = (TextView) convertView.findViewById(R.id.tv_zone);
            viewHolder.tv_datetime = (TextView) convertView.findViewById(R.id.tv_datetime);

            convertView.setTag(viewHolder); //把viewHolder设置进convertView标签
        } else { //非第一次加载
            viewHolder = (ViewHolder) convertView.getTag(); //从convertView标签中取出viewHolder
        }

        //从列表中获取当前位置的数据
        data = mList.get(position);

        //为相应控件设置文字
        viewHolder.tv_remark.setText(data.getRemark());
        viewHolder.tv_zone.setText(data.getZone());
        viewHolder.tv_datetime.setText(data.getDatetime());

        return convertView;
    }

    class ViewHolder { //保存控件状态的内部类
        private TextView tv_remark; //快递状态
        private TextView tv_zone; //已到达城市
        private TextView tv_datetime; //快递状态改变时间
    }

}
