package com.skills.user.controller;

import javax.json.Json;
import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skills.user.service.JwtService;
import com.skills.user.service.UserService;
import com.skills.user.util.UserDataRequest;

@RestController
@RequestMapping("/auth")
public class UserController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/user/signup")
    public JsonObject addUser(@RequestBody UserDataRequest userDataRequest) {
        boolean result = userService.addNewUser(userDataRequest);
        JsonObject jsonResult = Json.createObjectBuilder().add("success", result)
                .add("message", result == true ? "User data added successfully!" : "User data failed to be added!")
                .build();
        return jsonResult;
    }

    @PutMapping("/user/signup/{username}")
    public JsonObject updateUser(@RequestBody UserDataRequest userDataRequest) {
        boolean result = userService.updateExistingUser(userDataRequest);
        JsonObject jsonResult = Json.createObjectBuilder().add("success", result)
                .add("message", result == true ? "User data updated successfully!" : "User data failed to be updated!")
                .build();

        return jsonResult;
    }

    @PostMapping("/user/login")
    public JsonObject authenticateAndGetToken(@RequestBody UserDataRequest userDataRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDataRequest.getUsername(), userDataRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(userDataRequest.getUsername());
            return Json.createObjectBuilder()
                    .add("success", true)
                    .add("token", token)
                    .build();
        } else {
            return Json.createObjectBuilder()
                    .add("success", false)
                    .add("message", "Invalid user request!")
                    .build();
        }
    }
}
