package com.github.bgalek.spring.boot.spa.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @RestController
    static class ApiController {
        @GetMapping("/api")
        Set<Integer> api() {
            return IntStream.rangeClosed(0, 255).boxed().collect(Collectors.toSet());
        }
    }
}
