package linchange.example.com.supergreattest.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import linchange.example.com.supergreattest.R;

/**
 * Created by Lin Change on 2017-02-19.
 */
//自定义Dialog
public class CustomDialog extends Dialog {
    //定义模版
    public CustomDialog(Context context, int layout, int style) {
        this(context, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layout, style, Gravity.CENTER);
    }

    //定义属性
    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity, int anim) {
        super(context, style);
        //设置属性
        setContentView(layout); //设置界面样式

        Window window = getWindow(); // 得到窗口
        WindowManager.LayoutParams layoutParams = window.getAttributes(); //得到窗口的属性
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams); //把修改过的属性设置回窗口
        window.setWindowAnimations(anim); //为窗口设置动画效果
    }

    //实例
    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity) {
        this(context, width, height, layout, style, gravity, R.style.pop_anim_style);
    }

}
