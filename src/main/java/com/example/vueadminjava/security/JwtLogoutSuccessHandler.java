package com.example.vueadminjava.security;

import cn.hutool.json.JSONUtil;
import com.example.vueadminjava.common.lang.Result;
import com.example.vueadminjava.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	JwtUtils jwtUtils;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}

		response.setContentType("application/json;charset=UTF-8");
		ServletOutputStream outputStream = response.getOutputStream();

		response.setHeader(jwtUtils.getHeader(), "");

		Result<String> result = new Result<>();
		result.succ("退出登录");

		outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

		outputStream.flush();
		outputStream.close();
	}
}
