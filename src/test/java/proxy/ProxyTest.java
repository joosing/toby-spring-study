package proxy;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

public class ProxyTest {
    @Test
    public void simpleProxy() {
        Hello hello = new HelloUppercase(new HelloTarget());
        Assertions.assertEquals("HELLO TOBY", hello.sayHello("Toby"));
        Assertions.assertEquals("HI TOBY", hello.sayHi("Toby"));
        Assertions.assertEquals("THANK YOU TOBY", hello.sayThankYou("Toby"));
    }

    @Test
    public void dynamicProxy() {
        Hello hello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UpperCaseHandler(new HelloTarget())
        );
        Assertions.assertEquals("HELLO TOBY", hello.sayHello("Toby"));
        Assertions.assertEquals("HI TOBY", hello.sayHi("Toby"));
        Assertions.assertEquals("THANK YOU TOBY", hello.sayThankYou("Toby"));
    }

    /**
     * 스프링의 다이나믹 프록시 기능(ProxyFactoryBean)을 사용하려면 무엇을 이해하고, 무엇을 해야하는가?
     * - 우선 ProxyFactoryBean 을 생성해 보자.
     * - 부가기능을 수행하 Advice를 구현해서 팩토리빈에 주입해 주어야 한다.
     * - 타겟을 주입해 준다.
     * - 필요에 따라 타겟의 메서드 중 특정한 메서드만 Advice를 통해 실행되도록 설정할 수 있다.
     * - 타겟의 인터페이스를 받지 않는다. 스프링의 ProxyFactoryBean 내부에서 타겟이 구현한 모든 인터페이스를 알아내서 나이나믹
     * - getObject()를 통해 다이나믹 프록시를 얻으면 되고, 프록시를 타겟 처럼 사용하면 된다.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(new HelloTarget());
        factoryBean.addAdvice(new UpperCaseAdvice());
        Hello hello = (Hello) factoryBean.getObject();

        Assertions.assertEquals("HELLO TOBY", hello.sayHello("Toby"));
        Assertions.assertEquals("HI TOBY", hello.sayHi("Toby"));
        Assertions.assertEquals("THANK YOU TOBY", hello.sayThankYou("Toby"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void pointCutAdvisoer() {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");
        factoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));
        Hello hello = (Hello) factoryBean.getObject();

        Assertions.assertEquals("HELLO TOBY", hello.sayHello("Toby"));
        Assertions.assertEquals("HI TOBY", hello.sayHi("Toby"));
        Assertions.assertEquals("Thank You Toby", hello.sayThankYou("Toby"));
    }

    // 템플릿/콜백 패턴에서 콜백 메서드 객체 역할을 한다.
    static class UpperCaseAdvice implements MethodInterceptor {

        // MethodInvocation
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            assert ret != null;
            return ret.toUpperCase();
        }
    }
}
