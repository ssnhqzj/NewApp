package co.newapp;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;

/**
 * 类描述： 全屏界面resize
 * 创建人： QuZhiJie
 * 创建时间： 2018/6/13
 * 版权： 成都智慧一生约科技有限公司
 */
public class KeyboardActivity extends AppCompatActivity {

    private FrameLayout tbLayout;
    private RelativeLayout keyboardLayout;
    private LinearLayout bottomLayout;
    private ScrollView scrollView;
    private EditText editText;
    private AdjustResizePopupWindow adjustResizePop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        //AndroidBug5497Workaround.assistActivity(this);

        tbLayout = findViewById(R.id.bottom_toolbar);
        editText = findViewById(R.id.bottom_et);
        bottomLayout = findViewById(R.id.bottom_edit_layout);
        scrollView = findViewById(R.id.scroll_view);
        keyboardLayout = findViewById(R.id.keyboard_layout);
        tbLayout.post(new Runnable() {
            @Override
            public void run() {
                new AdjustResizePopupWindow(KeyboardActivity.this).assistView(tbLayout).showPop();
            }
        });

        //hideSystemNavigationBar();
        alwaysHideNavigationBar();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void hideSystemNavigationBar() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View view = this.getWindow().getDecorView();
            view.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void alwaysHideNavigationBar(){
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
    }

}
