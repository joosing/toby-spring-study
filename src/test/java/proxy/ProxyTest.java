package proxy;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        /*
         * 궁금한점?
         * 자바의 리플렉션 기술을 사용하면 Hello 라는 인터페이스 타입  클래스 객체만 가지고 프록시 객체를 만들어 낼 수 있는가?
         * 다이나믹 프록시 객체는 타겟의 모든 메서드 객체를 가지고 있다고 대응되는 프록시 메서드 호출 시 InvocationHandler 로
         * 메서드를 파라미터로 념겨주게 되는가?
         */
        Hello hello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UpperCaseHandler(new HelloTarget())
        );
        Assertions.assertEquals("HELLO TOBY", hello.sayHello("Toby"));
        Assertions.assertEquals("HI TOBY", hello.sayHi("Toby"));
        Assertions.assertEquals("THANK YOU TOBY", hello.sayThankYou("Toby"));
    }
}
