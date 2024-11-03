package com.github.bgalek.spring.boot.starter.spa.autoconfigure;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class SinglePageAppController {
    /**
     * If this url is not matched, spring continues checking other resolvers REST controllers and static files resolver.
     **/
    @GetMapping(value = "{path:^(?!assets|static|swagger-ui).*$}/**", produces = MediaType.TEXT_HTML_VALUE)
    public String forward() {
        return "forward:/";
    }
}
