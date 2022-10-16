package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 구조에 대해 이해하고 싶어서 정리해 봅니다.
 * InvocationHandler 인터페이스를 구현한 UpperCaseHandler 클래스는 Proxy 역할을 수행합니다. Proxy 는 실제 작업 처리를 위임받아서
 * 어떤 특정한 부가작업을 수행한 후에 실제 타겟 객체에게 다시 실제 작업을 위임하는 구조로 동작합니다. 그래서 타겟 객체에 대한 참조를
 * 가지고 있어야 합니다. 그리고 타겟 객체가 가지는 메서드 객체를 받아서 해당 메서드를 리플렉션을 활용해 다이나믹하게 호출해 주는 역할을
 * 합니다. 만약 메서드의 시그니처가 다 다르고 다른 부가기능을 필요로 한다면 메서드의 이름 패턴을 통해 구분하는 로직이 추가로 필요합니다.
 */
@SuppressWarnings("ClassCanBeRecord")
public class UpperCaseHandler implements InvocationHandler {
    private final Object target;

    public UpperCaseHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        if (ret instanceof String && method.getName().startsWith("say")) {
            return ((String) ret).toUpperCase();
        } else {
            return ret;
        }
    }
}
