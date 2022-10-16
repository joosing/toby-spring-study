package proxy;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
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

    @SuppressWarnings("serial")
    @Test
    public void classNamePointCutAdvisor() {
        // 포인트컷 준비
        NameMatchMethodPointcut classMethodPointCut = new NameMatchMethodPointcut() {
            // 익명 내부 클래스로 만들어서 클래스를 하위 클래스를 구현할 수 있구나.
            @Override
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getSimpleName().startsWith("HelloT");
            }
        };
        classMethodPointCut.setMappedName("sayH*");

        // 테스트
        checkAdviced(new HelloTarget(), classMethodPointCut, true);
        // 이런 식으로 하위 클래스를 임시로 만들 수 있구나.
        class HelloWorld extends HelloTarget {}
        checkAdviced(new HelloWorld(), classMethodPointCut, false);
        class HelloToby extends HelloTarget {}
        checkAdviced(new HelloToby(), classMethodPointCut, true);
    }

    private static void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        // 1. 프록시 팩토리 빈 생성
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        // 2. 타겟 설정
        proxyFactoryBean.setTarget(target);
        // 3. 재활용 가능한 부가기능 전담 객체와 클래스 및 메서드 선정 PointCut 객체를 포함한 Advisor 생성
        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));
        // 4. 프록시 생성
        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();

        assert proxiedHello != null;
        if (adviced) {
            Assertions.assertEquals("HELLO TOBY", proxiedHello.sayHello("Toby"));
            Assertions.assertEquals("HI TOBY", proxiedHello.sayHi("Toby"));
        } else {
            Assertions.assertEquals("Hello Toby", proxiedHello.sayHello("Toby"));
            Assertions.assertEquals("Hi Toby", proxiedHello.sayHi("Toby"));
        }
        Assertions.assertEquals("Thank You Toby", proxiedHello.sayThankYou("Toby"));
    }
}
