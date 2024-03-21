package cn.com.kun.foo.javacommon.collections.list;

import java.util.ArrayList;
import java.util.List;

public class TestList {


    public static void main(String[] args) {

        List<String> stringList = null;
        stringList = method(stringList);
        System.out.println(stringList.size());
    }

    private static List<String> method(List<String> stringList) {

        if (stringList == null){
            stringList = new ArrayList<>();
            stringList.add("kunghsu");
        }
        return stringList;
    }
}
