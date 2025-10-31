package com.skills.user.repository;

import java.sql.SQLException;
import java.util.Optional;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skills.user.service.DatabaseService;
import com.skills.user.util.UserInfo;

@Repository
public class UserInfoRepository {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserInfoRepository.class);
    private DatabaseService dbs;

    @Autowired
    public UserInfoRepository(DatabaseService dbs) {
        this.dbs = dbs;
    }

    public Optional<UserInfo> findByUsername(String username) {
        try {
            String findUserByUsername = "SELECT id, username, password, role FROM skill_exchange_user WHERE username= :username";
            JsonObject queryFindUserByUsername = Json.createObjectBuilder().add("username", username).build();
            JsonArray rowsFromFindUserByUsername = dbs.queryUsingNamedParameters(findUserByUsername,
                    queryFindUserByUsername);
            if (rowsFromFindUserByUsername.size() != 1) {
                log.error("Error finding user by username: {}", username);
                return Optional.empty();
            }
            JsonObject row = rowsFromFindUserByUsername.getJsonObject(0);
            UserInfo userInfo = new UserInfo();
            userInfo.setId(row.getInt("id"));
            userInfo.setUserName(row.getString("username"));
            userInfo.setPassword(row.getString("password"));
            userInfo.setRole(row.getString("role"));
            Optional<UserInfo> op = Optional.of(userInfo);
            if (op.isPresent()) {
                return op;
            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            log.error("Error finding user by username: {}", username, e.getMessage());
            return Optional.empty();
        }
    }

    public int checkUser(String username) {
        try {
            String checkExistingUser = "SELECT COUNT(*) FROM skill_exchange_user WHERE username= :username";
            JsonObject queryCheckExistingUser = Json.createObjectBuilder().add("username", username).build();
            JsonArray rowsFromCheckExistingUser;
            rowsFromCheckExistingUser = dbs.queryUsingNamedParameters(checkExistingUser,
                    queryCheckExistingUser);
            return rowsFromCheckExistingUser.getJsonObject(0).getInt("count");
        } catch (SQLException e) {
            log.error("Something went wrong to check DB table skill_exchange_user: " + e.getMessage());
            return -1;
        }

    }

    public boolean addNewUser(String username, String password) {
        try {
            String createNewUser = "INSERT INTO skill_exchange_user (username, password, role) VALUES (:username, :password, :role)";
            JsonObject queryCreateNewUser = Json.createObjectBuilder().add("username", username)
                    .add("password", password).add("role", "user").build();
            dbs.executeNamedParametersUpdate(createNewUser, queryCreateNewUser);
            return true;
        } catch (SQLException e) {
            log.error("Unble to log user messages to DB table skill_exchange_user: " + e.getMessage());
        } catch (Exception e) {
            log.error("Exception while updating skill_exchange_user: " + e.getMessage());
        }
        return false;
    }

    public boolean updateExistingUser(String username, String password) {
        try {
            String updateExistingUser = "UPDATE skill_exchange_user SET password= :password WHERE username= :username";
            JsonObject queryFromUpdateExistingUser = Json.createObjectBuilder().add("username", username)
                    .add("password", password).build();
            dbs.executeNamedParametersUpdate(updateExistingUser, queryFromUpdateExistingUser);
            return true;
        } catch (SQLException e) {
            log.error("Unble to log user messages to DB table skill_exchange_user: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Exception while updating skill_exchange_user: " + e.getMessage());
            return false;
        }
    }
}
