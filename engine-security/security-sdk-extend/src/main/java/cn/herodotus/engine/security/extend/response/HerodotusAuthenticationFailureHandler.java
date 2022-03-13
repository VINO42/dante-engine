package cn.herodotus.engine.security.extend.response;

import cn.herodotus.engine.assistant.core.domain.Result;
import cn.herodotus.engine.security.core.exception.SecurityGlobalExceptionHandler;
import cn.herodotus.engine.web.core.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Description: 认证失败处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/19 20:48
 */
public class HerodotusAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Result<String> result = SecurityGlobalExceptionHandler.resolveSecurityException(exception, request.getRequestURI());
        response.setStatus(result.getStatus());
        WebUtils.renderJson(response, result);
    }
}
