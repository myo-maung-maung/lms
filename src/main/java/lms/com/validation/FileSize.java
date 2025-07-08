package lms.com.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileSizeValidator.class)
@Documented
public @interface FileSize {
    String message() default "File size is too large";
    long max();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
