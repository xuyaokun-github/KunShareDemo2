package cn.com.kun.springframework.core.jackson.jacksonModuleParameterNamesDemo;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * 不可变类的反序列化
 *
 * 亲测成功
 *
 * author:xuyaokun_kzx
 * date:2024/9/2
 * desc:
*/
public class JacksonMixinModule extends SimpleModule {

    public JacksonMixinModule() {
        super(JacksonMixinModule.class.getName());
    }

    // 注册所有使用Mixin机制的类
    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(JmpnDemoVO.class, JmpnDemoVOMixin.class);
        // ......
    }
}
