package com.conton.impress.web.controller;

import com.conton.base.common.RestResponse;
import com.conton.impress.model.Member;
import com.conton.impress.model.MemberFriend;
import com.conton.impress.model.Message;
import com.conton.impress.model.VO.MemberVO;
import com.conton.impress.service.MemberFriendService;
import com.conton.impress.service.MemberService;
import com.conton.impress.service.MessageService;
import com.conton.impress.web.PermissionContext;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 会员相关
 */
@Controller
@RequestMapping("/member")
public class MemberController extends ImpressBaseComtroller{

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberFriendService memberFriendService;
    @Autowired
    private MessageService messageService;

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
    public RestResponse<MemberVO> login( String cellphone,String passWord,String code) {
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

    /**
     * 我的好友（分页）
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/myFriends")
    @ResponseBody
    public RestResponse<PageInfo<MemberVO>> register(@RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "20") int pageSize) {
        RestResponse<PageInfo<MemberVO>> restResponse = new RestResponse<PageInfo<MemberVO>>();

        Member member = PermissionContext.getMember();

        MemberFriend model = new MemberFriend();
        model.setMemberId(member.getId());
        model.setStatus("normal");
        PageInfo<MemberFriend> memberFriendInfo = memberFriendService.query(pageNum, pageSize, model);

        if (memberFriendInfo != null ) {

            PageInfo<MemberVO> result = new PageInfo<MemberVO>();
            result.setPageSize(pageSize);
            result.setPageNum(pageNum);
            result.setTotal(memberFriendInfo.getTotal());

            if(!memberFriendInfo.getList().isEmpty()) {
                List<Long> friendMemberIdList = new LinkedList<Long>();

                for (MemberFriend memberFriend : memberFriendInfo.getList()) {
                    friendMemberIdList.add(memberFriend.getFriendMemberId());
                }

                Example example = new Example(Member.class);
                example.or().andIn(Member.ID, friendMemberIdList);

                List<Member> memberList = memberService.queryList(example);

                List<MemberVO> memberVOList = new LinkedList<MemberVO>();

                for (Member member1 : memberList) {
                    MemberVO memberVO = new MemberVO();
                    BeanUtils.copyProperties(member1, memberVO);
                    memberVOList.add(memberVO);
                }
                result.setList(memberVOList);
            }
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(result);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("读取好友列表失败！");
        }
        return restResponse;
    }

    /**
     * 申请添加好友（本质是发送了一条消息在messageController）
     *
     * @param friendId
     * @return
     */
    @RequestMapping(value = "/applyAddFriends")
    @ResponseBody
    public RestResponse<Void> applyAddFriends(@RequestParam(required = true) long friendId) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();

        Message message = new Message();
        message.setStatus("normal");
        message.setCategory("friend");
        message.setFromMemberId(member.getId());
        message.setToMemberId(friendId);
        message.setProcessStatus("unprocessed");
        message.setUpdateAt(new Date());

        if (messageService.insert(message)) {
            restResponse.setCode(RestResponse.OK);

        } else {
            restResponse.setCode("error");
            restResponse.setMessage("添加好友成功!");
        }
        return restResponse;
    }

    /**
     * 添加好友
     *
     * @param friendId
     * @return
     */
    @RequestMapping(value = "/addFriends")
    @ResponseBody
    public RestResponse<Void> addFriends(@RequestParam(required = true) long friendId) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();

        if (memberFriendService.addFriend(member.getId(), friendId)) {
            restResponse.setCode(RestResponse.OK);

        } else {
            restResponse.setCode("error");
            restResponse.setMessage("添加好友成功!");
        }
        return restResponse;
    }

    /**
     * 删除好友
     *
     * @param friendId
     * @return
     */
    @RequestMapping(value = "/deleteFriends")
    @ResponseBody
    public RestResponse<Void> deleteFriends(@RequestParam(required = true) long friendId) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();

        MemberFriend model = new MemberFriend();
        model.setMemberId(member.getId());
        model.setFriendMemberId(friendId);
        model.setStatus("normal");

        List<MemberFriend> memberFriendList = memberFriendService.queryList(model);

        if (memberFriendList != null) {

            for (MemberFriend memberFriend : memberFriendList) {
                memberFriend.setStatus("delete");
                memberFriendService.update(memberFriend);
                restResponse.setCode(RestResponse.OK);
            }

        } else {
            restResponse.setCode("error");
            restResponse.setMessage("没有该好友!");
        }
        return restResponse;
    }

}
