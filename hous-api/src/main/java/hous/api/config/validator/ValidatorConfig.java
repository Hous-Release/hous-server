package hous.api.config.validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class ValidatorConfig {

    @Bean
    static LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    static MethodValidationPostProcessor methodValidationPostProcessor(LocalValidatorFactoryBean localValidatorFactoryBean) {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(localValidatorFactoryBean);
        return processor;
    }
}
