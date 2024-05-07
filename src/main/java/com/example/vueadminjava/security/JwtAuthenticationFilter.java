package com.example.vueadminjava.security;

import cn.hutool.core.util.StrUtil;
import com.example.vueadminjava.entity.SysUser;
import com.example.vueadminjava.service.SysUserService;
import com.example.vueadminjava.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;


/**
 * 在首次登录成功后，LoginSuccessHandler将生成JWT，并返回给前端。在之后的所有请求中（包括再次登录请求），都会携带此JWT信息。
 * 我们需要写一个JWT过滤器JwtAuthenticationFilter，当前端发来的请求有JWT信息时，该过滤器将检验JWT是否正确以及是否过期，
 * 若检验成功，则获取JWT中的用户名信息，检索数据库获得用户实体类，并将用户信息告知Spring Security，
 * 后续我们就能调用security的接口获取到当前登录的用户信息。
 * 若前端发的请求不含JWT，我们也不能拦截该请求，因为一般的项目都是允许匿名访问的，有的接口允许不登录就能访问，
 * 没有JWT也放行是安全的，因为我们可以通过Spring Security进行权限管理，设置一些接口需要权限才能访问，不允许匿名访问
 */

/**
 * JwtAuthenticationFilter继承了BasicAuthenticationFilter，该类用于普通http请求进行身份认证，该类有一个重要属性
 * ：AuthenticationManager，表示认证管理器，它是一个接口，它的默认实现类是ProviderManager
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserDetailServiceImpl userDetailService;

	@Autowired
	SysUserService sysUserService;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

		String jwt = request.getHeader(jwtUtils.getHeader());
		// 这里如果没有jwt，继续往后走，因为后面还有鉴权管理器等去判断是否拥有身份凭证，所以是可以放行的
		// 没有jwt相当于匿名访问，若有一些接口是需要权限的，则不能访问这些接口
		if (StrUtil.isBlankOrUndefined(jwt) || "/captcha".equals(request.getRequestURI())) {
			chain.doFilter(request, response);
			return;
		}

		// token解析失败会返回null，解析失败表示token有问题
		Claims claim = jwtUtils.getClaimByToken(jwt);

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sd = sdf.format(claim.getExpiration());
		System.out.println("claim========="+sd);

		if (claim == null) {
			throw new JwtException("token 异常");
		}

		// token还不能过期
		if (jwtUtils.isTokenExpired(claim)) {
//			throw new JwtException("token已过期");
			String username = claim.getSubject();
			SysUser sysUser = sysUserService.getByUsername(username);
			// 重新生成token
			String newToken = jwtUtils.generateToken(sysUser.getUsername());
			// 将新token设置到响应头中
			response.setHeader(jwtUtils.getHeader(), newToken);
		}


		String username = claim.getSubject();

		// 获取用户信息
		SysUser sysUser = sysUserService.getByUsername(username);

		// 构建UsernamePasswordAuthenticationToken,这里密码为null，是因为提供了正确的JWT,实现自动登录
		UsernamePasswordAuthenticationToken token
				= new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(sysUser.getId()));

		// 要将user相关的信息存入SecurityContextHolder，因为后面的filter需要通过holder来读取这些信息
		SecurityContextHolder.getContext().setAuthentication(token);

		chain.doFilter(request, response);
	}
}
