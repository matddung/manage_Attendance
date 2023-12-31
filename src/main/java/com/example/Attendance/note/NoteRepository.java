package com.example.Attendance.note;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByAddresseeId(long id);
    List<Note> findBySenderId(long id);
}
