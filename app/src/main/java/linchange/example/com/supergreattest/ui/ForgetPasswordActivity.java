package linchange.example.com.supergreattest.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.entity.MyUser;

//忘记或重置密码界面
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_pass_old; //旧密码输入框
    private EditText et_pass_new; //新密码输入框
    private EditText et_password_again; //再次输入密码框
    private Button btn_update_password; //重置密码按钮

    private EditText et_email; //邮箱输入框
    private Button btn_by_email; //通过邮箱修改密码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        initView(); //初始化View
    }

    //初始化View
    private void initView() {
        //绑定视图
        et_email = (EditText) findViewById(R.id.et_email);
        et_pass_old = (EditText) findViewById(R.id.et_pass_old);
        et_pass_new = (EditText) findViewById(R.id.et_pass_new);
        et_password_again = (EditText) findViewById(R.id.et_password_again);
        btn_by_email = (Button) findViewById(R.id.btn_by_email);
        btn_update_password = (Button) findViewById(R.id.btn_update_password);

        //设置点击事件
        btn_by_email.setOnClickListener(this);
        btn_update_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_password: //直接重置密码事件
                //1.获取三个密码输入框中的值
                String oldPassword = et_pass_old.getText().toString().trim();
                String newPassword = et_pass_new.getText().toString().trim();
                String againPassword = et_password_again.getText().toString().trim();

                //2.判断必填的旧密码、新密码、再次输入密码不能为空
                if (!TextUtils.isEmpty(oldPassword) &
                        !TextUtils.isEmpty(newPassword) &
                        !TextUtils.isEmpty(againPassword)) { //三个密码都非空时
                    //3.判断两次新密码是否相等
                    if (newPassword.equals(againPassword)) { //两次新密码一致
                        //4.重置密码
                        MyUser.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) { //重置密码无异常
                                    Toast.makeText(ForgetPasswordActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else { //重置密码异常
                                    int errorCode = e.getErrorCode(); //获取错误码
                                    if (errorCode == 210) { //原密码错误
                                        Toast.makeText(ForgetPasswordActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                                    } else { //其他错误
                                        Toast.makeText(ForgetPasswordActivity.this, "重置密码失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    } else { //两次新密码不一致
                        Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "密码输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_by_email: //通过邮箱修改密码事件
                //1.获取邮箱输入框的内容
                final String email = et_email.getText().toString().trim();
                //2.判断是否为空
                if (!TextUtils.isEmpty(email)) { //输入框不为空
                    //3.发送邮件
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) { //发送邮件无异常
                                Toast.makeText(ForgetPasswordActivity.this, "已经发送至邮箱：" + email, Toast.LENGTH_SHORT).show();
                            } else { //发送邮件异常
                                Toast.makeText(ForgetPasswordActivity.this, "邮箱发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else { //输入框为空
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
