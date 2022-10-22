package study.proxy.factory;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import lombok.Setter;
import study.proxy.invocation.UpperCaseHandler;

@Setter
public class UppercaseProxyFactoryBean implements FactoryBean<Object> { // 범용적으로 사용하기 위해 Object 타입 지정
    private Object target;
    private Class<?> targetInterface; // 타겟과 타겟의 인터페이스를 함께 설정해야 한다.
    private String pattern; // 부가기능에서 필요로 하는 값에 의존한다.

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { targetInterface },
                new UpperCaseHandler(target, pattern)); // 팩토리빈과 부가기능 객체가 결합된다.
    }

    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
