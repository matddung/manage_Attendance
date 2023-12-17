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

    @GetMapping("/")
    public String listNote() {
        long id = memberService.getCurrentUser().getId();
        noteService.noteList(id);
        return "note_main";
    }

    @GetMapping("/read/{id}")
    public String readNote(@PathVariable Long id) {
        noteService.readNote(id);
        return "redirect:/note/detail/" + id;
    }

    @GetMapping("/send")
    public String showSendNoteForm(Model model) {
        Member isLoginedUser = memberService.getCurrentUser();

        if (isLoginedUser == null) {
            return "main";
        }

        return "note_send";
    }

    @PostMapping("/send")
    public String doSendNote(@RequestParam String subject, @RequestParam String content, @RequestParam Member addressee) {
        Member isLoginedUser = memberService.getCurrentUser();

        RsData<Note> note = noteService.sendNote(isLoginedUser, addressee, subject, content);

        return "redirect:/question";
    }

    @PostMapping("/delete/{id}")
    public String doDeleteNote(@PathVariable Long id) {
        Note note = noteService.findById(id).orElse(null);
        Member isLoginedMember = memberService.getCurrentUser();

        noteService.delete(id);

        return "redirect:/note";
    }
}
