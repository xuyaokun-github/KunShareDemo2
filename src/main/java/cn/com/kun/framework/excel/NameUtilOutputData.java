package cn.com.kun.framework.excel;

import com.alibaba.excel.annotation.ExcelProperty;

public class NameUtilOutputData {

    @ExcelProperty(value = "字",index = 0)
    private String word;
    @ExcelProperty(value = "名",index = 1)
    private String name;

    public NameUtilOutputData() {
    }

    public NameUtilOutputData(String word, String name) {
        this.word = word;
        this.name = name;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
