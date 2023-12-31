package com.example.Attendance.board.question;

import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;

    @GetMapping("/list")
    public Page<Question> showListQuestion(@RequestParam(name = "page", value = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "kw", value = "kw", defaultValue = "") String kw) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Page<Question> paging = this.questionService.getList(page, kw);
        return paging;
    }

    @GetMapping("/detail/{id}")
    public Question showDetailQuestion(@PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Question question = questionService.findById(id).get();
        return question;
    }

    @PostMapping("/freeBoardCreate")
    public Question doFreeBoardCreateQuestion(@RequestParam(name = "subject") String subject,
                                              @RequestParam(name = "content") String content) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Question question = questionService.freeBoardCreate(isLoginedMember, subject, content);
        return question;
    }

    @PostMapping("/noticeBoardCreate")
    public Question doNoiceBoardCreateQuestion(@RequestParam(name = "subject") String subject,
                                               @RequestParam(name = "content") String content) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (!memberService.getCurrentMember().isAdmin()) {
            throw new RuntimeException("관리자 권한이 필요합니다.");
        }
        Question question = questionService.noticeBoardCreate(isLoginedMember, subject, content);
        return question;
    }

    @PostMapping("/modify/{id}")
    public Question doModifyQuestion(@PathVariable Long id,
                                     @RequestParam(name = "subject") String subject,
                                     @RequestParam(name = "content") String content) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return questionService.modify(id, subject, content);
    }

    @PostMapping("/delete/{id}")
    public Question doDeleteQuestion(@PathVariable Long id) {
        Question question = questionService.findById(id).orElse(null);
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        if (question.getWriter().getId() != isLoginedMember.getId()) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다.");
        }
        return questionService.delete(id);
    }
}