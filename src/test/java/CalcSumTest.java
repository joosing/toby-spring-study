import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class CalcSumTest {
    @Test
    public void sumOfNumbers() throws IOException {
        final Calculator calculator = new Calculator();
        final int sum = calculator.calSum(getClass().getResource("numbers.txt").getPath());
        Assert.assertEquals(10, sum);
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        final Calculator calculator = new Calculator();
        final int sum = calculator.calcMultiply(getClass().getResource("numbers.txt").getPath());
        Assert.assertEquals(24, sum);
    }

    @Test
    public void concatenateStrings() throws IOException {
        final Calculator calculator = new Calculator();
        final String result = calculator.concatenate(getClass().getResource("numbers.txt").getPath());
        Assert.assertEquals("1234", result);
    }
}
