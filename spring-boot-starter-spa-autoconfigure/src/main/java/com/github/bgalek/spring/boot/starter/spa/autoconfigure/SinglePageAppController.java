package com.github.bgalek.spring.boot.starter.spa.autoconfigure;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class SinglePageAppController {
    /**
     * It's combination of three Spring features: list of paths, ant matchers syntax and regular expressions.
     * Spring allows regular expressions only for named path parameters i.e. "path".
     * <p>
     * First path ensures that first level paths are matched (literal / at the beginning)
     * example: /something
     * <p>
     * Second path ensures that all other levels are matched (ant \/**\/ at the beginning).
     * example: /something/even/more
     * <p>
     * Regular expressions checks if url does not end with literal dot and has some letters/numbers afterwards.
     * example: styles.css, scripts.js, object.json, font.woff2
     * warning: index.html won't work.
     * <p>
     * If url is matched, it means we want to serve HTML response (index file) - even for non existing html files (404).
     * That's how SPA applications work.
     * <p>
     * If this url is not matched, spring continues checking other resolvers REST controllers and static files resolver.
     *
     * @return String (forward to index page)
     **/
    @GetMapping(value = {"/{path:^(?!.*\\.[a-z0-9]+$).*$}", "/**/{path:^(?!.*\\.[a-z0-9]+$).*$}"}, produces = MediaType.TEXT_HTML_VALUE)
    public String forward() {
        return "forward:/";
    }
}
