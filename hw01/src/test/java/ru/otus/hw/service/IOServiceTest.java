package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IOServiceTest {

    @Test
    void test() {
        String testString = "TEST";
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(outputStream)) {
            IOService ioService = mock(IOService.class);
            doAnswer(invocation -> {
                printStream.println((String) invocation.getArgument(0));
                return null;
            }).when(ioService).printLine(testString);

            List<Question> records = new ArrayList<>();
            records.add(new Question("testText?", Collections.singletonList(new Answer("testAnswer", true))));

            final QuestionDao questionDao = mock(QuestionDao.class);
            when(questionDao.findAll()).thenReturn(records);

            TestServiceImpl testService = new TestServiceImpl(ioService, questionDao);
            testService.executeTest();
            assertTrue(outputStream.toString().contains("testText?"));
            assertTrue(outputStream.toString().contains("testAnswer"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}