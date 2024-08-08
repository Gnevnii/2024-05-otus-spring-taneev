package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            String answer;
            boolean isValidNumber = true;
            do {
                if (!isValidNumber) {
                    ioService.printLine("Incorrect answer number. Try again.");
                }

                answer = ioService.readStringWithPrompt(generateQuestionString(question));
                isValidNumber = isValidNumber(question, answer);
            } while (!isValidNumber);

            var isAnswerValid = question.answers().get(Integer.parseInt(answer) - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean isValidNumber(final Question question, final String answer) {
        boolean isValidNumber;
        int answerNumber;
        isValidNumber = NumberUtils.isCreatable(answer);
        if (isValidNumber) {
            answerNumber = Integer.parseInt(answer);
            isValidNumber = answerNumber <= question.answers().size() && answerNumber > 0;
        }
        return isValidNumber;
    }

    private String generateQuestionString(final Question question) {
        return question.text() + " "
                + (question.answers().isEmpty() ? "" : "\r\nAnswers:\r\n" + question.answers()
                .stream()
                .map(a -> question.answers().indexOf(a) + 1 + ". " + a.text())
                .collect(Collectors.joining(";\r\n")));
    }
}
