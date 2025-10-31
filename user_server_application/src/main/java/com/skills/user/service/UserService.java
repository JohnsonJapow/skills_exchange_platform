package com.skills.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skills.user.repository.UserInfoRepository;
import com.skills.user.service.UserService;
import com.skills.user.util.UserDataRequest;

@Service
public class UserService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
    private UserInfoRepository userInfoRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserInfoRepository userInfoRepository, PasswordEncoder encoder) {
        this.userInfoRepository = userInfoRepository;
        this.encoder = encoder;
    }

    public boolean addNewUser(UserDataRequest userDataRequest) {
        String username = userDataRequest.getUsername();
        String password = userDataRequest.getPassword();
        if(username==null||username.length()==0){
            log.error("Invalid username :{}", username);
            return false;
        }
        if (password==null||password.length()==0) {
            log.error("Invalid password :{}", password);
            return false;
        }
        int countOfThisUser = userInfoRepository.checkUser(username);
        if (countOfThisUser == 0) {
            password = encoder.encode(userDataRequest.getPassword());
            userInfoRepository.addNewUser(username, password);
            return true;
        }
        log.info("Could not add a new user :{}" + username);
        return false;
    }

    public boolean updateExistingUser(UserDataRequest userDataRequest) {
        String username = userDataRequest.getUsername();
        String password = userDataRequest.getPassword();
        if(username==null||username.length()==0){
            log.error("Invalid username :{}", username);
            return false;
        }
        if (password==null||password.length()==0) {
            log.error("Invalid password :{}", password);
            return false;
        }
        int countOfThisUser = userInfoRepository.checkUser(username);
        if (countOfThisUser == 1) {
            password = encoder.encode(userDataRequest.getPassword());
            userInfoRepository.updateExistingUser(username, password);
            return true;
        }
        log.info("Could not update the user :{}" + username);
        return false;
    }
}
