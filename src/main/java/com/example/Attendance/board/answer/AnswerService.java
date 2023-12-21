package com.example.Attendance.board.answer;

import com.example.Attendance.Util.RsData;
import com.example.Attendance.board.question.Question;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final MemberService memberService;

    public RsData<Answer> createComment(Question question, String content) {
        LocalDateTime now = LocalDateTime.now();
        Member isLoginedMember = memberService.getCurrentMember();

        Answer answer = Answer.builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .question(question)
                .build();

        answerRepository.save(answer);

        return new RsData<>("S-1", "댓글 등록 완료", answer);
    }

    @Transactional
    public RsData<Answer> modify(Long id, String content) {
        LocalDateTime now = LocalDateTime.now();

        Answer answer = answerRepository.findById(id).orElse(null);

        if (answer == null) {
            return new RsData<>("F-1", "댓글을 찾아 올 수 없습니다.", answer);
        }

        answer.setContent(content);
        answer.setModifyDate(now);

        answerRepository.save(answer);

        return new RsData<>("S-1", "댓글 수정 완료", answer);
    }

    @Transactional
    public RsData<Answer> delete(long id) {
        Optional<Answer> isAnswer = answerRepository.findById(id);

        if (isAnswer.isEmpty()) {
            return new RsData<>("F-1", "댓글을 찾아 올 수 없습니다.", null);
        }

        Answer answer = isAnswer.get();
        answerRepository.delete(answer);

        return new RsData<>("S-1", "댓글 삭제 완료", answer);
    }

    public Optional<Answer> findById(long id) {
        return answerRepository.findById(id);
    }

    public long getQuestionIdByAnswerId(long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Answer Id :" + answerId));
        return answer.getQuestion().getId();
    }
}
