package hous.api.config.aop.logging;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

	/**
	 * execution([수식어] 리턴타입 [클래스이름].이름(파라미터)
	 * * : 모든 값 포함
	 * .. : 0개 이상 의미
	 * <p>
	 * hous.api.controller 패키지 내부의 모든 클래스에서 파라미터가 0개 이상인 모든 메서드
	 */
	@Pointcut("execution(* hous.api.controller..*(..))")
	public void controllerExecute() {
	}

	@Around("hous.api.config.aop.logging.LoggingAspect.controllerExecute()")
	public Object requestLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		long startAt = System.currentTimeMillis();
		Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
		long endAt = System.currentTimeMillis();
		log.info("====> Request: {} {} ({}ms)\n *Header = {}\n", request.getMethod(), request.getRequestURL(),
			endAt - startAt, getHeaders(request));
		if (returnValue != null) {
			log.info("====> Response: {}", returnValue);
		}
		return returnValue;
	}

	private Map<String, Object> getHeaders(HttpServletRequest request) {
		Map<String, Object> headerMap = new HashMap<>();

		Enumeration<String> headerArray = request.getHeaderNames();
		while (headerArray.hasMoreElements()) {
			String headerName = headerArray.nextElement();
			headerMap.put(headerName, request.getHeader(headerName));
		}
		return headerMap;
	}
}
