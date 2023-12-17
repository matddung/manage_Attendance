package com.example.Attendance.note;

import com.example.Attendance.Util.RsData;
import com.example.Attendance.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public List<Note> noteList(long id) {
        return noteRepository.findByAddressee(id);
    }

    @Transactional
    public RsData<Note> sendNote(Member sender, Member addressee, String subject, String content) {
        LocalDateTime now = LocalDateTime.now();

        Note note = Note.builder()
                .sender(sender)
                .addressee(addressee)
                .subject(subject)
                .content(content)
                .sendDate(LocalDateTime.now())
                .isRead(false)
                .build();

        note = noteRepository.save(note);

        return new RsData<>("S-1", "쪽지가 성공적으로 전송되었습니다.", note);
    }

    @Transactional
    public Note readNote(Long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Note Id:" + id));
        note.setRead(true);
        note.setReadDate(LocalDateTime.now());
        return noteRepository.save(note);
    }

    @Transactional
    public RsData<Note> delete(Long id) {
        Note note = noteRepository.findById(id).orElse(null);

        noteRepository.delete(note);

        return new RsData<>("S-1", "쪽지가 삭제되었습니다.", note);
    }

    public Optional<Note> findById(long id) {
        return noteRepository.findById(id);
    }
}