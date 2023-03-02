package fr.insee.rem.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import fr.insee.rem.domain.exception.SampleAlreadyExistsException;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandlers {

    @ExceptionHandler(SampleNotFoundException.class)
    public ResponseEntity<SampleNotFoundException> exceptionSampleNotFoundHandler(final HttpServletRequest req, final SampleNotFoundException exception) {
        log.error("exceptionSampleNotFoundHandler  : " + exception.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SurveyUnitNotFoundException.class)
    public ResponseEntity<SurveyUnitNotFoundException> exceptionSurveyUnitNotFoundHandler(
        final HttpServletRequest req,
        final SurveyUnitNotFoundException exception) {
        log.error("exceptionSurveyUnitNotFoundHandler  : " + exception.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CsvFileException.class)
    public ResponseEntity<CsvFileException> exceptionCsvFileHandler(final HttpServletRequest req, final CsvFileException exception) {
        log.error("exceptionCsvFileHandler  : " + exception.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SampleAlreadyExistsException.class)
    public ResponseEntity<SampleAlreadyExistsException> exceptionSampleAlreadyExistsHandler(
        final HttpServletRequest req,
        final SampleAlreadyExistsException exception) {
        log.error("exceptionSampleAlreadyExistsHandler  : " + exception.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
    }

}
