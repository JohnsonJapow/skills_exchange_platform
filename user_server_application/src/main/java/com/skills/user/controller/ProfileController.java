package com.skills.user.controller;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skills.user.service.ProfileService;

@RestController
@RequestMapping("/user/info")
public class ProfileController {
    private ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService=profileService;
    }

    @GetMapping("/profile/{username}")
    public JsonObject getProfile(@PathVariable String username){
        JsonObject result = profileService.getUserProfile(username);
        return result;
    }

    @PostMapping("/profile")
    public String addProfile(@RequestBody JsonObject request){
        String result = profileService.addUserProfile(request);
        return result;
    }

    @PutMapping("/profile/{username}")
    public String updateProfile(@RequestBody JsonObject request){
        String result = profileService.updateExictingUserProfile(request);
        return result;
    }
}
