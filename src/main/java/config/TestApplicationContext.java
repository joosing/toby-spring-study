package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("/testContext.xml")
public class TestApplicationContext {

}
