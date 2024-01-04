package com.example.Attendance.note;

import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;
    private final MemberService memberService;

    @GetMapping("/sendList")
    public List<Note> sendNoteList() {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return noteService.sendNoteList(isLoginedMember.getId());
    }

    @GetMapping("/receiveList")
    public List<Note> receiveNoteList() {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return noteService.receiveNoteList(isLoginedMember.getId());
    }

    @GetMapping("/read/{id}")
    public Note readNote(@PathVariable Long id) {
        return noteService.readNote(id);
    }

    @PostMapping("/send")
    public Note doSendNote(@RequestParam(name = "subject") String subject,
                           @RequestParam(name = "content") String content,
                           @RequestParam(name = "addressee") Member addressee) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Note note = noteService.sendNote(isLoginedMember, addressee, subject, content);
        return note;
    }

    @PostMapping("/delete/{id}")
    public Note doDeleteNote(@PathVariable Long id) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return noteService.delete(id);
    }
}