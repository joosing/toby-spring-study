package service.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import lombok.Getter;

@Getter
public class MockMailSender implements MailSender {
    private final List<String> requests = new ArrayList<>();

    public void clear() {
        requests.clear();
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) {
        requests.add(Objects.requireNonNull(simpleMessage.getTo())[0]);
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) {

    }
}
