package fr.insee.rem.controller.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    private Integer code;
    private String path;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    /**
     * @param status       http status for this error
     * @param request      request initiating the exception
     * @param timestamp    timestamp of the generated error
     * @param errorMessage error message
     */
    public ApiError(HttpStatus status, WebRequest request, LocalDateTime timestamp, String errorMessage) {
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = status.getReasonPhrase();
        }
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        createApiError(status.value(), path, timestamp, errorMessage);
    }

    private void createApiError(int code, String path, LocalDateTime timestamp, String errorMessage) {
        this.code = code;
        this.path = path;
        this.message = errorMessage;
        this.timestamp = timestamp;
    }
}
