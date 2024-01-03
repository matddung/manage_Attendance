package com.example.Attendance.note;

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

    public List<Note> sendNoteList(long id) {
        return noteRepository.findByAddresseeId(id);
    }

    public List<Note> receiveNoteList(long id) {
        return noteRepository.findBySenderId(id);
    }

    @Transactional
    public Note sendNote(Member sender, Member addressee, String subject, String content) {
        Note note = Note.builder()
                .sender(sender)
                .addressee(addressee)
                .subject(subject)
                .content(content)
                .sendDate(LocalDateTime.now())
                .isRead(false)
                .build();

        return noteRepository.save(note);
    }

    @Transactional
    public Note readNote(Long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Note Id:" + id));
        note.setRead(true);
        note.setReadDate(LocalDateTime.now());
        return noteRepository.save(note);
    }

    @Transactional
    public Note delete(Long id) {
        Note note = noteRepository.findById(id).orElse(null);

        noteRepository.delete(note);

        return note;
    }

    public Optional<Note> findById(long id) {
        return noteRepository.findById(id);
    }
}