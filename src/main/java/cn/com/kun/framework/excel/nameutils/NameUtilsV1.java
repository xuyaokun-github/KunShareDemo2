package cn.com.kun.framework.excel.nameutils;

import cn.com.kun.framework.excel.NameUtilOutputData;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class NameUtilsV1 {

    public static void main(String[] args) {

        Map<String, List<String>> wordNamesMap = read("D:\\home\\kunghsu\\name\\word.xlsx");
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

        List<String> allWordList = new ArrayList<>();
        List<String> familyNameList = new ArrayList<>();

        // 创建ExcelReaderBuilder对象
        ExcelReaderBuilder readerBuilder = EasyExcel.read();
        // 获取文件对象
        readerBuilder.file(sourceFilePath);
        // 指定映射的数据模板
        //  readerBuilder.head(DemoData.class);
        // 指定sheet
        readerBuilder.sheet(0);
        // 自动关闭输入流
        readerBuilder.autoCloseStream(true);
        // 设置Excel文件格式
        readerBuilder.excelType(ExcelTypeEnum.XLSX);
        // 注册监听器进行数据的解析
        readerBuilder.registerReadListener(new AnalysisEventListener() {
            // 每解析一行数据,该方法会被调用一次
            @Override
            public void invoke(Object object, AnalysisContext analysisContext) {

                // 如果没有指定数据模板, 解析的数据会封装成 LinkedHashMap返回
                // demoData instanceof LinkedHashMap 返回 true
                if (object instanceof LinkedHashMap){
                    System.out.println("invoke返回对象为LinkedHashMap类型");
                    LinkedHashMap hashMap = (LinkedHashMap) object;
                    String wordList = (String) hashMap.get(0);
                    familyNameList.add((String) hashMap.get(1));
                    String[] words = wordList.split(" ");
                    for (String word : words){
                        if (StringUtils.isNotBlank(word.trim())){
                            allWordList.add(word.trim());
                        }
                    }
                }

                System.out.println("解析数据为:" + object.toString());
            }

            // 全部解析完成被调用
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                System.out.println("解析完成...");
                // 可以将解析的数据保存到数据库
            }
        });
        readerBuilder.doReadAll();

        //展示所有字
        System.out.println("输出全部待选字：");
        showAllStringList(allWordList);

        List<String> allNameList = new ArrayList<>();
        Map<String, List<String>> wordNamesMap = new HashMap<>();
        for (int i = 0; i < allWordList.size(); i++) {
            for (int j = 0; j < allWordList.size(); j++) {
                allNameList.add(familyNameList.get(0) + allWordList.get(i) + allWordList.get(j));
                String word = allWordList.get(i);
                List<String> names = wordNamesMap.get(word);
                if (names == null){
                    names = new ArrayList<>();
                    wordNamesMap.put(word, names);
                }
                names.add(familyNameList.get(0) + word + allWordList.get(j));
            }
        }

        System.out.println("输出全部名字：");
        showAllStringList(allNameList);

        return wordNamesMap;
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
