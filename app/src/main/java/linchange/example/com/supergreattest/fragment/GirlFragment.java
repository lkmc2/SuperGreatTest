package linchange.example.com.supergreattest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.adapter.GirlAdapter;
import linchange.example.com.supergreattest.entity.GirlData;
import linchange.example.com.supergreattest.utils.LogUtils;
import linchange.example.com.supergreattest.utils.PicassoUtils;
import linchange.example.com.supergreattest.utils.StaticClass;
import linchange.example.com.supergreattest.view.CustomDialog;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by Lin Change on 2017-02-16.
 */
//美女图展示界面
public class GirlFragment extends Fragment {
    private static final String TAG = "GirlFragment";

    private GridView mGridView; //图片展示列表
    private List<GirlData> mList = new ArrayList<>(); //妹子图实体类列表
    private GirlAdapter mAdapter; //妹子图片适配器
    private CustomDialog dialog; //提示框
    private ImageView iv_img; //预览图片
    private List<String> mListUrl = new ArrayList<>(); //网址列表
    private PhotoViewAttacher mAttacher; //图片放大观看组件

    /**
     * item点击事件处理流程
     * 1.监听点击事件
     * 2.提示框
     * 3.加载图片
     * 4.PhotoView
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_girl, null); //加载布局
        findView(view); //初始化View
        return view;
    }

    //初始化View
    private void findView(View view) {
        //绑定布局
        mGridView = (GridView) view.findViewById(R.id.mGridView);

        //初始化提示框
        dialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                R.layout.dialog_gril, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);

        iv_img = (ImageView) dialog.findViewById(R.id.iv_img); //绑定dialog里的组件


        String welfare = null; //网址中的中文字符（福利）
        try {
            welfare = URLEncoder.encode(getString(R.string.welfare), "utf-8"); //文字转码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //进行网络请求
        RxVolley.get(StaticClass.GIRL_URL_PRE + welfare + StaticClass.GIRL_URL_LAST, new HttpCallback() {
            @Override
            public void onSuccess(String t) { //请求成功时执行
                LogUtils.i(TAG, "json=" + t);
                parsingJson(t); //解析json数据
            }
        });

        //图片展示窗口设置item点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PicassoUtils.loadImageView(getActivity(), mListUrl.get(position), iv_img); //加载图片

//                PicassoUtils.loadImageViewSize(getActivity(), mListUrl.get(position), 720, 1280, iv_img);

                mAttacher = new PhotoViewAttacher(iv_img); //初始化图片放大观看组件
                mAttacher.update(); //刷新

                dialog.show(); //显示提示框
            }
        });
    }

    /**
     * 解析json数据
     * @param t json数据
     */
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t); //生成json根布局
            JSONArray resultArray = jsonObject.getJSONArray("results"); //获取result数组

            for (int i = 0; i < resultArray.length(); i++) { //遍历result数组中的数据
                JSONObject json = (JSONObject) resultArray.get(i); //获取当前位置的json对象
                String url = json.getString("url"); //获取json对象中的url属性

                mListUrl.add(url); //把网址放入网址列表

                GirlData data = new GirlData(); //新建妹子图实体类
                data.setImgUrl(url); //给妹子图实体类设置图片网址

                mList.add(data); //将妹子图实体类添加到妹子图实体类列表
            }

            mAdapter = new GirlAdapter(getActivity(), mList); //生成适配器
            mGridView.setAdapter(mAdapter); //图片展示列表与适配器进行绑定

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**json数据格式
     * {
         "count":50,
         "error":false,
         "results":[
             {
             "desc":"11.3",
             "ganhuo_id":"56cc6d1d421aa95caa7075b1",
             "publishedAt":"2015-11-03T06:04:59.454000",
             "readability":"",
             "type":"福利",
             "url":"http://ww2.sinaimg.cn/large/7a8aed7bjw1exng5dd728j20go0m877n.jpg",
             "who":"张涵宇"
             },
             {
             "desc":"5.25",
             "ganhuo_id":"56cc6d1d421aa95caa7075e0",
             "publishedAt":"2015-05-25T03:37:08.537000",
             "readability":"",
             "type":"福利",
             "url":"http://ww4.sinaimg.cn/large/7a8aed7bgw1esfbgw6vc3j20gy0pedic.jpg",
             "who":"张涵宇"
             }
         ]
     }
     *
     *
     */
}
