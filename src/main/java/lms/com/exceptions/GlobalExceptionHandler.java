package lms.com.exceptions;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lms.com.common.LMSResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public LMSResponse handleAll(Exception ex, HttpServletRequest req) {
        return LMSResponse.fail("Unexpected error occurred.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public LMSResponse handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest req) {
        return LMSResponse.fail("Invalid argument: " + ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public LMSResponse handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest req) {
        return LMSResponse.fail("Entity not found: " + ex.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    public LMSResponse handleDuplicateException(DuplicateException ex, HttpServletRequest req) {
        return LMSResponse.fail("Duplicate entity: " + ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public LMSResponse handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
        return LMSResponse.fail("Unauthorized access: " + ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public LMSResponse handleInvalidJson(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return LMSResponse.fail("Malformed JSON input.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public LMSResponse handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest req) {
        return LMSResponse.fail("Missing request parameter: " + ex.getParameterName());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public LMSResponse handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return LMSResponse.fail("Validation failed", errors);
    }

    @ExceptionHandler(DataAccessException.class)
    public LMSResponse handleDatabase(DataAccessException ex, HttpServletRequest req) {
        return LMSResponse.fail("Database access error.");
    }

    @ExceptionHandler(TransactionSystemException.class)
    public LMSResponse handleTransaction(TransactionSystemException ex, HttpServletRequest req) {
        return LMSResponse.fail("Transaction failed. Please try again.");
    }

    @ExceptionHandler(EntityDeletionException.class)
    public LMSResponse handleDeletion(EntityDeletionException ex, HttpServletRequest req) {
        return LMSResponse.fail("Failed to delete entiy: " + ex.getMessage());
    }

    @ExceptionHandler(EntityCreationException.class)
    public LMSResponse handleCreation(EntityCreationException ex, HttpServletRequest req) {
        return LMSResponse.fail("Failedd to creation entity: " + ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public LMSResponse handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        return LMSResponse.fail("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public LMSResponse handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return LMSResponse.fail("Access is denied.");
    }

    @ExceptionHandler(TokenExpiredException.class)
    public LMSResponse handleTokenExpired(TokenExpiredException ex, HttpServletRequest req) {
        return LMSResponse.fail("Token expired. Please login again.");
    }

    @ExceptionHandler(JwtException.class)
    public LMSResponse handelJwtError(JwtException ex, HttpServletRequest req) {
        return LMSResponse.fail("Invalid JWT token.");
    }

    @ExceptionHandler(IOException.class)
    public LMSResponse handleIO(IOException ex, HttpServletRequest req) {
        return LMSResponse.fail("File upload failed: " + ex.getMessage());
    }
}
