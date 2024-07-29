package cn.com.kun.framework.schedule.xxljob.extend;

import com.xxl.job.core.biz.model.ReturnT;

import static com.xxl.job.core.biz.model.ReturnT.FAIL_CODE;
import static com.xxl.job.core.biz.model.ReturnT.SUCCESS_CODE;

public class ReturnTHelper {

    public static ReturnT<String> buildSuccess(String msg) {
        ReturnT<String> result = new ReturnT<>(SUCCESS_CODE, msg);
        return result;
    }

    public static ReturnT<String> buildFail(String msg) {
        ReturnT<String> result = new ReturnT<>(FAIL_CODE, msg);
        return result;
    }

}
