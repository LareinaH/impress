package com.conton.impress.web.controller;

import com.conton.base.common.RestResponse;
import com.conton.impress.model.Member;
import com.conton.impress.model.VO.MemberVO;
import com.conton.impress.service.MemberService;
import com.conton.impress.web.PermissionContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 会员相关
 */
@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 手机登录
     *
     * @param cellphone 手机号
     * @param passWord  密码
     * @param code      验证码
     * @return
     */
    @RequestMapping(value = "/un/login")
    @ResponseBody
    public RestResponse<MemberVO> login(@RequestParam(required = true) String cellphone,
                                        @RequestParam(required = true) String passWord,
                                        @RequestParam(required = true) String code) {
        RestResponse<MemberVO> restResponse = new RestResponse<MemberVO>();

        //TODO: 验证验证码

        Member model = new Member();
        model.setCellphone(cellphone);
        List<Member> memberList = memberService.queryList(model);

        if (memberList.isEmpty()) {

            //该用户不存在 注册新用户
            Member member = new Member();
            member.setCellphone(cellphone);
            member.setPassword(passWord);
            member.setStatus("normal");
            member.setSex("unknow");
            member.setTicket(RandomStringUtils.randomAlphanumeric(15));

            if (memberService.insert(member)) {
                MemberVO memberVO = new MemberVO();
                BeanUtils.copyProperties(member, memberVO);
                restResponse.setCode(RestResponse.OK);
                restResponse.setData(memberVO);
            } else {
                restResponse.setCode("error");
                restResponse.setMessage("注册用户失败!");
            }

        } else {
            if (memberList.get(0).getPassword().equals(passWord) && memberList.get(0).getStatus().equals("normal")) {
                MemberVO memberVO = new MemberVO();
                BeanUtils.copyProperties(memberList.get(0), memberVO);
                restResponse.setCode(RestResponse.OK);
                restResponse.setData(memberVO);
            } else {
                restResponse.setCode("error");
                restResponse.setMessage("用户名或者密码错误!");
            }
        }

        return restResponse;
    }


    //发送验证码


    /**
     * 绑定手机号
     *
     * @param cellphone
     * @param code
     * @return
     */
    @RequestMapping(value = "/bindCellphone")
    @ResponseBody
    public RestResponse<MemberVO> bindCellphone(@RequestParam(required = true) String cellphone,
                                                @RequestParam(required = true) String code) {
        RestResponse<MemberVO> restResponse = new RestResponse<MemberVO>();

        //TODO: 验证验证码

        Member model = new Member();
        model.setCellphone(cellphone);
        model.setStatus("normal");
        List<Member> memberList = memberService.queryList(model);

        if (!memberList.isEmpty()) {
            restResponse.setCode("error");
            restResponse.setMessage("该手机号已经注册!");
            return restResponse;
        }

        Member member = PermissionContext.getMember();

        if (!member.getCellphone().isEmpty()) {

            restResponse.setCode("error");
            restResponse.setMessage("改用户已经绑定手机!");
            return restResponse;
        }
        member.setCellphone(cellphone);

        if (memberService.update(member)) {

            MemberVO memberVO = new MemberVO();
            BeanUtils.copyProperties(member, memberVO);
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(memberVO);

        } else {
            restResponse.setCode("error");
            restResponse.setMessage("手机号绑定失败!");
        }

        return restResponse;
    }

    //修改密码
    @RequestMapping(value = "/editPassword")
    @ResponseBody
    public RestResponse<Void> editPassword(@RequestParam(required = true) String oldPassword,
                                           @RequestParam(required = true) String newPassword) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();

        if (!member.getPassword().equals(oldPassword)) {
            restResponse.setCode("error");
            restResponse.setMessage("原密码错误!");
            return restResponse;
        }
        member.setPassword(newPassword);

        if (memberService.update(member)) {

            restResponse.setCode(RestResponse.OK);

        } else {
            restResponse.setCode("error");
            restResponse.setMessage("修改密码失败!");
        }
        return restResponse;
    }

    /**
     * 修改个人资料
     *
     * @param name
     * @param headPortrait
     * @return
     */
    @RequestMapping(value = "/editMemberInfo")
    @ResponseBody
    public RestResponse<MemberVO> editMemberInfo(String name, String headPortrait) {
        RestResponse<MemberVO> restResponse = new RestResponse<MemberVO>();

        Member member = PermissionContext.getMember();
        if (!StringUtils.isEmpty(name)) {
            member.setName(name);
        }
        if (!StringUtils.isEmpty(headPortrait)) {
            member.setHeadPortrait(headPortrait);
        }

        if (memberService.update(member)) {

            MemberVO memberVO = new MemberVO();
            BeanUtils.copyProperties(member, memberVO);
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(memberVO);

        } else {
            restResponse.setCode("error");
            restResponse.setMessage("修改个人资料失败!");
        }
        return restResponse;
    }

    /**
     * 设置性别
     *
     * @param sex
     * @return
     */
    @RequestMapping(value = "/setSex")
    @ResponseBody
    public RestResponse<Void> setSex(@RequestParam(required = true) String sex) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();
        if (!StringUtils.isEmpty(sex)) {
            member.setSex(sex);
        }
        if (memberService.update(member)) {
            restResponse.setCode(RestResponse.OK);

        } else {
            restResponse.setCode("error");
            restResponse.setMessage("设置性别!");
        }
        return restResponse;
    }
    //我的好友（分页）

}
