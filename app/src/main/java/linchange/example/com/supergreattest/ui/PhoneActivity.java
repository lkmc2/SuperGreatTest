package linchange.example.com.supergreattest.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.utils.LogUtils;
import linchange.example.com.supergreattest.utils.StaticClass;

//电话归属地查询界面
public class PhoneActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PhoneActivity";

    private EditText et_number; //号码输入框
    private ImageView iv_company; //运营商logo
    private TextView tv_result; //查询结果

    private Button btn_1; //数字键1
    private Button btn_2; //数字键2
    private Button btn_3; //数字键3
    private Button btn_4; //数字键4
    private Button btn_5; //数字键5
    private Button btn_6; //数字键6
    private Button btn_7; //数字键7
    private Button btn_8; //数字键8
    private Button btn_9; //数字键9
    private Button btn_0; //数字键0
    private Button btn_del; //删除键
    private Button btn_query; //查询键

    private boolean isQueryEnd = false; //表示是否查询完毕的标志位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        initView(); //初始化View
    }

    //初始化View
    private void initView() {
        //绑定视图
        et_number = (EditText) findViewById(R.id.et_number);
        iv_company = (ImageView) findViewById(R.id.iv_company);
        tv_result = (TextView) findViewById(R.id.tv_result);

        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_query = (Button) findViewById(R.id.btn_query);

        //设置点击事件
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_0.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_query.setOnClickListener(this);

        //为删除键设置长按事件（清除号码输入框所有文字）
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_number.setText(""); //清空号码输入框中的所有文字
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        /**
         * 逻辑
         * 1.获取输入框的内容
         * 2.判断内容是否为空
         * 3.网络请求
         * 4.解析Json
         * 5.结果显示
         * ----------
         * 键盘逻辑
         */
        //1.获取输入框的内容（手机号）
        String str = et_number.getText().toString();

        switch (view.getId()) {
            case R.id.btn_1: //按下数字键
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
            case R.id.btn_0:
                if (isQueryEnd) { //如果查询结束标志位为真（表示上一次查询已经结束）
                    isQueryEnd = false; //将查询标志位设为假
                    str = ""; //将电话号码字符设为空
                    et_number.setText(""); //将电话号码输入框的内容设为空
                }
                //每次结尾添加一个文字
                et_number.setText(str + ((Button) view).getText()); //当前按钮上的文字与之前的文字拼接
                et_number.setSelection(str.length() + 1); //移动光标到输入框文字的最后面（不然会一直停留在最开始的位置）
                break;

            case R.id.btn_del: //按下删除键
                if (!TextUtils.isEmpty(str) && str.length() > 0) { //输入的文字不为空而且长度大于0
                    //每次结尾减去一个文字
                    et_number.setText(str.substring(0, str.length() - 1)); //截取当前文字后（去掉最后一个）设置到输入框
                    et_number.setSelection(str.length() - 1); //移动光标到输入框文字的最后面（不然会一直停留在最后面的位置）
                }
                break;

            case R.id.btn_query: //按下查询键
                if (!TextUtils.isEmpty(str)) { //当输入的电话号码不为空
                    getPhone(str); //获取手机归属地
                } else { //当输入的电话号码为空时
                    Toast.makeText(this, getString(R.string.enter_not_null), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 获取手机归属地
     * @param str 输入的手机号
     */
    private void getPhone(String str) {
        String url = "http://apis.juhe.cn/mobile/get?phone=" + str +
                "&key=" + StaticClass.PHONE_KEY; //拼接请求的网址

        //执行网络请求
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
//                Toast.makeText(PhoneActivity.this, "结果:" + t, Toast.LENGTH_SHORT).show();
                parsingJson(t); //解析Json
                LogUtils.i(TAG, "getPhone:phone=" + t);
            }
        });
    }
    /**
     *
     {
     "resultcode":"200",
     "reason":"Return Successd!",
     "result":{
         "province":"浙江",
         "city":"杭州",
         "areacode":"0571",
         "zip":"310000",
         "company":"中国移动",
         "card":"移动动感地带卡"
        }
     }
     */

    /**
     *解析Json数据
     * @param t json数据
     */
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t); //生成Json根对象

            String resultCode = jsonObject.getString("resultcode"); //从根对象中获取结果码
            if (!"200".equals(resultCode)) { //当结果码不为200时（结果码为200代表返回正确）
                Toast.makeText(this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                iv_company.setBackgroundResource(0); //清空运营商的logo
                tv_result.setText(""); //清空查询结果
            }

            JSONObject resultObject = jsonObject.getJSONObject("result"); //从根对象中获取result对象
            String province = resultObject.getString("province"); //从result对象中获取省份
            String city = resultObject.getString("city"); //从result对象中获取城市
            String areacode = resultObject.getString("areacode"); //从result对象中获取区号
            String zip = resultObject.getString("zip"); //从result对象中获取邮编
            String company = resultObject.getString("company"); //从result对象中获取运营商
            String card = resultObject.getString("card"); //从result对象中获取电话卡类型

            if ("".equals(card)) { //如果电话卡类型为空
                card = "无"; //将电话卡类型设置为无
            }

            //将返回文字设置进查询结果组件
            tv_result.setText(getString(R.string.text_belong_place) + province + city + "\n");
            tv_result.append(getString(R.string.text_areacode) + areacode + "\n");
            tv_result.append(getString(R.string.text_zip) + zip + "\n");
            tv_result.append(getString(R.string.text_phone_company) + company + "\n");
            tv_result.append(getString(R.string.text_card_type) + card);

            //运营商logo显示（把对应的运营商图片设置到logo控件上）
            switch (company) {
                case "中国移动":
                case "移动":
                    iv_company.setBackgroundResource(R.drawable.china_mobile);
                    break;

                case "中国联通":
                case "联通":
                    iv_company.setBackgroundResource(R.drawable.china_unicom);
                    break;

                case "中国电信":
                case "电信":
                    iv_company.setBackgroundResource(R.drawable.china_telecom);
                    break;
            }
            isQueryEnd = true; //设置查询结束标志位为真
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
