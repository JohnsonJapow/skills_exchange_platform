package com.skills.user.service;

import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skills.user.util.JsonUtils;
import com.skills.user.util.MissingValueException;

@Service
public class ProfileService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProfileService.class);
    private DatabaseService dbs;

    @Autowired
    public ProfileService(DatabaseService dbs) {
        this.dbs = dbs;
    }

    public JsonObject getUserProfile(String username) {
        String getExistingUserProfile = "SELECT * FROM skill_exchange_user_profile WHERE username= :username";
        JsonObject queryExistingUserProfile = Json.createObjectBuilder().add("username", username).build();
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        try {
            JsonArray resultArray = dbs.queryUsingNamedParameters(getExistingUserProfile, queryExistingUserProfile);
            if (resultArray.size() != 1) {
                resultBuilder.add("Error", "Error found when search the profile with the username {}" + username
                        + ", result size {}" + resultArray.size());
                return resultBuilder.build();
            }
            resultBuilder.add("Profile", resultArray.getJsonObject(0));
            JsonObject result = resultBuilder.build();
            log.info(result.toString());
            return result;
        } catch (SQLException e) {
            resultBuilder.add("Error", "Error found when search the profile with the username {}" + username);
            JsonObject result = resultBuilder.build();
            log.error(e.getMessage());
            return result;
        }
    }

    public String addUserProfile(JsonObject request) {
        try {
            String username = JsonUtils.getString(request, "username");
            String skills = JsonUtils.getString(request, "skills");
            String wishList = JsonUtils.getString(request, "wishList");
            String urls = JsonUtils.getString(request, "urls");


            String addNewUserProfile = "ALTER TABLE skill_exchange_user_profile ADD username= :username, skills =:skills, wishList =:wishList, urls =:urls";
            JsonObject executeAddNewUserProfile = Json.createObjectBuilder().add("username", username)
                    .add("skills", skills).add("wishList", wishList).add("urls", urls).build();
            int result = dbs.executeNamedParametersUpdate(addNewUserProfile, executeAddNewUserProfile);            


            if (result == 0) {
                return "Success: add a new user profile";
            }
            return "SQL statements that return nothing";
        } catch (MissingValueException e) {
            log.error("MissingValueException from the request {}" + request, e.getMessage());
        } catch (SQLException e) {
            log.error("SQLException from the request {}" + request, e.getMessage());
        }
        return "Something went wrong the the request {}" + request;
    }

    public String updateExictingUserProfile(JsonObject request) {
        try {
            String username = JsonUtils.getString(request, "username");
            String skills = JsonUtils.getString(request, "skills");
            String wishList = JsonUtils.getString(request, "wishList");
            String urls = JsonUtils.getString(request, "urls");
            String updateExistingUserProfile = "UPDATE skill_exchange_user_profile SET skills =:skills, wishList =:wishList, urls =:urls WHERE username= :username";
            JsonObject executeUpdateExistingUserProfile = Json.createObjectBuilder().add("username", username)
                    .add("skills", skills).add("wishList", wishList).add("urls", urls).build();
            int result = dbs.executeNamedParametersUpdate(updateExistingUserProfile, executeUpdateExistingUserProfile);
            if (result == 0) {
                return "Success: update an existing user profile";
            }
            return "SQL statements that return nothing";
        } catch (MissingValueException e) {
            log.error("MissingValueException from the request {}" + request, e.getMessage());
        } catch (SQLException e) {
            log.error("SQLException from the request {}" + request, e.getMessage());
        }
        return "Something went wrong the the request {}" + request;
    }
}
