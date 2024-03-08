package cn.com.kun.springframework.batch.common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * Spring Batch工具类
 *
 * author:xuyaokun_kzx
 * date:2024/3/7
 * desc:
*/
public class BatchUtils {

    /**
     * 获取文件总行数
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static long getFileLineCount(String fileName) throws IOException {

        /**
         * 不支持resource下的文件
         */
        try (Stream stream = Files.lines(Paths.get(fileName))) {

            return stream.count();
        }

    }

    public static int getFileLineCountByIOCommons(String fileName) throws IOException {

        List lines = FileUtils.readLines(new File(fileName), "UTF-8");
        return lines.size();

    }

}
