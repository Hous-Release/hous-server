package hous.api.config.interceptor.version;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class VersionInterceptor implements HandlerInterceptor {

	private final VersionCheckHandler versionCheckHandler;

	@Override
	public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
		@NotNull Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		Version version = handlerMethod.getMethodAnnotation(Version.class);
		if (version == null) {
			return true;
		}
		versionCheckHandler.checkVersion(request);
		return true;
	}
}
