package cn.com.kun.service.mybatis.impl;

import cn.com.kun.bean.entity.DeadLockDemoDO;
import cn.com.kun.bean.entity.Student;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.mapper.DeadLockDemoMapper;
import cn.com.kun.mapper.StudentMapper;
import cn.com.kun.service.StudentService;
import cn.com.kun.service.mybatis.DeadLockDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DeadLockDemoServiceImpl implements DeadLockDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeadLockDemoServiceImpl.class);

    @Autowired
    private DeadLockDemoMapper deadLockDemoMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentService studentService;

//    @Transactional(rollbackFor = Exception.class,isolation = Isolation.REPEATABLE_READ) //用可重复读，不会有死锁
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.SERIALIZABLE) //用串行化，会出现死锁
    @Override
    public void insert(DeadLockDemoDO deadLockDemoDO) {

        //先select，后update就会出现死锁
        deadLockDemoMapper.select(deadLockDemoDO);
        deadLockDemoMapper.insert(deadLockDemoDO);
    }

    @Override
    public void deleteAll() {
        deadLockDemoMapper.deleteAll();
    }

    @Transactional
    @Override
    public void updateByIdCard(String finalIdCard, String finalIdCard2) {

        long start = System.currentTimeMillis();
        LOGGER.info("更新线程 更新数据：{}", finalIdCard2);
        int res = studentMapper.updateByIdCard(UUID.randomUUID().toString(), finalIdCard2);

        ThreadUtils.sleep(5000);

        LOGGER.info("更新线程 更新数据：{}", finalIdCard);
        int res2 = studentMapper.updateByIdCard(UUID.randomUUID().toString(), finalIdCard);

    }


    /**
     *
     *
     * @param student
     * @param student2
     */
    @Transactional
    @Override
    public void saveBatch(Student student, Student student2) {

        List<Student> studentList = new ArrayList<>();
        studentList.add(student);
        List<Student> studentList2 = new ArrayList<>();
        studentList2.add(student2);
        LOGGER.info("插入线程 开始插入数据:{} ", student.getIdCard());
        for (int j = 0; j < 20; j++) {
            studentList.stream().parallel().forEach(obj -> {
                try {
                    int res = studentMapper.insert(obj);
                }catch (Exception e){
                    LOGGER.error("save异常", e);

                }
            });
        }

        ThreadUtils.sleep(5000);
        LOGGER.info("插入线程 开始插入数据:{} ", student2.getIdCard());
        for (int j = 0; j < 20; j++) {
            studentList.stream().parallel().forEach(obj -> {
                try {
                    int res = studentMapper.insert(obj);
                }catch (Exception e){
                    LOGGER.error("save异常", e);

                }
            });
        }

    }

    @Transactional
    @Override
    public void deleteLimit() {

        //加一个操作，验证普通的插入操作是否会被死锁的事务回滚影响？
//        saveStudent();
        studentService.saveStudentByIndependentTrx();

        deadLockDemoMapper.deleteLimit("kunghsu");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveStudent() {

        Student student1 = new Student();
        student1.setIdCard("idCard-0615");
        student1.setAddress(UUID.randomUUID().toString());
        student1.setStudentName("kunghsu-" + "0615demo");
        student1.setCreateTime(new Date());
        int res = studentMapper.insert(student1);
        LOGGER.info("insert操作执行成功,res:{} address:{}", res, student1.getAddress());
    }

    @Transactional
    @Override
    public void insertMany() {

        //加一个操作，验证普通的插入操作是否会被死锁的事务回滚影响？
        saveStudent();

        DeadLockDemoDO deadLockDemoDO = new DeadLockDemoDO();
        deadLockDemoDO.setVersion("1");
        deadLockDemoDO.setDemoName("kunghsu");
        deadLockDemoDO.setDemoKey(UUID.randomUUID().toString());
        DeadLockDemoDO deadLockDemoDO2 = new DeadLockDemoDO();
        deadLockDemoDO2.setVersion("1");
        deadLockDemoDO2.setDemoName("kunghsu2");
        deadLockDemoDO2.setDemoKey(UUID.randomUUID().toString());

        //保存db
        for (int i = 0; i < 1000; i++) {
            deadLockDemoDO.setDemoKey(UUID.randomUUID().toString());
            deadLockDemoDO2.setDemoKey(UUID.randomUUID().toString());
            deadLockDemoMapper.insert(deadLockDemoDO);
            deadLockDemoMapper.insert(deadLockDemoDO2);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
