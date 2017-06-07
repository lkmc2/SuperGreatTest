package linchange.example.com.supergreattest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import linchange.example.com.supergreattest.MainActivity;
import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.entity.MyUser;
import linchange.example.com.supergreattest.utils.ShareUtils;
import linchange.example.com.supergreattest.view.CustomDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    
    private EditText et_name; //用户名
    private EditText et_password; //密码
    private Button btn_login; //登陆按钮
    private Button btn_register; //注册按钮
    private CheckBox keep_password; //记住密码单选框
    private TextView tv_forget; //忘记密码

    private CustomDialog dialog; //正在登陆提示框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initView();//初始化UI
    }

    //初始化UI
    private void initView() {
        //绑定视图
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        keep_password = (CheckBox) findViewById(R.id.keep_password);
        tv_forget = (TextView) findViewById(R.id.tv_forget);

        //初始化自定义Dialog
        dialog = new CustomDialog(this, 300, 300, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER);
        //设置Dialog在屏幕外点击无效
        dialog.setCancelable(false);

        //设置点击事件
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_forget.setOnClickListener(this);

        //设置选中的状态
        boolean isCheck = ShareUtils.getBoolean(this, "keeppass", false);
        keep_password.setChecked(isCheck);
        if (isCheck) { //如果上一次已选择记住密码
            et_name.setText(ShareUtils.getString(this, "name", "")); //将用户名设置到用户名输入框
            et_password.setText(ShareUtils.getString(this, "password", "")); //将密码设置到密码输入框

            et_password.setSelection(ShareUtils.getString(this, "password", "").length()); //将光标移动到密码输入框文字的最后
            et_name.setSelection(ShareUtils.getString(this, "name", "").length()); //将光标移动到用户名输入框文字的最后
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
                break;
            case R.id.btn_register: //注册事件
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.btn_login: //登陆事件
                //1.获取输入框的值
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                //2.判断是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(password)) { //用户名和密码都不为空
                    //显示正在登陆提示框
                    dialog.show();

                    //创建登陆对象
                    final MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);

                    //执行登陆操作
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            //正在登陆提示框消失
                            dialog.dismiss();

                            //判断结果
                            if (e == null) { //未产生异常
                                //判断邮箱是否验证
                                if (user.getEmailVerified()) { //用户邮箱已验证
                                    //跳转
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                } else { //用户邮箱未验证
                                    Toast.makeText(LoginActivity.this, "请前往邮箱认证", Toast.LENGTH_SHORT).show();
                                }
                            } else { //产生异常
                                int errorCode = e.getErrorCode(); //获取错误码

                                if (errorCode == 9016) { //网络未连接
                                    Toast.makeText(LoginActivity.this, "无网络连接，请检查您的手机网络。", Toast.LENGTH_SHORT).show();
                                } else if (errorCode == 101) { //账号密码错误
                                    Toast.makeText(LoginActivity.this, "帐号或密码不正确", Toast.LENGTH_SHORT).show();
                                } else { //其他错误
                                    Toast.makeText(LoginActivity.this, "登陆失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                } else { //用户名或密码为空
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //假设现在输入用户名和密码，但是不点击登陆，而是直接退出了
    @Override
    protected void onDestroy() {
        super.onDestroy();

//        //保存状态
        ShareUtils.putBoolean(this, "keeppass", keep_password.isChecked());
        saveOrClearNameAndPsw();
    }

    //保存或清除密码
    private void saveOrClearNameAndPsw() {
        //是否记住密码
        if (keep_password.isChecked()) { //记住密码
            //记住用户名和密码
            ShareUtils.putString(this, "name", et_name.getText().toString().trim());
            ShareUtils.putString(this, "password", et_password.getText().toString().trim());
        } else { //清除密码
            ShareUtils.deleteShare(this, "name");
            ShareUtils.deleteShare(this, "password");
        }
    }


}
