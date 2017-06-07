package linchange.example.com.supergreattest.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.adapter.CourierAdapter;
import linchange.example.com.supergreattest.entity.CourierData;
import linchange.example.com.supergreattest.utils.LogUtils;
import linchange.example.com.supergreattest.utils.StaticClass;

//快递查询界面
public class CourierActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CourierActivity";

    private EditText et_name; //快递公司输入框
    private EditText et_number; //快递单号输入框
    private Button btn_search; //查询按钮按钮
    private ListView mListView; //信息展示列表

    private List<CourierData> mList = new ArrayList<>(); //快递实体类列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        initView(); //初始化View
    }

    //初始化View
    private void initView() {
        //绑定视图
        et_name = (EditText) findViewById(R.id.et_name);
        et_number = (EditText) findViewById(R.id.et_number);
        btn_search = (Button) findViewById(R.id.btn_search);
        mListView = (ListView) findViewById(R.id.mListView);

        //设置点击事件
        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search: //点击查询快递按钮
                /**
                 * 1.获取输入框的内容
                 * 2.判断是否为空
                 * 3.拿到数据去请求Json
                 * 4.解析Json
                 * 5.ListView适配器
                 * 6.实体类（item）
                 * 7.设置数据/显示效果
                 */
                //1.获取输入框的内容
                String name = et_name.getText().toString().trim();
                String number = et_number.getText().toString().trim();

                //拼接URL
                String url = "http://v.juhe.cn/exp/index?key=" + StaticClass.COURIER_KEY +
                        "&com=" + name + "&no=" + number;

                //2.判断是否为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) { //必填内容不为空
                    //3.拿到数据去请求Json
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) { //请求成功后执行的方法
//                            Toast.makeText(CourierActivity.this, t, Toast.LENGTH_SHORT).show();
                            LogUtils.i(TAG, "onClick:Json:" + t);
                            //清空快递实体类列表
                            mList.clear();

                            //4.解析Json
                            parsingJson(t);
                        }
                    });

                } else { //必填内容未填满
                    Toast.makeText(this, getString(R.string.enter_not_null), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 解析传入的Json数据
     * @param t json数据
     */
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t); //获取Json根对象
            JSONObject jsonResult = jsonObject.getJSONObject("result"); //获取根对象中的result节点
            JSONArray jsonArray = jsonResult.getJSONArray("list"); //获取result节点中的list数组

            for (int i = 0; i < jsonArray.length(); i++) { //遍历list数组
                JSONObject json = jsonArray.getJSONObject(i); //从list数组中获取单个Json对象

                CourierData data = new CourierData(); //新建快递实体类
                data.setRemark(json.getString("remark")); //设置快递状态
                data.setZone(json.getString("zone")); //设置快递到达城市
                data.setDatetime(json.getString("datetime")); //设置快递状态改变时间

                mList.add(data); //将实体类添加进实体类列表
            }

            Collections.reverse(mList); //将实体类列表中的数据逆序排列

            CourierAdapter adapter = new CourierAdapter(this, mList); //新建快递类适配器
            mListView.setAdapter(adapter); //为实体类展示列表界面设置适配器
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
