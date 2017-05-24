package com.cotton.impress.service;

public interface JPushService {

    /*
     *  jmessage
     */

    boolean createUser(String username,String password);

    boolean updatePassword(String username, String password);

    String getUserInfo(String username);


    /*
     * jpush
     */

    boolean setPushMessage(boolean bAll,String username,String message);


}
