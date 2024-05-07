package com.example.vueadminjava.security;

import cn.hutool.json.JSONUtil;
import com.example.vueadminjava.common.lang.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JWT认证失败处理器:BasicAuthenticationFilter认证失败的时候会进入AuthenticationEntryPoint
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
		ServletOutputStream outputStream = response.getOutputStream();

		Result result = Result.fail("请先登录");

		outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

		outputStream.flush();
		outputStream.close();
	}
}
