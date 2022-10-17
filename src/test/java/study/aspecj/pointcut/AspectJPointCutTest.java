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

    @Test
    public void pointcut() throws Exception {
        targetClassPointCutMatches("execution(* *(..))", true, true, true, true, true, true);
        targetClassPointCutMatches("execution(* hello(..))", true, true, false, false, false, false);
        targetClassPointCutMatches("execution(* hello())", true, false, false, false, false, false);
        targetClassPointCutMatches("execution(* hello(String))", false, true, false, false, false, false);
        targetClassPointCutMatches("execution(* meth*(..))", false, false, false, false, true, true);
        targetClassPointCutMatches("execution(* *(int, int))", false, false, true, true, false, false);
        targetClassPointCutMatches("execution(* *())", true, false, false, false, true, true);
        targetClassPointCutMatches("execution(* study.aspecj.pointcut.Target.*(..))", true, true, true, true, true, false);
        targetClassPointCutMatches("execution(* study.aspecj.pointcut.*.*(..))", true, true, true, true, true, true);
        // '..'을 이용해 특정 패키지 이하 모든 서브 패키지를 다 지정할 수도 있다.
        targetClassPointCutMatches("execution(* study..*.*(..))", true, true, true, true, true, true);
        targetClassPointCutMatches("execution(* com..*.*(..))", false, false, false, false, false, false);
        targetClassPointCutMatches("execution(* *..Target.*(..))", true, true, true, true, true, false);
        targetClassPointCutMatches("execution(* *..Tar*.*(..))", true, true, true, true, true, false);
        targetClassPointCutMatches("execution(* *..*get.*(..))", true, true, true, true, true, false);
        targetClassPointCutMatches("execution(* *..B*.*(..))", false, false, false, false, false, true);
        targetClassPointCutMatches("execution(* *..TargetInterface.*(..))", true, true, true, true, false, false);
        targetClassPointCutMatches("execution(* *(..) throws RuntimeException)", false, false, false, true, false, false);
        targetClassPointCutMatches("execution(int *(..))", false, false, true, true, false, false);
        targetClassPointCutMatches("execution(void *(..))", true, true, false, false, true, true);
    }
    public void targetClassPointCutMatches(String expression, boolean... expected) throws Exception {
        pointCutMatches(expression, expected[0], Target.class, "hello");
        pointCutMatches(expression, expected[1], Target.class, "hello", String.class);
        pointCutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
        pointCutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
        pointCutMatches(expression, expected[4], Target.class, "method");
        pointCutMatches(expression, expected[5], Bean.class, "method");
    }

    public void pointCutMatches(String expression, Boolean expected, Class<?> clazz, String methodName, Class<?>... args) throws Exception {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        Assertions.assertEquals(expected, pointcut.matches(clazz.getMethod(methodName, args), clazz));
    }


}
