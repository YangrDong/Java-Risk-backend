package com.example.vueadminjava.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.vueadminjava.common.exception.CaptchaException;
import com.example.vueadminjava.common.lang.Const;
import com.example.vueadminjava.util.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CaptchaFilter继承了OncePerRequestFilter抽象类，
 * 该抽象类在每次请求时只执行一次过滤，即它的作用就是保证一次请求只通过一次filter,
 * 而不需要重复执行。CaptchaFilter需要重写其doFilterInternal方法来自定义处理逻辑
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

	@Autowired
	RedisUtil redisUtil;

	@Autowired
	LoginFailureHandler loginFailureHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

		String url = httpServletRequest.getRequestURI();

		// 先判断是不是登录的请求
		if ("/login".equals(url) && httpServletRequest.getMethod().equals("POST")) {
			try{
				// 校验验证码
				validate(httpServletRequest);
			} catch (CaptchaException e) {
				// 交给登录认证失败处理器
				loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
			}
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);//进入下一个过滤器
	}

	// 校验验证码逻辑
	private void validate(HttpServletRequest httpServletRequest) {
		String code = httpServletRequest.getParameter("code");
		String key = httpServletRequest.getParameter("token");

		if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
			throw new CaptchaException("验证码错误");
		}

		if (!code.equals(redisUtil.hget(Const.CAPTCHA_KEY, key))) {
			throw new CaptchaException("验证码错误");
		}

		// 一次性使用
		redisUtil.hdel(Const.CAPTCHA_KEY, key);
	}
}
