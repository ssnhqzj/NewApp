package co.newapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

/**
 * 类描述： 日期Long格式化
 * 创建人： QuZhiJie
 * 创建时间： 2018/6/13
 * 版权： 成都智慧一生约科技有限公司
 */
public class DateConvertActivity extends AppCompatActivity {

    EditText timeET;
    EditText dateET;
    Button convertET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_convert);

        /*WaterCleanChatView waterView = findViewById(R.id.water_chat_view);
        waterView.setArcWidth(SizeUtils.dp2px(this,6));*/

        timeET = findViewById(R.id.time);
        dateET = findViewById(R.id.date);
        convertET = findViewById(R.id.convert);
        convertET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = Long.parseLong(timeET.getText().toString());
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(time);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int h = cal.get(Calendar.HOUR_OF_DAY);
                int m = cal.get(Calendar.MINUTE);
                int s = cal.get(Calendar.SECOND);

                String date = String.format(Locale.getDefault(),"%d-%d-%d %d:%d:%d",
                        year,
                        month,
                        day,
                        h,
                        m,
                        s);
                dateET.setText(date);
            }
        });
    }
}
