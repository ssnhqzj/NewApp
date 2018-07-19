package co.newapp;

import android.app.Application;
import android.content.Context;

import com.bilibili.magicasakura.utils.ThemeUtils;

import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * 类描述：
 * 创建人： QuZhiJie
 * 创建时间： 2018/6/19$
 * 版权： 成都智慧一生约科技有限公司
 */
public class MyApplication extends Application implements ThemeUtils.switchColor {

    @Override
    public void onCreate() {
        super.onCreate();
        ThemeUtils.setSwitchColor(this);
        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addStrategy(new CustomSDCardLoader())
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
    }

    @Override
    public int replaceColorById(Context context, int colorId) {
        return 0;
    }

    @Override
    public int replaceColor(Context context, int color) {
        return 0;
    }
}
