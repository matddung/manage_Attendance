package com.example.Attendance.board.answer;

import com.example.Attendance.board.question.Question;
import com.example.Attendance.board.question.QuestionService;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final MemberService memberService;

    @PostMapping("/create/{id}")
    public Answer createAnswer(@PathVariable("id") long id,
                               @Parameter(name = "content") @RequestParam String content) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Question question = questionService.findById(id).get();
        return answerService.createComment(question, content);
    }

    @PostMapping("/delete/{id}")
    public Answer deleteAnswer(@PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Answer answer = answerService.findById(id).orElse(null);
        if (answer.getWriter().getId() != isLoginedMember.getId()) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.");
        }
        return answerService.delete(id);
    }

    @PutMapping("/modify/{id}")
    public Answer modifyAnswer(@PathVariable Long id,
                               @Parameter(name = "content") @RequestParam String content) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return answerService.modify(id, content);
    }
}
