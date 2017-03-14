package com.conton.impress.web.controller;

import com.conton.base.common.RestResponse;
import com.conton.impress.model.Member;
import com.conton.impress.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 会员相关
 */
@Controller
@RequestMapping("/users")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/{id}")
    @ResponseBody
    public RestResponse<Member> getById(@PathVariable Long id) {
        Member member = memberService.getById(id);
        return new RestResponse<Member>(member);
    }
}
