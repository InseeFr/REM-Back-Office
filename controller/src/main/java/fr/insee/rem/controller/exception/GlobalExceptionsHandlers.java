package fr.insee.rem.controller.exception;

import fr.insee.rem.domain.exception.PartitionAlreadyExistsException;
import fr.insee.rem.domain.exception.PartitionNotFoundException;
import fr.insee.rem.domain.exception.SettingsException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandlers {

    /**
     * Global method to process the catched exception
     *
     * @param ex      Exception catched
     * @param status  status linked with this exception
     * @param request request initiating the exception
     * @return the apierror object with associated status code
     */
    private ResponseEntity<ApiError> processException(final Exception ex, final HttpStatus status,
                                                      final WebRequest request) {
        log.error(ex.getMessage());
        ApiError error = new ApiError(status, request, LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(PartitionNotFoundException.class)
    public ResponseEntity<ApiError> exceptionPartitionNotFoundHandler(final WebRequest request,
                                                                      final PartitionNotFoundException exception) {
        return processException(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(SurveyUnitNotFoundException.class)
    public ResponseEntity<ApiError> exceptionSurveyUnitNotFoundHandler(
            final WebRequest request, final SurveyUnitNotFoundException exception) {
        return processException(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(CsvFileException.class)
    public ResponseEntity<ApiError> exceptionCsvFileHandler(final WebRequest request,
                                                            final CsvFileException exception) {
        log.error("exceptionCsvFileHandler  : " + exception.getMessage());
        return processException(exception, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(PartitionAlreadyExistsException.class)
    public ResponseEntity<ApiError> exceptionPartitionAlreadyExistsHandler(
            final WebRequest request, final PartitionAlreadyExistsException exception) {
        log.error("exceptionPartitionAlreadyExistsHandler  : " + exception.getMessage());
        return processException(exception, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(SettingsException.class)
    public ResponseEntity<ApiError> exceptionSettingsHandler(final WebRequest request,
                                                             final SettingsException exception) {
        log.error("exceptionSettingsHandler  : " + exception.getMessage());
        return processException(exception, HttpStatus.BAD_REQUEST, request);
    }

}
