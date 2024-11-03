# Spring Boot Starter - Single Page App
> Zero configuration single page app configuration for spring boot

[![Build](https://github.com/bgalek/spring-boot-starter-spa/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/bgalek/spring-boot-starter-spa/actions/workflows/build.yml)
![Maven Central](https://img.shields.io/maven-central/v/com.github.bgalek.spring.boot/spring-boot-starter-spa)

## Why?
Single Page Apps require one thing: return index.html on HTML request.

Sometimes it's wise to separate your apps (api backend & frontend backend + frontend) - but then you'll have to deal with versioning both aps in compatible way. 

For simple use cases it's totally ok to have spring-boot serve your SPA application (build i.e. by webpack).

Spring-boot does not support this feature out of the box. I've found multiple workarounds (404 page filters, manual file checking etc.)
and decided to make no-brainer library to handle this problem.

## Usage
Add library dependency:
```groovy
compile "com.github.bgalek.spring.boot:spring-boot-starter-spa:1.1.0"
```

Copy your fontend application to `/public` or `/static` folder during your app build phase.

## Compatibility

This configuration won't affect your:

- API calls (registered Controllers)
- Existing static files
