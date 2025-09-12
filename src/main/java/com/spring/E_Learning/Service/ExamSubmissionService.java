package com.spring.E_Learning.Service;


import com.spring.E_Learning.DTOs.ExamAnswerDto;
import com.spring.E_Learning.DTOs.ExamSubmissionRequest;
import com.spring.E_Learning.DTOs.ExamSubmissionResponse;
import com.spring.E_Learning.Enum.RequestStatus;
import com.spring.E_Learning.Enum.RequestType;
import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Model.*;
import com.spring.E_Learning.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamSubmissionService {
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final ExamSubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final StudentRequestRepository studentRequestRepository;

    public ExamSubmissionResponse submitExam(int studentId, ExamSubmissionRequest request) {
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new EntityNotFoundException("Exam not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        if (!"STUDENT".equalsIgnoreCase(student.getRole().name())) {
            throw new RuntimeException("Only students can submit exams");
        }

        Boolean alreadySubmitted = submissionRepository
                .existsByExamIdAndStudentId(exam.getId(), student.getId());

        StudentRequest retakeRequest = null;

        if (alreadySubmitted) {
            retakeRequest = studentRequestRepository
                    .findTopByStudentIdAndExamIdAndTypeAndStatus(
                            student.getId(),
                            exam.getId(),
                            RequestType.RETAKE_EXAM,
                            RequestStatus.APPROVED
                    ).orElse(null);

            if (retakeRequest == null) {
                throw new RuntimeException("You have already submitted this exam");
            }
        }

        ExamSubmission submission = ExamSubmission.builder()
                .exam(exam)
                .student(student)
                .submittedAt(LocalDateTime.now())
                .build();

        int score = 0;
        List<ExamAnswer> answers = new ArrayList<>();

        for (ExamAnswerDto dto : request.getAnswers()) {
            Question question = questionRepository.findById(dto.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Question not found"));

            Option selected = optionRepository.findById(dto.getSelectedOptionId())
                    .orElseThrow(() -> new EntityNotFoundException("Option not found"));

            boolean isCorrect = Boolean.TRUE.equals(selected.isCorrect());
            if (isCorrect) score++;

            ExamAnswer answer = ExamAnswer.builder()
                    .submission(submission)
                    .question(question)
                    .selectedOption(selected)
                    .isCorrect(isCorrect)
                    .build();

            answers.add(answer);
        }

        submission.setScore(score);
        submission.setAnswers(answers);

        ExamSubmission savedSubmission = submissionRepository.save(submission);

        if (retakeRequest != null) {
            retakeRequest.setStatus(RequestStatus.EXPIRED);
            studentRequestRepository.save(retakeRequest);
        }

        return new ExamSubmissionResponse(
                savedSubmission.getId(),
                savedSubmission.getScore(),
                savedSubmission.getSubmittedAt()
        );
    }


    public List<ExamSubmissionResponse> getExamResults(int examId) {
        return submissionRepository.findByExamId(examId)
                .stream()
                .map(s -> new ExamSubmissionResponse(
                        s.getId(),
                        s.getScore(),
                        s.getSubmittedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<ExamSubmissionResponse> getStudentResults(int studentId) {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userRepository.findUserByEmail(loggedInEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!loggedInUser.getRole().equals(Role.STUDENT)) {
            throw new RuntimeException("Only students can view results");
        }

        if (loggedInUser.getId() != studentId) {
            throw new RuntimeException("You are not allowed to view other students' results");
        }

        return submissionRepository.findByStudentId(studentId)
                .stream()
                .map(s -> new ExamSubmissionResponse(
                        s.getId(),
                        s.getScore(),
                        s.getSubmittedAt()
                ))
                .collect(Collectors.toList());
    }


}
