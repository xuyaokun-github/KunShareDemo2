package cn.com.kun.service.memorycache;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.component.memorycache.maintain.MemoryCacheNoticeProcessor;
import cn.com.kun.component.memorycache.maintain.annotation.EvictCacheNotice;
import cn.com.kun.mapper.StudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.com.kun.common.constants.MemoryCacheConfigConstants.MEMORY_CACHE_CONFIG_NAME_STUDENT;
import static cn.com.kun.common.constants.MemoryCacheConfigConstants.MEMORY_CACHE_CONFIG_NAME_STUDENT_2;

/**
 * 模拟内存缓存机制的维护方app
 * author:xuyaokun_kzx
 * date:2021/7/8
 * desc:
*/
@Service
public class MemCacheMaintainAppService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemCacheMaintainAppService.class);

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private MemoryCacheNoticeProcessor memoryCacheNoticeProcessor;

    /**
     * 改成注解进行刷新处理
     *
     * @param reqVO
     * @return
     */
    @EvictCacheNotice(configName = "memorycache-student-service", key = "#reqVO.id.toString()")
    public Integer updateStudent(StudentReqVO reqVO) {

        LOGGER.info("我是维护方服务，开始更新学生信息");
        Student student = new Student();
        BeanUtils.copyProperties(reqVO, student);
        return studentMapper.update(student);
    }

    public Integer updateStudent2(StudentReqVO reqVO) {

        //业务逻辑
        LOGGER.info("开始更新学生信息");
        memoryCacheNoticeProcessor.notice(MEMORY_CACHE_CONFIG_NAME_STUDENT, reqVO.getId().toString());
        memoryCacheNoticeProcessor.notice(MEMORY_CACHE_CONFIG_NAME_STUDENT_2);

        return 1;
    }
}
