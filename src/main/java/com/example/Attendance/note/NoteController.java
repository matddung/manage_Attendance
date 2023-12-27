package com.example.Attendance.note;

import com.example.Attendance.Util.RsData;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;
    private final MemberService memberService;

    @GetMapping("")
    public String sendNoteList() {
        Member isLoginedMember = memberService.getCurrentMember();

        if(isLoginedMember == null) {
            return "redirect:/";
        }

        noteService.sendNoteList(isLoginedMember.getId());
        return "note_main";
    }

    @GetMapping("/receive")
    public String receiveNoteList() {
        Member isLoginedMember = memberService.getCurrentMember();

        if(isLoginedMember == null) {
            return "redirect:/";
        }

        noteService.receiveNoteList(isLoginedMember.getId());
        return "note_main";
    }

    @GetMapping("/read/{id}")
    public String readNote(@PathVariable Long id) {
        noteService.readNote(id);
        return "redirect:/note/detail/" + id;
    }

    @GetMapping("/send")
    public String showSendNoteForm(Model model) {
        Member isLoginedMember = memberService.getCurrentMember();

        if (isLoginedMember == null) {
            return "redirect:/";
        }

        return "note_send";
    }

    @PostMapping("/send")
    public String doSendNote(@RequestParam String subject, @RequestParam String content, @RequestParam Member addressee) {
        Member isLoginedMember = memberService.getCurrentMember();

        if (isLoginedMember == null) {
            return "redirect:/";
        }

        RsData<Note> note = noteService.sendNote(isLoginedMember, addressee, subject, content);

        return "redirect:/question";
    }

    @PostMapping("/delete/{id}")
    public String doDeleteNote(@PathVariable Long id) {
        Member isLoginedMember = memberService.getCurrentMember();

        if (isLoginedMember == null) {
            return "redirect:/";
        }

        noteService.delete(id);

        return "redirect:/note";
    }
}
