package com.app.demo.securities;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.demo.services.impls.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	UserDetailsServiceImpl customUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	try {
		//Lấy jwt từ request ra nè
		String jwt = getJwtFromRequest(request);
		if(StringUtils.hasText(jwt)&& tokenProvider.validateToken(jwt)) {
			//Lấy user id từ chuỗi jwt
			String username = tokenProvider.getUsernameFromJWT(jwt);
			
			//Lấy thông tin người dùng từ id
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
			if(!ObjectUtils.isEmpty(userDetails)){
				
				//Nếu người dùng hợp lệ, set thông tin cho Security Context
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
	} catch (Exception ex) {
		log.error("failed on set user authentication", ex);
	}
	 filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		 String bearerToken = request.getHeader("Authorization");
	        // Kiểm tra xem header Authorization có chứa thông tin jwt không
	        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
	            return bearerToken.substring(7);
	        }
		return null;
	}
}
