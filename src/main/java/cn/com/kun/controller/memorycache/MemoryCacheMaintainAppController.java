package cn.com.kun.controller.memorycache;

import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.memorycache.IMemCacheDemoService;
import cn.com.kun.service.memorycache.MemCacheMaintainAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 这个用来触发 远程接口的数据变更
 * 数据变更发生时需要清空缓存
 *
 * 模拟维护方
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
@RequestMapping("/memory-cache-maintain")
@RestController
public class MemoryCacheMaintainAppController {

    /**
     * cglib代理
     */
    @Autowired
    MemCacheMaintainAppService memCacheMaintainAppService;

    /**
     * JDK代理
     */
    @Autowired
    IMemCacheDemoService memCacheDemoService;

    /**
     * http://localhost:8080/kunsharedemo/memory-other/update?id=10
     * 修改ID为10的学生信息
     * {
     *         "id": 10,
     *         "idCard": null,
     *         "studentName": "1234561625711319304",
     *         "address": "sz"
     *     }
     * @return
     */
    @PostMapping("/update")
    public ResultVo<Integer> update(@RequestBody StudentReqVO reqVO){

        Integer res = memCacheMaintainAppService.updateStudent(reqVO);
        return ResultVo.valueOfSuccess(res);
    }

    @GetMapping("/update2")
    public ResultVo<Integer> update2(){

        StudentReqVO reqVO = new StudentReqVO();
        reqVO.setId(10L);
        reqVO.setIdCard(System.currentTimeMillis()+"");
        reqVO.setCreateTime(new Date());
        Integer res = memCacheMaintainAppService.updateStudent2(reqVO);
        return ResultVo.valueOfSuccess(res);
    }


}
