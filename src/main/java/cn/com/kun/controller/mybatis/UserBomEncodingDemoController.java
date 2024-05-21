package cn.com.kun.controller.mybatis;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.foo.javacommon.string.md5.MD5EncryptUtil;
import cn.com.kun.mapper.StudentMapper;
import cn.com.kun.mapper.UserMapper;
import cn.com.kun.service.mybatis.UserService;
import cn.com.kun.springframework.springcloud.feign.client.KunShareDemo2Feign;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@RequestMapping("/user-bom-demo")
@RestController
public class UserBomEncodingDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserBomEncodingDemoController.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private KunShareDemo2Feign kunShareDemo2Feign;

    @Autowired
    private StudentMapper studentMapper;

    @GetMapping("/testBomEncodingProblem")
    public String testBomEncodingProblem() throws IOException {

        //读取文件1 和 文件2
        List<String> lineList = new ArrayList<>();

        List<String> fileContentList1 = IOUtils.readLines(new FileInputStream(new File("D:\\home\\kunghsu\\bom\\1.txt")), "UTF-8");
        List<String> fileContentList2 = IOUtils.readLines(new FileInputStream(new File("D:\\home\\kunghsu\\bom\\2.txt")), "UTF-8");

        lineList.addAll(fileContentList1);
        lineList.addAll(fileContentList2);

        lineList.stream().forEach(string->{

            LOGGER.info("文件行内容：{}", string);

            String[] strs = string.split("\\|");
            String firstName = strs[0];

            LOGGER.info("firstName：{} MD5：{}", firstName, MD5EncryptUtil.md5Encode(firstName));

            //假如存在，则不再插入，假如不存在则插入
            User userRes = userMapper.getUserByFirstname(firstName);
            if (userRes == null){
                User user = new User();
                user.setFirstname(firstName);
                user.setLastname(strs[1]);
                user.setCreateTime(new Date());
                userMapper.insert(user);
            }

        });

        return "OK";
    }

    @GetMapping("/testBomEncodingProblem2")
    public String testBomEncodingProblem2() throws IOException {

        //读取文件1 和 文件2
        List<String> lineList = new ArrayList<>();

        List<String> fileContentList1 = IOUtils.readLines(new FileInputStream(new File("D:\\home\\kunghsu\\bom\\1.txt")), "UTF-8");
        List<String> fileContentList2 = IOUtils.readLines(new FileInputStream(new File("D:\\home\\kunghsu\\bom\\2.txt")), "UTF-8");

        lineList.addAll(fileContentList1);
        lineList.addAll(fileContentList2);

        lineList.stream().forEach(string->{

            LOGGER.info("文件行内容：{}", string);
            String[] strs = string.split("\\|");
            String firstName = strs[0];


            LOGGER.info("firstName：{} MD5：{}", firstName, MD5EncryptUtil.md5Encode(firstName));
            //
            Map<String, String> map = new HashMap<>();
            map.put("firstName", firstName);
            String source = JacksonUtils.toJSONString(map);
            LOGGER.info("req json:{} MD5：{}", source, MD5EncryptUtil.md5Encode(source));

            //反序列化
            Map<String, String> newMap = JacksonUtils.toJavaObject(source, Map.class);
            String newSource = JacksonUtils.toJSONString(newMap);
            LOGGER.info("newMap json:{} MD5：{}", newSource, MD5EncryptUtil.md5Encode(newSource));


        });

        return "OK";
    }


    @GetMapping("/testBomEncodingProblem3")
    public String testBomEncodingProblem3() throws IOException {

        //读取文件1 和 文件2
        List<String> lineList = new ArrayList<>();

        List<String> fileContentList1 = IOUtils.readLines(new FileInputStream(new File("D:\\home\\kunghsu\\bom\\1.txt")), "UTF-8");
        List<String> fileContentList2 = IOUtils.readLines(new FileInputStream(new File("D:\\home\\kunghsu\\bom\\2.txt")), "UTF-8");

        lineList.addAll(fileContentList1);
        lineList.addAll(fileContentList2);

        lineList.stream().forEach(string->{

            String[] strs = string.split("\\|");
            String firstName = strs[0];

            if (firstName.contains("AAA")){

                //
                LOGGER.info("文件行内容：{}", string);


                LOGGER.info("firstName：{} MD5：{}", firstName, MD5EncryptUtil.md5Encode(firstName));
                //
                User user = new User();
                user.setFirstname(firstName);
                String source = JacksonUtils.toJSONString(user);
                LOGGER.info("req json:{} MD5：{}", source, MD5EncryptUtil.md5Encode(source));

                //反序列化
                User newMap = JacksonUtils.toJavaObject(source, User.class);
                String newSource = JacksonUtils.toJSONString(newMap);
                LOGGER.info("newMap json:{} MD5：{}", newSource, MD5EncryptUtil.md5Encode(newSource));

                //请求接口
                kunShareDemo2Feign.testBomEncodingForPost(user.getFirstname());

            }

        });

        return "OK";
    }


    @GetMapping("/testBomEncodingProblem4")
    public String testBomEncodingProblem4() throws IOException {

        List<User> userList = userMapper.query(new HashMap());

        userList.forEach(user -> {

            //查询数据库
            if (user.getFirstname().contains("AAA")){
                LOGGER.info("firstName：{} MD5：{}", user.getFirstname(), MD5EncryptUtil.md5Encode(user.getFirstname()));

                //验证使用两个不同的“AAA”，查student表
                Map<String, String> queryMap = new HashMap<>();
                queryMap.put("idCard", user.getFirstname());
                List<Student> studentList = studentMapper.query(queryMap);
                if (!CollectionUtils.isEmpty(studentList)){
                    LOGGER.info("数据库查询studentList size：{}", studentList.size());
                    Student student = studentList.get(0);
                    LOGGER.info("数据库查询后student IdCard：{} MD5：{}", student.getIdCard(), MD5EncryptUtil.md5Encode(student.getIdCard()));
                }
            }
        });

        return "OK";
    }

    @PostMapping("/testBomEncodingForPost")
    public ResultVo testBomEncodingForPost(@RequestBody String firstname) throws IOException {


        User user = userService.queryUserByFirstName(firstname);
        String userStirng = JacksonUtils.toJSONString(user);
        LOGGER.info("接口服务层接收到的user:{} MD5：{}", userStirng, MD5EncryptUtil.md5Encode(userStirng));

        return ResultVo.valueOfSuccess("OK");
    }

}
