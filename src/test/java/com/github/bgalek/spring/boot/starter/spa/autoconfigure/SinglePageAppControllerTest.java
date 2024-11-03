package com.github.bgalek.spring.boot.starter.spa.autoconfigure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collections;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SinglePageAppControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("should not impact on existing rest controllers")
    public void shouldNotImpactOnExistingRestControllers() {
        //when
        ResponseEntity<String> response = httpRequest(URI.create("/api"), MediaType.APPLICATION_JSON);

        //expect
        Assertions.assertEquals(response.getBody(), "true");
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("should not impact on existing mvc controllers")
    public void shouldNotImpactOnExistingMvcControllers() {
        //when
        ResponseEntity<String> response = httpRequest(URI.create("/page"), MediaType.TEXT_HTML);

        //expect
        Assertions.assertEquals(response.getBody(), "template works!");
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @ParameterizedTest
    @MethodSource("provideUseCases")
    @DisplayName("should return index,html or static resources if found")
    public void shouldReturnIndexHtmlOrStaticFilesIfFound(URI requestUrl, MediaType mediaType, String expectedBody) {
        //when
        ResponseEntity<String> response = httpRequest(requestUrl, mediaType);

        //expect
        Assertions.assertEquals(response.getBody(), expectedBody);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    private static Stream<Arguments> provideUseCases() {
        return Stream.of(
                Arguments.of("/", MediaType.TEXT_HTML, "html works!"),
                Arguments.of("/pages", MediaType.TEXT_HTML, "html works!"),
                Arguments.of("/some/frontend/url", MediaType.TEXT_HTML, "html works!"),
                Arguments.of("/some/frontend-with-dash/url", MediaType.TEXT_HTML, "html works!"),
                Arguments.of("/some/frontend-with-dash/1.0/url", MediaType.TEXT_HTML, "html works!"),
                Arguments.of("/some/frontend-with-dash/something.interesting", MediaType.TEXT_HTML, "html works!"),
                Arguments.of("/some/frontend-with-dash/1.0", MediaType.TEXT_HTML, "html works!"),
                Arguments.of("/resource/resource.name/details/my-%20resource/1142472/data", MediaType.TEXT_HTML, "html works!"),
                Arguments.of("/assets/script.js", MediaType.ALL, "console.log('js works!');"),
                Arguments.of("/assets/css/app.css", MediaType.ALL, "#css { content: 'works!' }"),
                Arguments.of("/assets/data.json", MediaType.ALL, "static works!"),
                Arguments.of("/static/data2.json", MediaType.ALL, "static works!"),
                Arguments.of("/assets/fa-solid-900.b5cf8ae2.woff2", MediaType.ALL, "static works!")
        );
    }

    private ResponseEntity<String> httpRequest(URI requestUrl, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(mediaType));
        return restTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
    }

    @SpringBootApplication
    static class App {
        public static void main(String[] args) {
            SpringApplication.run(App.class, args);
        }

        @RestController
        static class ApiController {
            @GetMapping("/api")
            boolean api() {
                return true;
            }
        }

        @Controller
        static class MvcController {
            @GetMapping("/page")
            String page() {
                return "something";
            }
        }
    }
}