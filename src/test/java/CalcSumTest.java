import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class CalcSumTest {
    @Test
    public void sumOfNumbers() throws IOException {
        final Calculator calculator = new Calculator();
        final int sum = calculator.calcSum(getClass().getResource("numbers.txt").getPath());
        Assert.assertEquals(10, sum);
    }
}
