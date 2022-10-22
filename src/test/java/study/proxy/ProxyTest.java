package study.proxy;

import java.lang.reflect.Proxy;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import study.proxy.advice.UppercaseAdvice;
import study.proxy.invocation.UpperCaseHandler;
import study.proxy.proxy.HelloUppercase;
import study.proxy.target.Hello;
import study.proxy.target.HelloImpl;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations="/factoryBeanTest.xml") // 테스트 컨텍스트가 자동으로 만들어줄 애플리케이션 컨텍스트의 위치 지정
public class ProxyTest {
    @Autowired
    ApplicationContext context;

    @Test
    public void manualProxy() {
        Hello hello = new HelloUppercase(new HelloImpl());
        Assertions.assertEquals("HELLO TOBY", hello.sayHello("Toby"));
        Assertions.assertEquals("HI TOBY", hello.sayHi("Toby"));
        Assertions.assertEquals("THANK YOU TOBY", hello.sayThankYou("Toby"));
    }

    @Test
    public void dynamicProxy() {
        Hello hello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UpperCaseHandler(new HelloImpl(), "say"));

        Assertions.assertEquals("HELLO TOBY", hello.sayHello("Toby"));
        Assertions.assertEquals("HI TOBY", hello.sayHi("Toby"));
        Assertions.assertEquals("THANK YOU TOBY", hello.sayThankYou("Toby"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(new HelloImpl());
        factoryBean.addAdvice(new UppercaseAdvice());
        Hello hello = (Hello) factoryBean.getObject();

        Assertions.assertEquals("HELLO TOBY", hello.sayHello("Toby"));
        Assertions.assertEquals("HI TOBY", hello.sayHi("Toby"));
        Assertions.assertEquals("THANK YOU TOBY", hello.sayThankYou("Toby"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void pointCutAdvisor() {
        // 스프링의 ProxyFactoryBean 생성
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();

        // 팩토리빈에 타겟 설정
        factoryBean.setTarget(new HelloImpl());

        // 포인트컷과 어디바이스 생성과 설정
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");
        UppercaseAdvice advice = new UppercaseAdvice();

        // 어드바이저 팩토리에 추가
        factoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, advice));

        // 프록시 생성
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
                return clazz -> clazz.getSimpleName().startsWith("HelloI");
            }
        };
        classMethodPointCut.setMappedName("sayH*");

        // 테스트
        checkAdviced(new HelloImpl(), classMethodPointCut, true);
        // 이런 식으로 하위 클래스를 임시로 만들 수 있구나.
        class HelloWorld extends HelloImpl {}
        checkAdviced(new HelloWorld(), classMethodPointCut, false);
        class HelloToby extends HelloImpl {}
        checkAdviced(new HelloToby(), classMethodPointCut, false);
    }

    private static void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        // 1. 프록시 팩토리 빈 생성
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        // 2. 타겟 설정
        proxyFactoryBean.setTarget(target);
        // 3. 재활용 가능한 부가기능 전담 객체와 클래스 및 메서드 선정 PointCut 객체를 포함한 Advisor 생성
        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
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
