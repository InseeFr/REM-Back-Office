package fr.insee.rem.controller.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import fr.insee.rem.controller.exception.CsvFileException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class BeanToCsvUtils {

    private BeanToCsvUtils() {
        throw new UnsupportedOperationException("Utility class and cannot be instantiated");
    }

    public static <T> ByteArrayInputStream write(List<T> sources) throws CsvFileException {
        try (
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
            CSVWriter writer =
                new CSVWriter(streamWriter, ';', ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.DEFAULT_LINE_END)) {

            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer).build();
            beanToCsv.write(sources);

            if ( !beanToCsv.getCapturedExceptions().isEmpty()) {
                beanToCsv.getCapturedExceptions().stream().forEach(e -> log.error(e.getMessage(), e));
                throw new CsvFileException("File write error");
            }

            streamWriter.flush();

            return new ByteArrayInputStream(stream.toByteArray());

        }
        catch (Exception e) {
            throw new CsvFileException("File write error", e);
        }

    }
}
