package net.podspace.flink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Value("${spring.application.name}")
    private String appName;

    public HelloController() {}

    @GetMapping("/")
    public String index()
    {
        String version;
        try {
            logger.info("home page called for flink consumer application");
            version = System.getenv("APP_VERSION");
            if (version == null || version.isBlank()) {
                version = "None";
            }
        } catch (SecurityException ignore) {
            version = "None";
        }

        return "Greetings, flink consumer application version is " + version + " for " + appName;
    }
}
