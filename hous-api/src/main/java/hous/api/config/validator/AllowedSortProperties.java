package hous.api.config.validator;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {AllowedSortPropertiesValidator.class})
@Target({ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedSortProperties {

	String message() default "허용하지 않는 정렬기준을 입력했습니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String[] value();

	@Target({ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		AllowedSortProperties[] value();
	}
}
