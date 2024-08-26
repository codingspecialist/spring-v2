package shop.mtcoding.blog.core.error;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import shop.mtcoding.blog.core.error.ex.Exception400;

@Component
@Aspect // AOP 등록
public class GlobalValidationHandler {

    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void validCheck(JoinPoint jp) {
        Object[] args = jp.getArgs();
        for (Object arg : args) {
            if (arg instanceof Errors) {
                Errors errors = (Errors) arg;

                if (errors.hasErrors()) {
                    for (FieldError error : errors.getFieldErrors()) {
                        throw new Exception400(error.getDefaultMessage() + " : " + error.getField());
                    }
                }
            }
        }
    }

    @Around("@annotation(shop.mtcoding.blog.core.Hello)")
    //@Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object hello1(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("aop hello1 before 호출됨");
        Object proceed = jp.proceed(); // @Hello 어노테이션이 붙은 함수 호출 "user/login-form";
        System.out.println("aop hello1 after 호출됨");
        System.out.println(proceed);
        return proceed;
    }
}
