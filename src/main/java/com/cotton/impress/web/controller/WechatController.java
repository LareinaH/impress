package com.cotton.impress.web.controller;

import com.cotton.base.common.RestResponse;
import com.cotton.base.util.SHA1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.popular.api.TicketAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.ticket.Ticket;
import weixin.popular.bean.token.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by Administrator on 2017-05-16.
 */

@Controller
@RequestMapping("/wechat")
public class WechatController {

    @Value("${wechart.appId}")
    private String appId;
    @Value("${wechart.secret}")
    private String secret;
    @Value("${wchart.token}")
    private String token;
    @Value("${shareUrl}")
    private String url;

    @RequestMapping(value = "/wechatConfig")
    @ResponseBody
    public RestResponse<Map<String, Object>> wechatConfig() {

        RestResponse<Map<String, Object>> restResponse = new RestResponse<Map<String, Object>>();

        Map<String, Object> ret = new HashMap<String, Object>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + getJsapiTicket() +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ret.put("appId", appId);
        ret.put("url", url);
        ret.put("jsapi_ticket", getJsapiTicket());
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);


        restResponse.setCode(RestResponse.OK);
        restResponse.setData(ret);

        return restResponse;
    }

    @RequestMapping(value = "/weChatCallerBack")
    @ResponseBody
    public void weChatCallerBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 开始接入
        if (request.getMethod().equals("GET")) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");

            String[] str = {token, timestamp, nonce};
            Arrays.sort(str); // 字典序排序
            String bigStr = str[0] + str[1] + str[2];
            // SHA1加密
            String digest = new SHA1().getSHA1(token, timestamp, nonce, null).toLowerCase();

            // 确认请求来至微信
            if (digest.equals(signature)) {
                response.getWriter().print(echostr);
            }
        }
    }


    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }


    private String getJsapiTicket(){

        //1 通过 appid 和 secret  获得token
        Token token = TokenAPI.token(appId,secret);

        if(token != null){
            Ticket ticket = TicketAPI.ticketGetticket(token.getAccess_token());

            if(ticket != null){
                return ticket.getTicket();
            }
        }

        //2 通过token 获取 ticket


        return null;
    }
}
