package cn.com.kun.springframework.batch.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BatchDemoUtils {

    private static List<String> stringList = new ArrayList<>();

    /**
     * 启动job前调用
     */
    public static void init(){

        stringList.clear();
    }


    public static void addLineNumber(String lineNumber) {

        stringList.add(lineNumber);
    }


    public static void showLineCompare() {

        Set<String> set = new HashSet<>(stringList);
        if (set.size() != stringList.size()){
            //出现行号重复
            System.out.println("出现行号重复");

            //找到重复的行号
            for (String str : set){

                int count = 0;
                for (int i = 0; i < stringList.size(); i++) {
                    if (str.equals(stringList.get(i))){
                        count++;
                    }
                }
                if (count > 1){
                    System.out.println("重复行：" + str);
                }
            }
        }
    }
}
