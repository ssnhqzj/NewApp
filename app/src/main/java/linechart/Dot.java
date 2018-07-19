package linechart;

import android.content.Context;
import android.graphics.Point;

/**
 * 类描述： 点
 * 创建人： QuZhiJie
 * 创建时间： 2018/7/17$
 * 版权： 成都智慧一生约科技有限公司
 */
public class Dot {
    int x;
    float y;
    float data;
    int targetX;
    float targetY;
    int linenumber;
    int velocity;

    Dot(Context context, int x, float y, int targetX, float targetY, float data, int linenumber) {
        this.x = x;
        this.y = y;
        this.linenumber = linenumber;
        velocity = MyUtils.dip2px(context, 18);
        setTargetData(targetX, targetY, data, linenumber);
    }

    Point setupPoint(Point point) {
        point.set(x, (int) y);
        return point;
    }

    Dot setTargetData(int targetX, float targetY, float data, int linenumber) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.data = data;
        this.linenumber = linenumber;
        return this;
    }

    boolean isAtRest() {
        return (x == targetX) && (y == targetY);
    }

    void update() {
        x = (int) updateSelf(x, targetX, velocity);
        y = updateSelf(y, targetY, velocity);
    }

    private float updateSelf(float origin, float target, int velocity) {
        if (origin < target) {
            origin += velocity;
        } else if (origin > target) {
            origin -= velocity;
        }
        if (Math.abs(target - origin) < velocity) {
            origin = target;
        }
        return origin;
    }
}
