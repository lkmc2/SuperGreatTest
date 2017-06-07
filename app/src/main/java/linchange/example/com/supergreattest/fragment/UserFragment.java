package linchange.example.com.supergreattest.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;
import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.entity.MyUser;
import linchange.example.com.supergreattest.ui.CourierActivity;
import linchange.example.com.supergreattest.ui.LoginActivity;
import linchange.example.com.supergreattest.ui.PhoneActivity;
import linchange.example.com.supergreattest.utils.LogUtils;
import linchange.example.com.supergreattest.utils.UtilTools;
import linchange.example.com.supergreattest.view.CustomDialog;

/**
 * Created by Lin Change on 2017-02-16.
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "UserFragment";

    private Button edit_user; //编辑用户资料按钮
    private Button btn_confirm; //确认修改按钮
    private Button btn_exit_user; //退出登陆按钮

    private EditText et_username; //用户名输入框
    private EditText et_sex; //性别输入框
    private EditText et_age; //年龄输入框
    private EditText et_summary; //简介输入框

    private CircleImageView circle_image; //圆形用户头像
    private CustomDialog dialog; //自定义提示框（在点击圆形头像后出现）
    private Button btn_camera; //提示框布局里面的拍照按钮
    private Button btn_picture; //提示框布局里面的从图库选择按钮
    private Button btn_cancel; //提示框布局里面的取消按钮

    private TextView tv_courier_search; //物流查询文字
    private TextView tv_place_belong; //归属地查询文字

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_user, null); //加载视图
        findView(view);//绑定视图
        return view;
    }

    //初始化视图
    private void findView(View view) {
        //绑定视图
        edit_user = (Button) view.findViewById(R.id.edit_user);
        btn_exit_user = (Button) view.findViewById(R.id.btn_exit_user);
        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_sex = (EditText) view.findViewById(R.id.et_sex);
        et_age = (EditText) view.findViewById(R.id.et_age);
        et_summary = (EditText) view.findViewById(R.id.et_summary);
        circle_image = (CircleImageView) view.findViewById(R.id.circle_image);
        tv_courier_search = (TextView) view.findViewById(R.id.tv_courier_search);
        tv_place_belong = (TextView) view.findViewById(R.id.tv_place_belong);

        //初始化Dialog
        dialog = new CustomDialog(getActivity(),
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                R.layout.dialog_photo, R.style.Theme_dialog, Gravity.BOTTOM);
        //提示框外点击无效
        dialog.setCancelable(false);

        //初始化提示框布局中的三个按钮
        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        //设置点击事件
        edit_user.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_exit_user.setOnClickListener(this);
        circle_image.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        tv_courier_search.setOnClickListener(this);
        tv_place_belong.setOnClickListener(this);

        //将储存在ShareUtils中的头像字符串转换成图片
        UtilTools.getImageToShare(getActivity(), circle_image);

        //输入框默认是不可编辑的
        setEditBoxEnabled(false);

        //获取当前用户对象
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);

        //设置各输入框的值
        et_username.setText(userInfo.getUsername()); //设置用户名
        et_age.setText(userInfo.getAge() + ""); //设置年龄（getAge返回值为int型，需转换成String）
        et_sex.setText(userInfo.isSex() ? "男" : "女"); //设置性别（原本为boolean值，true为男）
        et_summary.setText(userInfo.getSummary()); //设置简介
    }

    /**
     * 设置四个输入框是否可以编辑
     * @param isEnabled 是否可编辑
     */
    private void setEditBoxEnabled(boolean isEnabled) {
        et_username.setEnabled(isEnabled);
        et_sex.setEnabled(isEnabled);
        et_age.setEnabled(isEnabled);
        et_summary.setEnabled(isEnabled);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_user: //编辑用户资料事件
                setEditBoxEnabled(true); //设置四个输入框可编辑
                btn_confirm.setVisibility(View.VISIBLE); //设置确认修改按钮可见
                break;

            case R.id.btn_confirm: //确认修改
                //1.拿到输入框的值
                String username = et_username.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String sex = et_sex.getText().toString().trim();
                String summary = et_summary.getText().toString().trim();

                //2.判断是否为空
                if (!TextUtils.isEmpty(username) & !
                        TextUtils.isEmpty(age) & !
                        TextUtils.isEmpty(sex)) { //必填选项都不为空
                    //3.更新属性
                    MyUser user = new MyUser(); //新建用户对象
                    user.setUsername(username); //设置用户名
                    user.setAge(Integer.parseInt(age)); //设置年龄

                    //设置性别
                    if (sex.equals("男")) { //当值为true时性别为男，false为女
                        user.setSex(true);
                    } else {
                        user.setSex(false);
                    }

                    //设置简介
                    if (!TextUtils.isEmpty(summary)) { //当简介不为空时直接设置
                        user.setSummary(summary);
                    } else { //当简介为空时设置默认值
                        user.setSummary(getString(R.string.default_summary));
                    }

                    BmobUser bmobUser = BmobUser.getCurrentUser(); //获取当前用户
                    user.update(bmobUser.getObjectId(), new UpdateListener() { //执行更新事件
                        @Override
                        public void done(BmobException e) {
                            if (e == null) { //未发生更新异常
                                //修改成功
                                setEditBoxEnabled(false); //设置四个输入框不可编辑
                                btn_confirm.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), getString(R.string.change_success), Toast.LENGTH_SHORT).show();
                            } else { //发生更新异常
                                Toast.makeText(getActivity(), getString(R.string.change_fail), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else { //必填选项尚未填满时提示
                    Toast.makeText(getActivity(), getString(R.string.enter_not_null), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.circle_image: //点击圆形头像
                dialog.show(); //显示提示框
                break;

            case R.id.btn_camera: //点击提示框布局里面的拍照按钮
                toCamera(); //跳转到相机
                break;

            case R.id.btn_picture: //提示框布局里面的从图库选择按钮
                toPicture(); //跳转到相册
                break;

            case R.id.btn_cancel: //点击提示框布局里面的取消按钮
                dialog.dismiss(); //提示框消失
                break;

            case R.id.tv_courier_search: //跳转到物流查询界面
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;

            case R.id.tv_place_belong: //跳转到归属地查询界面
                startActivity(new Intent(getActivity(), PhoneActivity.class));
                break;

            case R.id.btn_exit_user://退出登陆事件
                MyUser.logOut(); //清除缓存用户对象
//                BmobUser currentUser = MyUser.getCurrentUser(); //现在的currentUser是null了
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }

    public static final String PHOTO_IMAGE_NAME = "fileImg.jpg"; //存储的照片名
    public static final int CAMERA_REQUEST_CODE = 100; //拍照请求码
    public static final int IMAGE_REQUEST_CODE = 101; //图库选择照片请求码
    public static final int RESULT_REQUEST_CODE = 102; //图库选择照片返回码
    private File tempFile = null; //临时文件

    //跳转到相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用的话执行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE); //启动相机界面
        dialog.dismiss(); //提示框消失
    }

    //跳转到相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss(); //提示框消失
    }

    @Override //用于处理拍照或选择照片后的返回数据
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != getActivity().RESULT_CANCELED) { //当返回码不为0时执行（返回码为0时为取消操作）
            switch (requestCode) {
                case IMAGE_REQUEST_CODE: //请求选择图库照片返回的数据
                    if (data == null) { //当未返回数据时
                        return;
                    }
                    startPhotoZoom(data.getData()); //裁剪图片（data.getData()为返回的数据）
                    break;

                case CAMERA_REQUEST_CODE: //请求拍照返回的数据
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_NAME); //新建文件
                    startPhotoZoom(Uri.fromFile(tempFile)); //裁剪图片（Uri.fromFile(tempFile)是将文件转换成Uri数据）
                    break;

                case RESULT_REQUEST_CODE: //界面裁剪的返回数据
                    //有可能点击舍弃
                    if (data != null) {
                        //将返回的图片数据设置到CircleImageView控件上
                        setImageToView(data);
                        //既然已经设置了图片，原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //裁剪图片
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            LogUtils.e(TAG, "startPhotoZoom:uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP"); //裁剪路径
        intent.setDataAndType(uri, "image/*"); //设置数据和类型
        intent.putExtra("crop", "true"); //设置裁剪
        intent.putExtra("aspectX", 1); //裁剪宽高比
        intent.putExtra("aspectY", 1); //裁剪宽高比
        intent.putExtra("outputX", 320); // 裁剪图片的质量（分辨率）
        intent.putExtra("outputY", 320); // 裁剪图片的质量（分辨率）
        intent.putExtra("return-data", true); //发送数据
        startActivityForResult(intent, RESULT_REQUEST_CODE); //启动裁剪
    }

    /**
     * 将返回的图片数据设置到CircleImageView控件上
     * @param data 含图片数据的Intent
     */
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras(); //从Intent中获取数据
        if (bundle != null) { //如果获取的数据不为空
            Bitmap bitmap = bundle.getParcelable("data"); //将数据转换成位图
            circle_image.setImageBitmap(bitmap); //将位图设置到CircleImageView控件
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存头像控件的图片到ShareUtils下
        UtilTools.putImageToShare(getActivity(), circle_image);
    }
}
