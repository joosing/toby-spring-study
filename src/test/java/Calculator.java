import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    private <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initValue) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            T value = initValue;
            String line;
            while ((line = br.readLine()) != null) {
                value = callback.doSomethingWithLine(line, value);
            }
            return value;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Integer calSum(String filepath) throws IOException {
        return lineReadTemplate(filepath, (line, value) -> Integer.valueOf(line) + value, 0);
    }

    public Integer calcMultiply(String filepath) throws IOException {
        return lineReadTemplate(filepath, (line, value) -> Integer.valueOf(line) * value, 1);
    }

    public String concatenate(String filepath) throws IOException {
        return lineReadTemplate(filepath, (line, value) -> value + line, "");
    }
}
