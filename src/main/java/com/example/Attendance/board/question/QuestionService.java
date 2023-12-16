package com.example.Attendance.board.question;

import com.example.Attendance.Util.RsData;
import com.example.Attendance.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public RsData<Question> create(Member writer, String subject) {
        LocalDateTime now = LocalDateTime.now();

        Question question = Question.builder()
                .subject(subject)
                .writer(writer)
                .createDate(now)
                .recommend(0)
                .hit(0)
                .build();

        question = questionRepository.save(question);

        return new RsData<>("S-1", "게시물이 생성되었습니다.", question);
    }

    @Transactional
    public RsData<Question> modify(Long id, String subject) {
        LocalDateTime now = LocalDateTime.now();

        Question question = questionRepository.findById(id).orElse(null);

        if (question == null) {
            return new RsData<>("F-1", "게시물을 찾아 올 수 없습니다.", question);
        }

        question.setSubject(subject);
        question.setModifyDate(now);

        questionRepository.save(question);

        return new RsData<>("S-1", "게시물 수정 완료", question);
    }

    @Transactional
    public RsData<Question> delete(Long id) {
        Question question = questionRepository.findById(id).orElse(null);

        if (question == null) {
            return new RsData<>("F-1", "게시물을 찾아 올 수 없습니다.", null);
        }

        questionRepository.delete(question);

        return new RsData<>("S-1", "게시물 삭제 완료", question);
    }

    public List<Question> getList() {
        return questionRepository.findAll();
    }

    public Optional<Question> findById(long id) {
        return questionRepository.findById(id);
    }

    @Transactional
    public void updateHit(long id, Question question) {
        question = questionRepository.findById(id).get();
        question.updateHit(question.getHit());
    }
}
