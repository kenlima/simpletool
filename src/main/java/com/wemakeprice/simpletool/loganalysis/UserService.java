package com.wemakeprice.simpletool.loganalysis;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(@Value("${user.file}") String userDataFile) throws Exception {
        loadUserMap(userDataFile);
    }

    public static Map<String, User> users = null;

    public UserService() throws Exception {

    }

    public void loadUserMap(String userDataFile) throws Exception {
        users = Files.lines(Paths.get(userDataFile)).map(UserService::parseUserLine)
                .collect(Collectors.toMap(User::getUserCd, user -> user));
        logger.info("User loaded");

    }

    private static User parseUserLine(String line) {
        String[] arr = line.split(",");

        User user = new User();
        user.setUserCd(arr[0]);
        user.setUserName(arr[1]);
        user.setJikgubCd(arr[2]);
        user.setJikgubName(MyCommonData.getJikgub(arr[2]));
        user.setJikmooCd(arr[3]);
        user.setJikmooName(MyCommonData.getJikmoo(arr[3]));
        user.setJikchakCd(arr[4].trim().equalsIgnoreCase("null") ? "" : arr[4]);
        user.setJikchakName(MyCommonData.getJikchak(arr[4]));
        return user;
    }

    public User getUser(String userCd) {
        if (users.containsKey(userCd)) {
            return users.get(userCd);
        }
        User user = new User();
        user.setUserCd(userCd);
        user.setJikchakCd("없음");
        user.setJikmooCd("없음");
        user.setUserName("없음");
        return user;
    }
}
