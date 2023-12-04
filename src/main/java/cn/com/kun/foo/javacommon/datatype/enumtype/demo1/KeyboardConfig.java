package cn.com.kun.foo.javacommon.datatype.enumtype.demo1;

public class KeyboardConfig {

    private final static int KEYBOARD_FEATURE_DEFAULTS = collectFeatureDefaults(KeyboardFeature.class);

    protected final int _keyboardFeatures;

    public KeyboardConfig() {

        _keyboardFeatures = KEYBOARD_FEATURE_DEFAULTS;
    }

    /**
     * Method that calculates bit set (flags) of all features that
     * are enabled by default.
     */
    public static <F extends Enum<F> & CommonConfigFeature> int collectFeatureDefaults(Class<F> enumClass)
    {
        int flags = 0;
        for (F value : enumClass.getEnumConstants()) {
            if (value.enabledByDefault()) {
                flags |= value.getMask();
            }
        }
        return flags;
    }

    public final boolean isEnabled(KeyboardFeature f) {
        return (KEYBOARD_FEATURE_DEFAULTS & f.getMask()) != 0;
    }


}
