package cn.com.kun.springframework.core.jackson;


import cn.com.kun.springframework.springcloud.feign.vo.FeignJacksonDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.UUID;

public class TestDateSerialize {

    public static void main(String[] args) {


        FeignJacksonDO feignJacksonDO = new FeignJacksonDO();
        feignJacksonDO.setCreateTime(new Date());
        feignJacksonDO.setUuid(UUID.randomUUID().toString());

        ObjectMapper mapper = new ObjectMapper();
        String string = null;
        try {
            string = mapper.writeValueAsString(feignJacksonDO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        String string = JacksonUtils.toJSONString(feignJacksonDO);
        System.out.println(string);
    }




}
