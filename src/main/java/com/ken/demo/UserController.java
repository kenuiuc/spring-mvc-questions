package com.ken.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    @PutMapping(value = "/user", consumes = {"application/x-www-form-urlencoded;charset=UTF-8"})
    public String putUser(@RequestParam Map<String, String> data) throws Exception {
        return data.get("name");
    }

    @GetMapping(value = "/user")
    public String getUser() {
        return "KEN";
    }
}
