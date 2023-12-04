package cn.com.kun.foo.javacommon.datatype.enumtype.demo1;

public interface CommonConfigFeature {

    /**
     * Accessor for checking whether this feature is enabled by default.
     */
    public boolean enabledByDefault();

    /**
     * Returns bit mask for this feature instance
     */
    public int getMask();

    /**
     * Convenience method for checking whether feature is enabled in given bitmask
     *
     * @since 2.6
     */
    public boolean enabledIn(int flags);
}

