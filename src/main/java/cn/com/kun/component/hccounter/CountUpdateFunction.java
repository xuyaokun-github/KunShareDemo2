package cn.com.kun.component.hccounter;

public interface CountUpdateFunction {

    /**
     * 统计值更新
     *
     * @param countKey
     * @param count
     */
    void update(String countKey, long count);

}
