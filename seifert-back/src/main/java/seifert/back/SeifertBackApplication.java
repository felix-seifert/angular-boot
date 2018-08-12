package seifert.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SeifertBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeifertBackApplication.class, args);
    }

}
