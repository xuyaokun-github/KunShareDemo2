package cn.com.kun.framework.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/1/21
 * desc:
*/
public class NameUtilInputData {

    // 根据Excel中指定列名或列的索引读取
    @ExcelProperty(value = "五行类型", index = 0)
    private String wuXingType;

    @ExcelProperty(value = "字集合", index = 1)
    private String words;

    @ExcelProperty(value = "是否主属性(Y/N)", index = 2)
    private String mainTypeFlag;

    @ExcelProperty(value = "姓", index = 3)
    private String familyName;

    private List<String> allWordList;

    public String getWuXingType() {
        return wuXingType;
    }

    public void setWuXingType(String wuXingType) {
        this.wuXingType = wuXingType;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getMainTypeFlag() {
        return mainTypeFlag;
    }

    public void setMainTypeFlag(String mainTypeFlag) {
        this.mainTypeFlag = mainTypeFlag;
    }

    public List<String> getAllWordList() {
        return allWordList;
    }

    public void setAllWordList(List<String> allWordList) {
        this.allWordList = allWordList;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void splitAllWord() {

        allWordList = new ArrayList<>();
        if (words != null && words.length() > 0){
            String[] wordArr = words.split(" ");
            for (String word : wordArr){
                if (StringUtils.isNotBlank(word.trim())){
                    allWordList.add(word.trim());
                }
            }
        }
    }


}
