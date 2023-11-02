package cn.com.kun.foo.javacommon.collections.map;

import java.util.*;
import java.util.stream.Collectors;

public class TestMapSort {


    public static void main(String[] args) {

        Map<String, Integer> unsortMap = new HashMap<>();
        unsortMap.put("A", 1);
        unsortMap.put("B", 10);
        unsortMap.put("C", 2);
        unsortMap.put("D", 6);


        List<String> keyList = unsortMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
        System.out.println(keyList.size());


//        Map<String, Integer> result = unsortMap.entrySet().stream()
//                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
//                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
//        System.out.println(result.size());
//
//        Map<String, Integer> result2 = new LinkedHashMap<>();
//        unsortMap.entrySet().stream()
//                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//                .forEachOrdered(x -> result2.put(x.getKey(), x.getValue()));
//
//        System.out.println(result2.size());

    }



}
