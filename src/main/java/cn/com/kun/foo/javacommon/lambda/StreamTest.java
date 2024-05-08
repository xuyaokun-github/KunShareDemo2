package cn.com.kun.foo.javacommon.lambda;

import java.util.*;
import java.util.stream.Collectors;

public class StreamTest {

// +--------------------+ +------+ +------+ +---+ +-------+
// | stream of elements +-----> |filter+-> |sorted+-> |map+-> |collect|
// +--------------------+ +------+ +------+ +---+ +-------+

    public static void main(String args[]){

        List<DunkPlayerPo> list = new ArrayList<>();
        list.add(new DunkPlayerPo("小一", 10.d));
        list.add(new DunkPlayerPo("小五", 50.d));
        list.add(new DunkPlayerPo("小六", 60.d));
        list.add(new DunkPlayerPo("小6", 60.d));
        list.add(new DunkPlayerPo("小空", null));
        list.add(new DunkPlayerPo("小九", 90.d));

        long count = 0;
        List<DunkPlayerPo> filterList = null;

        // filter 过滤器的使用
        // 筛选出成绩不为空的学生人数
        count = list.stream().filter(p -> null != p.getScore()).count();
        System.out.println("参加考试的学生人数：" + count);

        // collect
        // 筛选出成绩不为空的学生集合
        filterList = list.stream().filter(p -> null != p.getScore()).collect(Collectors.toList());
        System.out.println("参加考试的学生信息：");
        filterList.stream().forEach(System.out::println);

        filterList = list.stream().filter(p -> null != p.getScore() && p.getScore() > 10000 ).collect(Collectors.toList());
        filterList.stream().forEach(System.out::println);

        // map 将集合映射为另外一个集合
        // 取出所有学生的成绩
        List<Double> scoreList = list.stream().map(p -> p.getScore()).collect(Collectors.toList());
        System.out.println("所有学生的成绩集合：" + scoreList);

        // 将学生姓名集合串成字符串，用逗号分隔
        String nameString = list.stream().map(p -> p.getName()).collect(Collectors.joining(","));
        System.out.println("所有学生的姓名字符串：" + nameString);

        // sorted排序
        // 按学生成绩逆序排序 正序则不需要加.reversed()
        filterList = list.stream().filter(p -> null != p.getScore()).sorted(Comparator.comparing(DunkPlayerPo::getScore).reversed()).collect(Collectors.toList());
        System.out.println("所有学生的成绩集合，逆序排序：");
        filterList.stream().forEach(System.out::println);

        System.out.println("按学生成绩归集：");
        Map<Double, List<DunkPlayerPo>> groupByScoreMap = list.stream().filter(p -> null != p.getScore())
                .collect(Collectors.groupingBy(DunkPlayerPo::getScore));
        for (Map.Entry<Double, List<DunkPlayerPo>> entry : groupByScoreMap.entrySet()) {
            System.out.println("成绩：" + entry.getKey() + " 人数：" + entry.getValue().size());
        }

        // forEach
        filterList.stream().forEach(p -> p.setScore(p.getScore() + 10));
        System.out.println("及格人数太少，给每个人加10分");
        filterList.stream().forEach(System.out::println);

        // count
        count = filterList.stream().filter(p -> p.getScore() >= 60).count();
        System.out.println("最后及格人数" + count);

        DoubleSummaryStatistics statistics = filterList.stream().mapToDouble(p -> p.getScore()).summaryStatistics();
        System.out.println("列表中最大的数 : " + statistics.getMax());
        System.out.println("列表中最小的数 : " + statistics.getMin());
        System.out.println("所有数之和 : " + statistics.getSum());
        System.out.println("平均数 : " + statistics.getAverage());

        // 并行流 使用
        count = list.parallelStream().filter(p -> null != p.getScore()).count();
        System.out.println("并行流处理参加考试的学生人数：" + count);

    }

}
