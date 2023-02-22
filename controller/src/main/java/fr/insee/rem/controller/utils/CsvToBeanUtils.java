package fr.insee.rem.controller.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import fr.insee.rem.controller.exception.CsvFileException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CsvToBeanUtils {

    private CsvToBeanUtils() {
        throw new UnsupportedOperationException("Utility class and cannot be instantiated");
    }

    public static <T> List<T> parse(MultipartFile csvFile, Class<T> targetType) throws CsvFileException {
        try (Reader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {

            CsvToBean<T> csvToBean =
                new CsvToBeanBuilder<T>(reader).withType(targetType).withSeparator(';').withIgnoreLeadingWhiteSpace(true).withEscapeChar('\0')
                    .withThrowExceptions(false).build();

            List<T> t = csvToBean.parse();

            if ( !csvToBean.getCapturedExceptions().isEmpty()) {
                csvToBean.getCapturedExceptions().stream().forEach(e -> log.error(e.getMessage(), e));
                throw new CsvFileException("File read error");
            }

            return t;
        }
        catch (Exception e) {
            throw new CsvFileException("File read error", e);
        }

    }
}
