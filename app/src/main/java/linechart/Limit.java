package linechart;

import android.support.annotation.NonNull;

/**
 * 类描述： 限制
 * 创建人： QuZhiJie
 * 创建时间： 2018/7/17$
 * 版权： 成都智慧一生约科技有限公司
 */
public class Limit implements Comparable<Limit>{
    public float yValue;
    public String desc;
    public int color;

    public Limit() {}

    public Limit(float yValue, String desc) {
        this.yValue = yValue;
        this.desc = desc;
    }

    public Limit(float yValue, String desc, int color) {
        this.yValue = yValue;
        this.desc = desc;
        this.color = color;
    }

    @Override
    public int compareTo(@NonNull Limit other) {
        return Float.compare(yValue, other.yValue);
    }
}
