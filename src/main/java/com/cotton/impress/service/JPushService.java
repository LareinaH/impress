package com.cotton.impress.service;

public interface JPushService {

    boolean createUser(String username,String password);

    boolean updatePassword(String username, String password);

    String getUserInfo(String username);


}
