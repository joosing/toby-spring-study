import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations="/applicationContext.xml")
public class ApplicationContextShareTest {
    @Autowired
    private ApplicationContext context;

    @Before
    public void setUp() {
        System.out.println(context);
    }

    @Test
    public void dummy() {
        System.out.println("dummy");
    }
}
