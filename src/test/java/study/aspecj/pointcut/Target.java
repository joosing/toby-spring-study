package study.aspecj.pointcut;

public class Target implements TargetInterface {
    @Override
    public void hello() {
        System.out.println("hello");
    }

    @Override
    public void hello(String a) {
        System.out.println("hello " + a);
    }

    @SuppressWarnings("ThrowsRuntimeException")
    @Override
    public int minus(int a, int b) throws RuntimeException {
        return a - b;
    }

    @Override
    public int plus(int a, int b) {
        return a + b;
    }

    public void method() {
        System.out.println("Target method");
    }
}
