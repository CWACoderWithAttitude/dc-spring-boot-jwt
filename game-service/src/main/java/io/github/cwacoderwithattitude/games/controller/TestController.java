package io.github.cwacoderwithattitude.games.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {
    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TestController.class.getName());

    @GetMapping("/all")
    public String allAccess() {
        logger.info("Accessed public endpoint");
        return "Public Content.";
    }

    @GetMapping("/user")
    public String userAccess() {
        logger.info("Accessed user endpoint");
        return "User Content.";
    }
}