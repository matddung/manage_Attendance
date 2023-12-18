package com.example.Attendance.board.question;

import com.example.Attendance.Util.RsData;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;

    @GetMapping("")
    public String showQuestionList(Model model) {
        List<Question> questions = new java.util.ArrayList<>(questionService.getList().stream()
                .sorted(Comparator.comparing(Question::getCreateDate))
                .toList());

        Collections.reverse(questions);
        model.addAttribute("questions", questions);


        try {
            Member isLoginedMember = memberService.getCurrentUser();
            model.addAttribute("isLoginedMember", isLoginedMember);
            return "question_list";
        } catch (NullPointerException e) {
            return "main";
        }
    }

    @GetMapping("/create")
    public String showCreateQuestion() {
        Member isLoginedMember = memberService.getCurrentUser();

        if (isLoginedMember == null) {
            return "main";
        }

        return "question_create";
    }

    @PostMapping("/create")
    public String doCreateQuestion(@RequestParam String subject, @RequestParam String content) {
        Member isLoginedMember = memberService.getCurrentUser();

        RsData<Question> question = questionService.create(isLoginedMember, subject, content);

        return "redirect:/question";
    }

    @GetMapping("/detail/{id}")
    public String showDetailQuestion(Model model, @PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentUser();

        if (isLoginedMember == null) {
            return "main";
        }

        Question question = questionService.findById(id).get();
        int hit = question.getHit() + 1;

        Question qs = Question.builder()
                .hit(hit)
                .build();

        question.updateHit(hit);
        questionService.updateHit(question.getId(), qs);

        model.addAttribute("question", question);
        model.addAttribute("writer", question.getWriter().getId() == (isLoginedMember.getId()));

        return "question_detail";
    }

    @GetMapping("/modify/{id}")
    public String showModifyQuestion(Model model, @PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentUser();

        Question question = questionService.findById(id).get();

        if (question.getWriter().getId() != isLoginedMember.getId()) {
            return "redirect:/detail/" + id;
        }

        model.addAttribute("question", question);

        return "question_modify";
    }

    @PostMapping("/modify/{id}")
    public String doModifyQuestion(@PathVariable Long id, @RequestParam String subject, @RequestParam String content) {
        questionService.modify(id, subject, content);
        return "redirect:/question/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String doDeleteQuestion(@PathVariable Long id) {
        Question question = questionService.findById(id).orElse(null);
        Member isLoginedMember = memberService.getCurrentUser();

        if (question.getWriter().getId() != isLoginedMember.getId()) {
            return "redirect:/question/detail" + id;
        }

        questionService.delete(id);

        return "redirect:/question";
    }
}
