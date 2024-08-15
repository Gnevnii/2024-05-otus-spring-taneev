package ru.otus.hw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AppProperties implements TestFileNameProvider, TestConfig {

    private int rightAnswersCountToPass;

    private String testFileName;

    public AppProperties(@Value("${test.rightAnswersCountToPass}") final int rightAnswersCountToPass,
                         @Value("${test.fileName}") final String testFileName) {
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.testFileName = testFileName;
    }
}