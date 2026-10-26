package in.ac.iiitt.atrang.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public class ErrorResponse {
    private boolean success = false;
    private int status;
    private String error;
    private String message;
    private Map<String, String> validationErrors;
    private OffsetDateTime timestamp = OffsetDateTime.now();

    public ErrorResponse() {}

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = OffsetDateTime.now();
    }

    public ErrorResponse(int status, String error, String message, Map<String, String> validationErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.validationErrors = validationErrors;
        this.timestamp = OffsetDateTime.now();
    }

    public boolean isSuccess() { return success; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public Map<String, String> getValidationErrors() { return validationErrors; }
    public OffsetDateTime getTimestamp() { return timestamp; }
}
