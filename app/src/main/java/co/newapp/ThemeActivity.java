package co.newapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import skin.support.SkinCompatManager;

/**
 * 类描述： 主题
 * 创建人： QuZhiJie
 * 创建时间： 2018/6/13
 * 版权： 成都智慧一生约科技有限公司
 */
public class ThemeActivity extends AppCompatActivity {

    private Button exchangeBtn;
    private TextView textView;
    private ImageView imageView;

    private boolean themeNight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        exchangeBtn = findViewById(R.id.theme_exchange);
        textView = findViewById(R.id.theme_txt);
        imageView = findViewById(R.id.theme_img);

        exchangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeNight = !themeNight;
                if (themeNight) {
                    // 后缀加载
                    // SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                    //
                    SkinCompatManager.getInstance().loadSkin("night.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                }else {
                    // 恢复应用默认皮肤
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                }
            }
        });
    }
}
