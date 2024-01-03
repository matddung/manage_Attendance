package com.example.Attendance.board.answer;

import com.example.Attendance.board.question.Question;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer createComment(Question question, String content) {
        Answer answer = Answer.builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .question(question)
                .build();

        return answerRepository.save(answer);
    }

    @Transactional
    public Answer modify(Long id, String content) {
        LocalDateTime now = LocalDateTime.now();

        Answer answer = answerRepository.findById(id).orElse(null);

        if (answer == null) {
            throw new RuntimeException("댓글을 찾아올 수 없습니다.");
        }

        answer.setContent(content);
        answer.setModifyDate(now);

        return answerRepository.save(answer);
    }

    @Transactional
    public Answer delete(long id) {
        Optional<Answer> isAnswer = answerRepository.findById(id);

        if (isAnswer.isEmpty()) {
            throw new RuntimeException("댓글을 찾아올 수 없습니다.");
        }

        Answer answer = isAnswer.get();
        answerRepository.delete(answer);

        return answer;
    }

    public Optional<Answer> findById(long id) {
        return answerRepository.findById(id);
    }
}
