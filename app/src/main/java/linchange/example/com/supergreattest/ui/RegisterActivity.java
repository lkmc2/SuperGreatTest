package linchange.example.com.supergreattest.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.entity.MyUser;

//注册界面
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_user; //用户名输入框
    private EditText et_age; //年龄输入框
    private EditText et_summary; //简介输入框
    private RadioGroup mRadioGroup; //单选框组
    private EditText et_pass; //密码输入框
    private EditText et_password; //密码再次输入框
    private EditText et_email; //邮箱输入框
    private Button btn_register; //注册按钮

    private boolean isGender = true;//性别（男为true，女为false）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();//初始化View
    }

    //初始化View
    private void initView() {
        //绑定视图
        et_user = (EditText) findViewById(R.id.et_user);
        et_age = (EditText) findViewById(R.id.et_age);
        et_summary = (EditText) findViewById(R.id.et_summary);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                //获取输入框的值
                String name = et_user.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String summary = et_summary.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();

                //判断必填字段是否全部填满
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) &&
                        !TextUtils.isEmpty(pass) &&
                        !TextUtils.isEmpty(password) &&
                        !TextUtils.isEmpty(email)) {
                    //判断两次输入的密码是否一致
                    if (pass.equals(password)) { //密码一致后再判断其他的
                        //先判断性别
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                                if (checkedId == R.id.rb_boy) {
                                    isGender = true;
                                } else if (checkedId == R.id.rb_girl) {
                                    isGender = false;
                                }
                            }
                        });

                        //判断简介是否为空
                        if (TextUtils.isEmpty(summary)) {
                            summary = getString(R.string.default_summary);
                        }

                        //创建用户
                        MyUser user = new MyUser();
                        user.setUsername(name);
                        user.setPassword(password);
                        user.setEmail(email);
                        user.setAge(Integer.parseInt(age));
                        user.setSex(isGender);
                        user.setSummary(summary);

                        //注册用户
                        user.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                if(e == null){ //当异常为空时注册成功
                                    Toast.makeText(getApplicationContext(), R.string.register_success, Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{ //注册失败
                                    Toast.makeText(getApplicationContext(), getString(R.string.register_fail) + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else { //两次密码输入不一致时
                        Toast.makeText(this, R.string.psw_no_same, Toast.LENGTH_SHORT).show();
                    }
                } else { //必填字段未填满
                    Toast.makeText(this, R.string.enter_not_null, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
