package main.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("DatabaseInitializer started.");
    }
}