package cn.com.kun.elasticsearch.controller;

import cn.com.kun.bean.model.EsDemoReqVO;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.elasticsearch.entity.NbaPlayerDocBean;
import cn.com.kun.elasticsearch.service.INbaPlayerElasticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RequestMapping("/elasticsearch")
@RestController
public class ElasticsearchDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ElasticsearchDemoController.class);

    @Autowired
    private INbaPlayerElasticService nbaPlayerElasticService;

    @RequestMapping("/createIndex")
    public ResultVo createIndex(){

        nbaPlayerElasticService.createIndex();
        return ResultVo.valueOfSuccess("");
    }

    @RequestMapping("/save")
    public ResultVo save(){

        NbaPlayerDocBean docBean = new NbaPlayerDocBean();
        //假如不设置id,会默认生成，ID默认是“null”
//        docBean.setId(11L);
        docBean.setType(111);
        docBean.setContent("11111111111string......");
        docBean.setFirstCode(docBean.getId() + "FirstCode" + System.currentTimeMillis());
        docBean.setSecordCode(docBean.getId() + "SecordCode" + System.currentTimeMillis());
        //为什么这里无ID的情况下保存，只会保存一次？无论调多少次，数据库里只有一条记录，后面保存的会覆盖前面的
        nbaPlayerElasticService.save(docBean);

        //很奇怪，这里假如传了新的ID，才会创建新记录。。为什么？？
        NbaPlayerDocBean docBean2 = new NbaPlayerDocBean("XX0258","XX8097","xxxxxxxxxxxxxxxxxx", 111);
        docBean2.setId(System.currentTimeMillis());
//        nbaPlayerElasticService.save(docBean2);

        return ResultVo.valueOfSuccess("");
    }


    @RequestMapping("/saveBigContent")
    public ResultVo saveBigContent(@RequestBody EsDemoReqVO esDemoReqVO){

        String content = esDemoReqVO.getContent();
        int bytes = content.getBytes(StandardCharsets.UTF_8).length;
        LOGGER.info("大报文字节数：{} 换算即{}M", bytes, bytes/1024/1024);
        LOGGER.info("大报文字符长度：{}", content.length());

        NbaPlayerDocBean docBean = new NbaPlayerDocBean();
        //假如不设置id,会默认生成，ID默认是“null”
//        docBean.setId(11L);
        docBean.setType(111);
        docBean.setContent(content);//text类型字段，无长度限制
        docBean.setFirstCode(content);//keyword字段，有长度限制
        docBean.setSecordCode(docBean.getId() + "SecordCode" + DateUtils.now());

        try {
            nbaPlayerElasticService.save(docBean);
        }catch (Exception e){
            e.printStackTrace();
        }

        return ResultVo.valueOfSuccess("");
    }


}
