package co.newapp;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 类描述：
 * 创建人： QuZhiJie
 * 创建时间： 2018/6/29$
 * 版权： 成都智慧一生约科技有限公司
 */
public class KeyBoardChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "KeyBoardChangeListener";
    // Threshold for minimal keyboard height.
    final int MIN_KEYBOARD_HEIGHT_PX = 150;
    private final Rect windowVisibleDisplayFrame = new Rect();
    // Top-level window decor view.
    private View decorView;
    private int lastVisibleDecorViewHeight;
    private KeyBoardListener keyBoardListener;

    KeyBoardChangeListener(Activity activity) {
        if (activity == null) {
            Log.e(TAG, "activity is null");
            return;
        }
        decorView = activity.getWindow().getDecorView();
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

    public interface KeyBoardListener {
        /**
         * call back
         *
         * @param isShow         true is show else hidden
         * @param keyboardHeight keyboard height
         */
        void onKeyboardChange(boolean isShow, int keyboardHeight);
    }
}
