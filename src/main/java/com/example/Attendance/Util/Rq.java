package com.example.Attendance.Util;

import com.example.Attendance.Util.Ut;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class Rq {
    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private Member member = null;
    private final User user;

    public Rq(MemberService memberService, HttpServletRequest req, HttpServletResponse resp) {
        this.memberService = memberService;
        this.req = req;
        this.resp = resp;

        // 현재 로그인한 회원의 인증정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            this.user = (User) authentication.getPrincipal();
        } else {
            this.user = null;
        }
    }

    private String getLoginedMemberUsername() {
        if (isLogout()) return null;

        return user.getUsername();
    }

    public boolean isLogin() {
        return user != null;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public Member getMember() {
        if (isLogout()) {
            return null;
        }

        if (member == null) {
            member = memberService.findByMemberId(getLoginedMemberUsername()).get();
        }

        return member;
    }

    public String historyBack(String msg) {
        String referer = req.getHeader("referer");
        String key = "historyBackFailMsg___" + referer;
        req.setAttribute("localStorageKeyAboutHistoryBackFailMsg", key);
        req.setAttribute("historyBackFailMsg", (msg));
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        return "common/js";
    }

    public String redirect(String url, String msg) {
        return "redirect:" + Ut.url.modifyQueryParam(url, "msg", Ut.url.encode(msg));
    }
}