package lms.com.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileTypeValidator.class)
@Documented
public @interface FileType {
    String message() default "Invalid file type";
    String[] allowed(); // MIME types
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
