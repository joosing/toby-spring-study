package study.proxy;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import study.proxy.factory.MessageFactoryBean;
import study.proxy.target.Hello;
import study.proxy.target.Introduce;
import study.proxy.target.Message;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations= "/studyContext.xml") // 테스트 컨텍스트가 자동으로 만들어줄 애플리케이션 컨텍스트의 위치 지정
public class FactoryBeanTest {
    @Autowired
    ApplicationContext context;
    @Autowired
    Hello hello;
    @Autowired
    Introduce introduce;

    @Test
    public void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        Assertions.assertSame(Message.class, message.getClass());
        Assertions.assertEquals(((Message)message).getText(), "Factory Bean");
    }

    @Test
    public void getFactoryBean() {
        Object factory = context.getBean("&message");
        Assertions.assertSame(MessageFactoryBean.class, factory.getClass());
    }

    @Test
    public void setupFactoryBean() {
        Assertions.assertEquals("HELLO TOBY", hello.sayHello("Toby"));
        Assertions.assertEquals("HI TOBY", hello.sayHi("Toby"));
        Assertions.assertEquals("THANK YOU TOBY", hello.sayThankYou("Toby"));
    }
}
