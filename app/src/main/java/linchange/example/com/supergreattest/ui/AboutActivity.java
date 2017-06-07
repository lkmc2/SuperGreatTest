package linchange.example.com.supergreattest.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.utils.UtilTools;

//关于软件界面
public class AboutActivity extends BaseActivity {

    private ListView mListView; //详情内容展示列表
    private List<String> mList = new ArrayList<>(); //存放需要展示的内容
    private ArrayAdapter<String> mAdapter; //列表适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //去掉Actionbar和TabLayout之间的阴影
        getSupportActionBar().setElevation(0);

        initView(); //初始化View
    }

    //初始化View
    private void initView() {
        //绑定视图
        mListView = (ListView) findViewById(R.id.mListView);

        //添加需要展示的数据
        mList.add("应用名：" + getString(R.string.app_name));
        mList.add("版本号：" + UtilTools.getVersion(this));
        mList.add("创作者：Lin Change");

        //新建适配器
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);

        //给内容展示列表设置适配器
        mListView.setAdapter(mAdapter);
    }


}
