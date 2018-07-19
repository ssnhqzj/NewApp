package linechart;

/**
 * 类描述： 折线图单点数据对象
 * 创建人： QuZhiJie
 * 创建时间： 2018/7/18$
 * 版权： 成都智慧一生约科技有限公司
 */
public class LineEntity {
    // X轴上的文字
    public String xAxisText;
    // Y轴上的值
    public float yValue;

    public LineEntity() {}

    public LineEntity(String xAxisText, float yValue) {
        this.xAxisText = xAxisText;
        this.yValue = yValue;
    }
}
