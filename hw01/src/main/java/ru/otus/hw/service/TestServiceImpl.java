package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        final List<Question> all = questionDao.findAll();
        for (Question question : all) {
            ioService.printLine(question.text() + " "
                    + (question.answers().isEmpty() ? "" : "\r\nAnswers:\r\n" + question.answers()
                    .stream()
                    .map(answer -> " - " + answer.text())
                    .collect(Collectors.joining(";\r\n"))));
            ioService.printLine("");
        }
    }
}
