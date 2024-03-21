package cn.com.kun.framework.apache.skywalking.extend;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

import java.util.UUID;

/**
 *
 * 假如log4j2配置文件中不含 %traceId2关键字，插件不会生效
 *
 * author:xuyaokun_kzx
 * date:2024/3/8
 * desc:
*/
@Plugin(
        name = "KunghsuTraceIdConverter",
        category = "Converter"
)
//假如这里也用traceId，会将skywalking的TraceIdConverter覆盖掉，不能这样。
@ConverterKeys({"\\$traceId2"})
public class KunghsuTraceIdConverter extends LogEventPatternConverter {
    protected KunghsuTraceIdConverter(String name, String style) {
        super(name, style);
    }

    public static KunghsuTraceIdConverter newInstance(String[] options) {
        //假如这里也用traceId，会将skywalking的TraceIdConverter覆盖掉，不能这样。
        //但是改成traceId2 ，这个又无法生效
        return new KunghsuTraceIdConverter("traceId2", "traceId2");
    }

    public void format(LogEvent event, StringBuilder toAppendTo) {

        try {
            String str = toAppendTo.toString();
            if (str.contains("TID: N/A")){
                str = str.replace("TID: N/A", UUID.randomUUID().toString());
                int index = toAppendTo.indexOf("TID: N/A");
//                toAppendTo.append(UUID.randomUUID().toString(), index, index+32);
            }
        }catch (Exception e){

        }
    }
}


