package cn.com.kun.foo.javacommon.lambda.optional.demo1;

import java.util.concurrent.atomic.AtomicReference;

public class OptDemoUser {

    private AtomicReference<OptDemoUser2> demoUser2AtomicReference = new AtomicReference<>(null);

    public AtomicReference<OptDemoUser2> getDemoUser2AtomicReference() {
        return demoUser2AtomicReference;
    }

    public void setDemoUser2AtomicReference(AtomicReference<OptDemoUser2> demoUser2AtomicReference) {
        this.demoUser2AtomicReference = demoUser2AtomicReference;
    }
}
