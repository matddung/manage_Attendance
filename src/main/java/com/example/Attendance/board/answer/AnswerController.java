package com.example.Attendance.board.answer;

import com.example.Attendance.board.question.Question;
import com.example.Attendance.board.question.QuestionService;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final MemberService memberService;

    @PostMapping("/create/{id}")
    public String createAnswer(@PathVariable("id") long id, @RequestParam String content) {
        Question question = questionService.findById(id).get();
        answerService.createComment(question, content);
        return "redirect:/question/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteAnswer(@PathVariable long id, RedirectAttributes redirectAttributes) {
        Answer answer = answerService.findById(id).orElse(null);
        long answerId = answerService.getQuestionIdByAnswerId(id);
        Member isLoginedMember = memberService.getCurrentMember();

        if (answer.getWriter().getId() != isLoginedMember.getId()) {
            redirectAttributes.addFlashAttribute("message", "댓글 삭제 권한이 없습니다.");
            return "/question/detail/" + answerId;
        }

        answerService.delete(id);

        redirectAttributes.addFlashAttribute("message", "댓글 삭제 완료");
        return "/question/detail/" + answer.getQuestion().getId();
    }

    @PostMapping("/modify/{id}")
    public String modifyAnswer(@PathVariable Long id, @RequestParam String content) {
        answerService.modify(id, content);
        return "redirect:/question/detail/" + id;
    }
}
