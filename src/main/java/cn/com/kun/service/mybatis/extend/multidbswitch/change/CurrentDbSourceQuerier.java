package cn.com.kun.service.mybatis.extend.multidbswitch.change;

public interface CurrentDbSourceQuerier {

    /**
     * 查询当前所使用的数据源
     *
     * @return
     */
    String queryCurrentDbSource();

}
