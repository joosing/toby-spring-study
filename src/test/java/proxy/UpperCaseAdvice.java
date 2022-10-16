package proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

class UpperCaseAdvice implements MethodInterceptor {

    // MethodInvocation
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String ret = (String) invocation.proceed();
        assert ret != null;
        return ret.toUpperCase();
    }
}