package hous.server.controller.advice;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebDataBinderAdvice {

    /**
     * GET 메소드의 DTO에서도 별도의 setter를 열지 않아도 된다.
     * 참고) https://jojoldu.tistory.com/407
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }
}
