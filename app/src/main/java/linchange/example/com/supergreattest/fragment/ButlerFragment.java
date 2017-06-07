package linchange.example.com.supergreattest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.adapter.ChatListAdapter;
import linchange.example.com.supergreattest.entity.ChatListData;
import linchange.example.com.supergreattest.utils.LogUtils;
import linchange.example.com.supergreattest.utils.ShareUtils;
import linchange.example.com.supergreattest.utils.StaticClass;

/**
 * Created by Lin Change on 2017-02-16.
 */
//管家机器人聊天界面
public class ButlerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ButlerFragment";

    private ListView mChatListView; //显示聊天内容的列表
//    private Button btn_left; //左按键
//    private Button btn_right; //右按键

    private EditText et_text; //内容输入框
    private Button btn_send; //发送按钮

    private List<ChatListData> mList = new ArrayList<>(); //聊天内容类实体列表

    private ChatListAdapter adapter; //适配器

    private SpeechSynthesizer mTts; //讯飞语音

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_butler, null); //加载布局
        findView(view); //初始化View
        return view;
    }

    //初始化View
    private void findView(View view) {
        //配置讯飞语音
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

        //绑定视图
        mChatListView = (ListView) view.findViewById(R.id.mChatListView);
        et_text = (EditText) view.findViewById(R.id.et_text);
        btn_send = (Button) view.findViewById(R.id.btn_send);
//        btn_left = (Button) view.findViewById(btn_left);
//        btn_right = (Button) view.findViewById(btn_right);

//        //设置点击事件
        btn_send.setOnClickListener(this);
//        btn_left.setOnClickListener(this);
//        btn_right.setOnClickListener(this);

        //给输入框设置键盘监听器
        et_text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER //当按下回车键和键盘回弹的时候
                        && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    btn_send.performClick(); //执行点击发送按钮的操作
                }
                return true;
            }
        });

        adapter = new ChatListAdapter(getActivity(), mList); //新建适配器
        mChatListView.setAdapter(adapter); //设置适配器

        addItem("我是聊天管家，很高兴为你服务", ChatListAdapter.VALUE_LEFT_TEXT); //默认对话内容
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send: //点击发送按钮
                /**
                 * 1.获取输入框的内容
                 * 2.判断是否为空
                 * 3.判断长度不能大于30
                 * 4.清空当前输入框
                 * 5.添加你输入的内容到右对话框
                 * 6.发送给机器人请求返回内容
                 * 7.拿到机器人的返回值添加到左对话框
                 */

                //1.获取输入框的内容
                String text = et_text.getText().toString();
                LogUtils.i(TAG, "text=" + text);
                //2.判断是否为空
                if (!TextUtils.isEmpty(text)) { //输入的内容为空
                    //3.判断长度不能大于30
                    if (text.length() <= 30) { //输入长度合理
                        //4.清空当前输入框
                        et_text.setText("");
                        //5.添加你输入的内容到右对话框
                        addItem(text, ChatListAdapter.VALUE_RIGHT_TEXT);

                        try {
                            text = URLEncoder.encode(text, "utf-8"); //文字转码
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        //6.发送给机器人请求返回内容
                        String url = "http://op.juhe.cn/robot/index?info=" + text +
                                "&key=" + StaticClass.CHAT_LIST_KEY; //拼接url

                        LogUtils.i(TAG, "url=" + url);
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                LogUtils.i(TAG, "json=" + t);
                                parsingJson(t); //解析json数据
                            }
                        });
                    } else { //输入长度不合理
                        Toast.makeText(getActivity(), R.string.enter_pass_limit, Toast.LENGTH_SHORT).show();
                    }
                } else { //输入的内容不为空
                    Toast.makeText(getActivity(), getString(R.string.enter_not_null), Toast.LENGTH_SHORT).show();
                }
                break;
//            case btn_left: //点击左按钮
//                addItem("左边", ChatListAdapter.VALUE_LEFT_TEXT);
//                break;
//            case btn_right: //点击右按钮
//                addItem("右边", ChatListAdapter.VALUE_RIGHT_TEXT);
//                break;
        }
    }

    /**
     * {
     *"reason":"成功的返回",
     *"result":
     *{
     *   "code":100000,
     *    text":"你好啊，希望你今天过的快乐"
     * },
     * "error_code":0
     * }
     */

    /**
     * 解析json数据
     * @param t 服务器返回的json数据
     */
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t); //新建json根对象
            JSONObject resultObject = jsonObject.getJSONObject("result"); //从根对象获取result结点

            //7.拿到机器人的返回值添加到左对话框
            String text = resultObject.getString("text"); //从result结点获取文字
            addItem(text, ChatListAdapter.VALUE_LEFT_TEXT); //聊天内容添加到左对话框
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一句界面上显示的聊天内容
     * @param text 聊天内容
     * @param type 显示的类型（左对话框、右对话框）
     */
    private void addItem(String text, int type) {
        if (type == ChatListAdapter.VALUE_LEFT_TEXT) { //如果是左对话框
            //获取上一次保存的语音播报开关状态
            boolean isSpeak = ShareUtils.getBoolean(getActivity(), "isSpeak", false);
            if (isSpeak) { //语音播报开关打开
                startSpeak(text); //开始说话
            }
        }

        ChatListData data = new ChatListData(); //新建聊天内容类实体
        data.setType(type); //设置类型
        data.setText(text); //设置显示的内容

        mList.add(data); //将聊天内容类实体加入聊天内容类实体列表
        adapter.notifyDataSetChanged(); //通过适配器通知当前内容改变

        //将对话内容显示窗口移到最下面
        mChatListView.setSelection(mChatListView.getBottom());
    }

    /**
     * 开始说话
     * @param text 需要语音读出的文字
     */
    private void startSpeak(String text) {
        //开始合成语音
        mTts.startSpeaking(text, mSynListener);
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };

}
