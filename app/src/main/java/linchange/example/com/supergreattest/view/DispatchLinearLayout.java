package linchange.example.com.supergreattest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

/**
 * Created by Lin Change on 2017-02-25.
 */
//事件分发布局类
public class DispatchLinearLayout extends LinearLayout {

    private DispatchKeyEventListener dispatchKeyEventListener; //分发事件接听器

    public DispatchLinearLayout(Context context) {
        super(context);
    }

    public DispatchLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DispatchKeyEventListener getDispatchKeyEventListener() {
        return dispatchKeyEventListener;
    }

    public void setDispatchKeyEventListener(DispatchKeyEventListener dispatchKeyEventListener) {
        this.dispatchKeyEventListener = dispatchKeyEventListener;
    }

    //分发键盘事件接口
    public static interface DispatchKeyEventListener {
        boolean dispatchKeyEvent(KeyEvent event); //如果返回值为true交给service处理，为false则此事件自己处理
    }

    //重写父类ViewGroup的键盘事件分发方法
    //如果返回值为true交给service处理，为false则此事件自己处理
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //如果不为空，说明被调用了，去获取事件
        if (dispatchKeyEventListener != null) {
            return dispatchKeyEventListener.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }
}
