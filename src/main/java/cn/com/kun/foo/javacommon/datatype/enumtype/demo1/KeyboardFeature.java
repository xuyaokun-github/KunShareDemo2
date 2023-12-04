package cn.com.kun.foo.javacommon.datatype.enumtype.demo1;

/**
 * 参考自 com.fasterxml.jackson.databind.SerializationFeature
 *
 * author:xuyaokun_kzx
 * date:2023/12/4
 * desc:
*/
public enum KeyboardFeature implements CommonConfigFeature {

    Q_VALUE(true),
    E_VALUE(true),
    W_VALUE(true),
    R_VALUE(false)
    ;

    private final boolean _defaultState;
    private final int _mask;

    private KeyboardFeature(boolean defaultState) {
        _defaultState = defaultState;
        _mask = (1 << ordinal());
    }

    @Override
    public boolean enabledByDefault() { return _defaultState; }


    @Override
    public int getMask() { return _mask; }

    @Override
    public boolean enabledIn(int flags) { return (flags & _mask) != 0; }
}

