package com.wemakeprice.simpletool.loganalysis;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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

    }

    private static User parseUserLine(String line) {
        String[] arr = line.split(",");

        User user = new User();
        user.setUserCd(arr[0]);
        user.setUserName(arr[6]);
        user.setJikmooCd(arr[17]);
        user.setJikmooName(MyCommonData.getJikmoo(arr[17]));
        user.setJikchakCd(arr[19].trim().equalsIgnoreCase("null") ? "" : arr[19]);
        user.setJikchakName(MyCommonData.getJikchak(arr[19]));
        user.setJikgubCd(arr[15]);
        user.setJikgubName(MyCommonData.getJikgub(arr[15]));
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
