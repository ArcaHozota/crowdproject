package jp.co.sony.ppog.listener;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import jp.co.sony.ppog.utils.CrowdPlusUtils;

/**
 * ログイン認証ハンドラ
 *
 * @author ArkamaHozota
 * @since 1.50
 */
@Component
public final class CrowdPlusAuthenticationEntryPointImpl implements AuthenticationEntryPoint {

	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException authException) throws IOException, ServletException {
		final ResponseResult result = new ResponseResult(401, authException.getMessage());
		final Gson gson = new Gson();
		final String json = gson.toJson(result);
		CrowdPlusUtils.renderString(response, json);
	}

}
