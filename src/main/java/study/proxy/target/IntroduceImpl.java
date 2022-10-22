package study.proxy.target;

public class IntroduceImpl implements Introduce {
    @Override
    public String introduceYou(String text) {
        return "You are "+ text;
    }

    @Override
    public String introduceMyself(String text) {
        return "I am " + text;
    }
}
