package com.example.vueadminjava.security;

import cn.hutool.json.JSONUtil;
import com.example.vueadminjava.common.lang.Result;
import com.example.vueadminjava.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;



@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	JwtUtils jwtUtils;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		response.setContentType("application/json;charset=UTF-8");
//		通过调用 response.getOutputStream() 方法获取的 ServletOutputStream 对象可以用来向客户端发送响应数据。
		ServletOutputStream outputStream = response.getOutputStream();
		// 登录成功生成jwt，并放置到请求头中，返回给前端
		String jwt = jwtUtils.generateToken(authentication.getName());
		response.setHeader(jwtUtils.getHeader(), jwt);
		Result<String> result = new Result<>();
		result.succ("SuccessLogin");

//		outputStream.write将 JSON 字符串的字节数组写入输出流中，并通过 outputStream.flush() 将数据发送到客户端。
		outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

		outputStream.flush();
		outputStream.close();
	}

}
