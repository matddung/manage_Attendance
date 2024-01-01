package com.example.Attendance.note;

import com.example.Attendance.Util.RsData;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
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
        if(isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return noteService.sendNoteList(isLoginedMember.getId());
    }

    @GetMapping("/receiveList")
    public List<Note> receiveNoteList() {
        Member isLoginedMember = memberService.getCurrentMember();
        if(isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return noteService.receiveNoteList(isLoginedMember.getId());
    }

    @GetMapping("/read/{id}")
    public Note readNote(@PathVariable Long id) {
        return noteService.readNote(id);
    }

    @PostMapping("/send")
    public Note doSendNote(@Parameter(name = "subject") @RequestParam String subject,
                           @Parameter(name = "content") @RequestParam String content,
                           @Parameter(name = "addressee") @RequestParam Member addressee) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        RsData<Note> note = noteService.sendNote(isLoginedMember, addressee, subject, content);
        return note.getData();
    }

    @PostMapping("/delete/{id}")
    public Note doDeleteNote(@PathVariable Long id) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return noteService.delete(id).getData();
    }
}