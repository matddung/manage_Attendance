package com.example.Attendance.board.question;

import com.example.Attendance.Util.RsData;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
        Member isLoginedMember = memberService.getCurrentMember();

        if (isLoginedMember == null) {
            return "redirect:/";
        }

        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }

    @GetMapping("/create")
    public String showCreateQuestion() {
        Member isLoginedMember = memberService.getCurrentMember();

        if (isLoginedMember == null) {
            return "redirect:/";
        }

        return "question_create";
    }

    @PostMapping("/create")
    public String doCreateQuestion(@RequestParam String subject, @RequestParam String content) {
        Member isLoginedMember = memberService.getCurrentMember();

        if (isLoginedMember == null) {
            return "redirect:/";
        }

        RsData<Question> question = questionService.create(isLoginedMember, subject, content);

        return "redirect:/question";
    }

    @GetMapping("/detail/{id}")
    public String showDetailQuestion(Model model, @PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentMember();

        if (isLoginedMember == null) {
            return "redirect:/";
        }

        Question question = questionService.findById(id).get();

        model.addAttribute("question", question);
        model.addAttribute("writer", question.getWriter().getId() == (isLoginedMember.getId()));

        return "question_detail";
    }

    @GetMapping("/modify/{id}")
    public String showModifyQuestion(Model model, @PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentMember();

        if (isLoginedMember == null) {
            return "redirect:/";
        }

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
        Member isLoginedMember = memberService.getCurrentMember();

        if (isLoginedMember == null) {
            return "redirect:/";
        }

        if (question.getWriter().getId() != isLoginedMember.getId()) {
            return "redirect:/question/detail" + id;
        }

        questionService.delete(id);

        return "redirect:/question";
    }
}
