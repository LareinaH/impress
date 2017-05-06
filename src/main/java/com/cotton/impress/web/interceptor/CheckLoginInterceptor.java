package com.cotton.impress.web.interceptor;

import com.cotton.base.common.RestResponse;
import com.cotton.impress.model.Member;
import com.cotton.impress.service.MemberService;

import com.cotton.impress.web.PermissionContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CheckLoginInterceptor implements HandlerInterceptor {

    @Autowired
    MemberService memberService;



    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {

        //清空context
        PermissionContext.clearThreadVariable();

        //1 从request里获取【APP-SESSION-TICKET】
        String appSessionTicket = httpServletRequest.getHeader("APP-SESSION-TICKET");

        if ( StringUtils.isEmpty(appSessionTicket)) {
            setReLogin(httpServletRequest, httpServletResponse);
            return false;
        }
        //根据ticket 获取用户

        Member model = new Member();
        model.setTicket(appSessionTicket);

        List<Member> memberList = memberService.queryList(model);

        if(memberList.isEmpty()){
            setReLogin(httpServletRequest,httpServletResponse);
            return false;
        }

        PermissionContext.setMember(memberList.get(0));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {

        //清理登录数据
        PermissionContext.clearThreadVariable();
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {

    }

    private void setReLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponse<Void> restResponse = new RestResponse<Void>();
        restResponse.setCode(RestResponse.LoginTimeout);
        restResponse.setMessage("用户未登录");

        //转换成json
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        String jsonString = g.toJson(restResponse);

        //写入response
        try {
            httpServletResponse.setContentType("application/json");

            OutputStream outputStream = null;//获取OutputStream输出流
            outputStream = httpServletResponse.getOutputStream();

            byte[] dataByteArr = jsonString.getBytes("UTF-8");
            outputStream.write(dataByteArr);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
