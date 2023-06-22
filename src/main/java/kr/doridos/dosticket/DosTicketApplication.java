package kr.doridos.dosticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class DosTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(DosTicketApplication.class, args);
    }

}
