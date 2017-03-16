package com.conton.impress.web.controller;

import com.conton.base.common.RestResponse;
import com.conton.impress.model.Member;
import com.conton.impress.model.VO.MemberVO;
import com.conton.impress.service.MemberService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "/{id}")
    @ResponseBody
    public RestResponse<Member> getById(@PathVariable Long id) {
        Member member = memberService.getById(id);
        return new RestResponse<Member>(member);
    }


    /**
     * 手机登录
     * @param cellphone 手机号
     * @param passWord 密码
     * @param code  验证码
     * @return
     */
    @RequestMapping(value = "/un/login")
    @ResponseBody
    public RestResponse<MemberVO> login(@RequestParam(required=true)String cellphone,
                                      @RequestParam(required=true)String passWord,
                                      @RequestParam(required=true)String code) {
        RestResponse<MemberVO> restResponse = new RestResponse<MemberVO>();

        //TODO: 验证验证码

        Member model = new Member();
        model.setCellphone(cellphone);
        model.setPassword(passWord);
        model.setStatus("normal");
        List<Member> memberList = memberService.queryList(model);

        if(!memberList.isEmpty()){
            MemberVO memberVO = new MemberVO();
            BeanUtils.copyProperties(memberList.get(0),memberVO);
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(memberVO);
        }else {
            restResponse.setCode("error");
            restResponse.setMessage("用户名或密码错误");
        }

        return restResponse;
    }


    /**
     * 手机注册
     * @param cellphone
     * @param passWord
     * @param code
     * @return
     */
    @RequestMapping(value = "/un/register")
    @ResponseBody
    public RestResponse<MemberVO> register(@RequestParam(required=true)String cellphone,
                                       @RequestParam(required=true)String passWord,
                                       @RequestParam(required=true)String code) {
        RestResponse<MemberVO> restResponse = new RestResponse<MemberVO>();

        //TODO: 验证验证码

        Member model = new Member();
        model.setCellphone(cellphone);
        model.setStatus("normal");
        List<Member> memberList = memberService.queryList(model);

        if(!memberList.isEmpty()){
            restResponse.setCode("error");
            restResponse.setMessage("该手机号已经注册");
            return restResponse;
        }

        Member member = new Member();
        member.setCellphone(cellphone);
        member.setPassword(passWord);
        member.setStatus("normal");
        member.setTicket(RandomStringUtils.randomAlphanumeric(15));

        if(memberService.insert(member)){
            MemberVO memberVO = new MemberVO();
            BeanUtils.copyProperties(member,memberVO);
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(memberVO);
        }else {
            restResponse.setCode("error");
            restResponse.setMessage("注册用户失败");
        }

        return restResponse;
    }

    //发送验证码

    //绑定手机号

    //修改密码

    //修改个人资料

    //我的好友（分页）

}
