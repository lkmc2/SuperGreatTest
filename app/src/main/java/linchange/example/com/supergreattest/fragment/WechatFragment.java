package linchange.example.com.supergreattest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.adapter.WechatAdapter;
import linchange.example.com.supergreattest.entity.WechatData;
import linchange.example.com.supergreattest.ui.WebViewActivity;
import linchange.example.com.supergreattest.utils.LogUtils;
import linchange.example.com.supergreattest.utils.StaticClass;

/**
 * Created by Lin Change on 2017-02-16.
 */
//微信文章精选界面
public class WechatFragment extends Fragment {
    private static final String TAG = "WechatFragment";

    private ListView mListView; //微信文章展示列表
    private List<WechatData> mList = new ArrayList<>(); //储存文章的列表

    private List<String> mListTitle = new ArrayList<>(); //存储文章标题的列表
    private List<String> mListUrl = new ArrayList<>(); //存储文章网址的列表

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_wechat, null); //加载布局
        findView(view); //初始化View
        return view;
    }

    //初始化View
    private void findView(View view) {
        //绑定视图
        mListView = (ListView) view.findViewById(R.id.mListView);

        //拼接URL(最后面加&ps=40可返回40条，默认返回20条，最多返回100条)
        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_KEY;
        //网络提交
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) { //当提交成功时返回数据
//                Toast.makeText(getActivity(), t, Toast.LENGTH_SHORT).show();
                LogUtils.i(TAG, "json=" + t);
                parsingJson(t); //解析json数据
            }
        });

        //给文章展示列表设置点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                LogUtils.i(TAG, "position=" + position);
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", mListTitle.get(position)); //保存当前文章的标题
                intent.putExtra("url", mListUrl.get(position)); //保存当前文章详情网址
                startActivity(intent); //启动新的界面
            }
        });
    }

    /**
     * 解析json数据
     * @param t 服务器返回的json数据
     */
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t); //生成json根对象
            JSONObject resultObject = jsonObject.getJSONObject("result"); //从根对象获取result结点
            JSONArray jsonList = resultObject.getJSONArray("list"); //从result结点获取list数组

            //遍历list中的对象并添加到文章实体类列表中
            for (int i = 0; i < jsonList.length(); i++) {
                JSONObject json = jsonList.getJSONObject(i); //从list中获取json对象

                String title = json.getString("title"); //获取文章标题
                String url = json.getString("url"); //获取文章详情网址
                String imgUrl = json.getString("firstImg");

                WechatData data = new WechatData(); //新建微信文章实体类对象
                data.setTitle(title); //给文章设置标题
                data.setSource(json.getString("source")); //给文章设置来源
                data.setImgUrl(imgUrl); //给文章设置图片

                mList.add(data); //将文章实体类加入文章实体类列表
                mListTitle.add(title); //将文章标题加入文章标题列表
                mListUrl.add(url); //将文章详情网址加入文章详情网址列表

//                LogUtils.i(TAG, "parsingJson position=" + i + ",  imgUrl=" + imgUrl);
            }

            WechatAdapter adapter = new WechatAdapter(getActivity(), mList); //新建文章适配器
            mListView.setAdapter(adapter); //给文章显示列表添加适配器
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** JSON格式
     * {
         "reason":"success",
         "result":{
             "list":[
                 {
                     "id":"wechat_20150401071581",
                     "title":"号外：集宁到乌兰花的班车出事了！！！！！",
                     "source":"内蒙那点事儿",
                     "firstImg":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-214279.jpg/168",
                     "mark":"",
                     "url":"http://v.juhe.cn/weixin/redirect?wid=wechat_20150401071581"
                 },
                 {
                     "id":"wechat_20150402028462",
                     "title":"【夜读】梁晓声：你追求的，就是你人生的意义",
                     "source":"人民日报",
                     "firstImg":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-214521.jpg/168",
                     "mark":"",
                     "url":"http://v.juhe.cn/weixin/redirect?wid=wechat_20150402028462"
                 }
                 ],
             "totalPage":16,
             "ps":20,
             "pno":1
         },
         "error_code":0
     }
     */
}
