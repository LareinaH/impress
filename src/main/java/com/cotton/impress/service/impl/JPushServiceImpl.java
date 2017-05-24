package com.cotton.impress.service.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.NativeHttpClient;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.common.model.RegisterInfo;
import cn.jmessage.api.user.UserInfoResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.cotton.impress.service.JPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JPushServiceImpl implements JPushService {

    protected static final Logger LOG = LoggerFactory.getLogger(JPushServiceImpl.class);

    @Value("${jpush.appkey}")
    String appkey;
    @Value("${jpush.masterSecret}")
    String masterSecret;

    @Override
    public boolean updatePassword(String username, String password) {
        JMessageClient client = new JMessageClient(appkey, masterSecret);

        try {
            client.updateUserPassword(username, password);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            return false;
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean createUser(String username, String password) {

        JMessageClient client = new JMessageClient(appkey, masterSecret);

        try {

            List<RegisterInfo> users = new ArrayList<RegisterInfo>();

            RegisterInfo user = RegisterInfo.newBuilder()
                    .setUsername(username)
                    .setPassword(password)
                    .build();

            users.add(user);

            RegisterInfo[] regUsers = new RegisterInfo[users.size()];

            String res = client.registerUsers(users.toArray(regUsers));
            LOG.info(res);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            return false;
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public String getUserInfo(String username) {
        JMessageClient client = new JMessageClient(appkey, masterSecret);

        try {
            UserInfoResult result = client.getUserInfo(username);
            return result.getUsername();
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());

        }
        return null;
    }

    @Override
    public boolean setPushMessage(boolean bAll, String username, String message) {

        ClientConfig clientConfig = ClientConfig.getInstance();
        final JPushClient jpushClient = new JPushClient(masterSecret, appkey, null, clientConfig);
        String authCode = ServiceHelper.getBasicAuthorization(appkey, masterSecret);

        NativeHttpClient httpClient = new NativeHttpClient(authCode, null, clientConfig);

        jpushClient.getPushClient().setHttpClient(httpClient);
        PushPayload payload;

        //1 发送给所有人 2 发送给制定人
        if(bAll){
            payload= buildPushObject_all_all_alert(message);
        }else {
            payload = buildPushObject_all_alias_alert(username,message);
        }

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);

        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            LOG.error("Sendno: " + payload.getSendno());

        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            LOG.error("Sendno: " + payload.getSendno());
        }

        return true;
    }

    public  PushPayload buildPushObject_all_alias_alert(String username,String alert) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(username))
                .setNotification(Notification.alert(alert))
                .build();
    }

    public  PushPayload buildPushObject_all_all_alert(String alert) {
        return PushPayload.alertAll(alert);
    }
}
