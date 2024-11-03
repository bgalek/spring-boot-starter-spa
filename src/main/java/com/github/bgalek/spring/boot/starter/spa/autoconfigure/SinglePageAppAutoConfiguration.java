package com.github.bgalek.spring.boot.starter.spa.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.SpringServletContainerInitializer;

@Configuration
@ConditionalOnClass(SpringServletContainerInitializer.class)
class SinglePageAppAutoConfiguration {
}
