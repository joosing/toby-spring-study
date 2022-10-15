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
