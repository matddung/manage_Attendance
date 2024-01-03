package com.example.Attendance.board.question;

import com.example.Attendance.board.answer.Answer;
import com.example.Attendance.member.Member;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public Question freeBoardCreate(Member writer, String subject, String content) {
        Question question = Question.builder()
                .subject(subject)
                .content(content)
                .writer(writer)
                .createDate(LocalDateTime.now())
                .boardId(2)
                .build();

        return questionRepository.save(question);
    }

    @Transactional
    public Question noticeBoardCreate(Member writer, String subject, String content) {
        Question question = Question.builder()
                .subject(subject)
                .content(content)
                .writer(writer)
                .createDate(LocalDateTime.now())
                .boardId(1)
                .build();

        return questionRepository.save(question);
    }

    @Transactional
    public Question modify(Long id, String subject, String content) {
        LocalDateTime now = LocalDateTime.now();

        Question question = questionRepository.findById(id).orElse(null);

        if (question == null) {
            throw new RuntimeException("게시물을 찾아올 수 없습니다.");
        }

        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(now);

        return questionRepository.save(question);
    }

    @Transactional
    public Question delete(Long id) {
        Question question = questionRepository.findById(id).orElse(null);

        if (question == null) {
            throw new RuntimeException("게시물을 찾아올 수 없습니다.");
        }

        questionRepository.delete(question);

        return question;
    }

    public Optional<Question> findById(long id) {
        return questionRepository.findById(id);
    }

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
    }

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                Join<Question, Member> u1 = q.join("writer", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, Member> u2 = a.join("writer", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"),
                        cb.like(q.get("content"), "%" + kw + "%"),
                        cb.like(u1.get("memberId"), "%" + kw + "%"),
                        cb.like(a.get("content"), "%" + kw + "%"),
                        cb.like(u2.get("memberId"), "%" + kw + "%"));
            }
        };
    }
}
