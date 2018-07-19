package co.newapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import java.lang.reflect.Method;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;

/**
 * 类描述：解决全屏模式下AdjustResize无效的PopupWindow
 * 创建人： QuZhiJie
 * 创建时间： 2018/7/2$
 * 版权： 成都智慧一生约科技有限公司
 */
public class AdjustResizePopupWindow extends PopupWindow {
    private Activity activity;
    private View view;
    private ViewGroup viewGroup;
    private int indexOfView;

    public AdjustResizePopupWindow(Activity activity) {
        this.activity = activity;
    }

    public AdjustResizePopupWindow assistView(View view) {
        this.view = view;
        initView();
        return this;
    }

    private void initView() {
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                hideSoftInput(activity, view);
            }
        });
    }

    public void showPop() {
//        if (view == null) return;

//        if (view.getParent() != null) {
//            viewGroup = ((ViewGroup) view.getParent());
//            indexOfView = viewGroup.indexOfChild(view);
//            viewGroup.removeView(view);
//        }

        View contentView = View.inflate(activity,R.layout.layout_bottom_pop, null);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setContentView(contentView);
        setFocusable(true);
        setOutsideTouchable(false);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        //popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE);
        showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

        contentView.findViewById(R.id.pop_edit).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    toggleSoftInput();
                }
                return false;
            }
        });
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                activity.dispatchTouchEvent(motionEvent);
                return false;
            }
        });
    }

    /**
     * 动态隐藏软键盘
     *
     * @param context 上下文
     * @param view    视图
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 切换键盘显示与否状态
     */
    private void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    static class KeyBoardChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private static final String TAG = "KeyBoardChangeListener";
        // Threshold for minimal keyboard height.
        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        private final Rect windowVisibleDisplayFrame = new Rect();
        // Top-level window decor view.
        private View decorView;
        private int lastVisibleDecorViewHeight;
        private KeyBoardListener keyBoardListener;

        KeyBoardChangeListener(View view) {
            decorView = view;
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        public void setKeyBoardListener(KeyBoardListener keyBoardListener) {
            this.keyBoardListener = keyBoardListener;
        }

        @Override
        public void onGlobalLayout() {
            // Retrieve visible rectangle inside window.
            decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
            final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();
            Log.e("qzj","visibleDecorViewHeight:"+visibleDecorViewHeight);
            int[] outLocation = new int[2];
            decorView.getLocationOnScreen(outLocation);
            Log.e("qzj","location x:"+outLocation[0]+",location Y:"+outLocation[1]);
            // Decide whether keyboard is visible from changing decor view height.
            if (lastVisibleDecorViewHeight != 0) {
                if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                    // Calculate current keyboard height (this includes also navigation bar height when in fullscreen mode).
                    int currentKeyboardHeight = decorView.getHeight() - windowVisibleDisplayFrame.bottom;
                    // Notify listener about keyboard being shown.
                    if (keyBoardListener != null) {
                        keyBoardListener.onKeyboardChange(true, currentKeyboardHeight);
                    }
                } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                    // Notify listener about keyboard being hidden.
                    if (keyBoardListener != null) {
                        keyBoardListener.onKeyboardChange(false, 0);
                    }
                }
            }
            // Save current decor view height for the next call.
            lastVisibleDecorViewHeight = visibleDecorViewHeight;
        }

        interface KeyBoardListener {
            void onKeyboardChange(boolean isShow, int keyboardHeight);
        }
    }
}
