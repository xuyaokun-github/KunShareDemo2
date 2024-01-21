package cn.com.kun.framework.excel.nameutils;

import cn.com.kun.framework.excel.NameUtilInputData;
import cn.com.kun.framework.excel.NameUtilOutputData;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.util.*;
import java.util.stream.Collectors;

public class NameUtilsV2 {

    public static void main(String[] args) {

        Map<String, List<String>> wordNamesMap = read("D:\\home\\kunghsu\\name\\word_v2.xlsx");
        writeExcel(wordNamesMap, "D:\\home\\kunghsu\\name\\names.xlsx");
    }

    /**
     *
     * @param wordNamesMap
     * @param targetFilePath
     */
    private static void writeExcel(Map<String, List<String>> wordNamesMap, String targetFilePath) {

        String filename = targetFilePath;
        // 创建ExcelWriter对象
        ExcelWriter excelWriter = EasyExcel.write(filename, NameUtilOutputData.class).build();
        List<String> list = new ArrayList<>(wordNamesMap.keySet());
        // 向Excel的同一个Sheet重复写入数据
        for (int i = 0; i < list.size(); i++) {
            // 创建Sheet对象
            WriteSheet writeSheet = EasyExcel.writerSheet(list.get(i)).build();
            List<NameUtilOutputData> nameUtilOutputDataList = getNameUtilOutputData(list.get(i), wordNamesMap.get(list.get(i)));
            excelWriter.write(nameUtilOutputDataList, writeSheet);
        }
        // 关闭流
        excelWriter.finish();
    }

    private static List<NameUtilOutputData> getNameUtilOutputData(String word, List<String> names) {

        List<NameUtilOutputData> nameUtilOutputDataList = new ArrayList<>();

        names.forEach(name->{

            NameUtilOutputData nameUtilOutputData = new NameUtilOutputData();
            nameUtilOutputData.setWord(word);
            nameUtilOutputData.setName(name);
            nameUtilOutputDataList.add(nameUtilOutputData);
        });

        return nameUtilOutputDataList;
    }



    public static Map<String, List<String>> read(String sourceFilePath) {

        List<NameUtilInputData> inputDataList = new ArrayList<>();
        // 读取excel
        EasyExcel.read(sourceFilePath, NameUtilInputData.class, new AnalysisEventListener<NameUtilInputData>() {
            // 每解析一行数据,该方法会被调用一次
            @Override
            public void invoke(NameUtilInputData inputData, AnalysisContext analysisContext) {
                System.out.println("解析数据为:" + inputData.toString());
                inputDataList.add(inputData);
            }
            // 全部解析完成被调用
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("解析完成...");
                // 可以将解析的数据保存到数据库
            }
        }).sheet().doRead();


        //得到全部主属性
        Set<String> mainTypeSet = inputDataList.stream().filter(obj-> "Y".equals(obj.getMainTypeFlag())).map(NameUtilInputData::getWuXingType).collect(Collectors.toSet());

        inputDataList.forEach(obj->{
            obj.splitAllWord();
            showAllStringList(obj.getAllWordList());
            System.out.println(String.format("五行为%s的字有%s个", obj.getWuXingType(), obj.getAllWordList().size()));
        });

        List<String> allNameList = new ArrayList<>();
        Map<String, List<String>> wordNamesMap = new HashMap<>();
        for (int i = 0; i < inputDataList.size(); i++) {
            NameUtilInputData inputData1 = inputDataList.get(i);
            boolean isMainType1 = mainTypeSet.contains(inputData1.getWuXingType());
            for (int j = 0; j < inputDataList.size(); j++) {
                NameUtilInputData inputData2 = inputDataList.get(j);
                boolean isMainType2 = mainTypeSet.contains(inputData2.getWuXingType());
                if (isMainType1 || isMainType2){
                    //添加
                    addName(allNameList, wordNamesMap, inputData1.getAllWordList(), inputData2.getAllWordList(), inputData1.getFamilyName(),
                            inputData1.getWuXingType(), inputData2.getWuXingType());
                }

            }
        }
        System.out.println("输出全部名字：");
        showAllStringList(allNameList);

        return wordNamesMap;
    }

    private static void addName(List<String> allNameList, Map<String, List<String>> wordNamesMap, List<String> allWordList, List<String> allWordList1, String familyName,
                                String wuXingType, String wuXingType2) {

        for (int i = 0; i < allWordList.size(); i++) {
            for (int j = 0; j < allWordList1.size(); j++) {
                allNameList.add(familyName + allWordList.get(i) + allWordList1.get(j));
                String word = allWordList.get(i);
                String wordKey = allWordList.get(i) + "(" + wuXingType + "+" + wuXingType2 + ")";
                List<String> names = wordNamesMap.get(wordKey);
                if (names == null){
                    names = new ArrayList<>();
                    wordNamesMap.put(wordKey, names);
                }
                names.add(familyName + word + allWordList1.get(j));
            }
        }
    }

    private static void showAllStringList(List<String> allWordList) {

        StringBuilder builder = new StringBuilder();
        allWordList.forEach(word->{
            builder.append(word + ",");
        });
        String str = builder.toString();
        System.out.println(str.substring(0, str.length()-1));
    }

}
