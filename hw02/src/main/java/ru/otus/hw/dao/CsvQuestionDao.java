package ru.otus.hw.dao;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try {
            final URL resource = getClass().getClassLoader().getResource(fileNameProvider.getTestFileName());
            try (InputStreamReader streamReader = new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(streamReader)) {

                final CsvToBean<QuestionDto> csvToBean = new CsvToBean<>();
                csvToBean.setCsvReader(buildReader(reader));
                csvToBean.setMappingStrategy(buildMappingStrategy());
                return csvToBean.parse()
                        .stream()
                        .skip(1)
                        .map(QuestionDto::toDomainObject)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new QuestionReadException("Ошибка чтения вопросов из файла " + fileNameProvider.getTestFileName(), e);
        }
    }

    private ColumnPositionMappingStrategy<QuestionDto> buildMappingStrategy() {
        final ColumnPositionMappingStrategy<QuestionDto> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(QuestionDto.class);
        return strategy;
    }

    private CSVReader buildReader(final BufferedReader reader) {
        return new CSVReaderBuilder(reader)
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(';')
                        .build())
                .build();
    }
}
