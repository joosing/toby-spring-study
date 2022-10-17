package study.aspecj.pointcut;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

@SuppressWarnings("ALL")
public class AspectJPointCutTest {

    @Test
    public void methodSignaturePointCut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("""
            execution(public int study.aspecj.pointcut.TargetInterface.minus(int, int) throws java.lang.RuntimeException)
           """);

        // Target.minus()
        Assertions.assertTrue(pointcut.matches(Target.class.getMethod("minus", int.class, int.class), Target.class));

        // Target.plus()
        Assertions.assertFalse(pointcut.matches(Target.class.getMethod("plus", int.class, int.class), Target.class));

        // Bean.method()
        Assertions.assertFalse(pointcut.matches(Bean.class.getMethod("method"), Bean.class));
    }

    @Test
    public void methodReflection() throws NoSuchMethodException {
        System.out.println(Target.class.getMethod("minus", int.class, int.class));
        // Output:
        // public int study.aspecj.pointcut.Target.minus(int,int) throws java.lang.RuntimeException
    }
}
