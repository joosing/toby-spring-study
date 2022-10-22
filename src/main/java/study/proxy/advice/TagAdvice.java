package study.proxy.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TagAdvice implements MethodInterceptor {
    private String tag;

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String ret = (String) invocation.proceed();
        assert ret != null;
        return tag + ret;
    }
}
