package com.app.demo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.AuthDTO;
import com.app.demo.dtos.JwtResponse;
import com.app.demo.securities.JwtTokenProvider;
import com.app.demo.securities.MyUserDetails;

@CrossOrigin 	
@RestController
@RequestMapping(path="/access-tokens")
public class AuthController {
		@Autowired
		private AuthenticationManager authenticationManager;
		@Autowired
		private JwtTokenProvider tokenProvider;
		
		@PostMapping
		public JwtResponse authenticateUser(@RequestBody AuthDTO loginRequest, HttpServletResponse response) {
			// Xác thực từ username và password
			Authentication authentication = null;
			try {
			 authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
			);}catch(Exception e) {
				throw new RuntimeException();
			}
					;		
			// Nếu không xảy ra exception tức là thông tin hợp lệ
	        // Set thông tin authentication vào Security Context			
			SecurityContextHolder.getContext().setAuthentication(authentication);			
			//trả  jwt về cho người dùng
			MyUserDetails userDetails =  (MyUserDetails) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority()).collect(Collectors.toList());
			
			String jwt = tokenProvider.generateToken((userDetails));
			Cookie cookie = new Cookie("access_token", jwt);
			cookie.setSecure(false);
			cookie.setHttpOnly(true);
			cookie.setMaxAge(604800); //7 days
			response.addCookie(cookie);
			
			
			
			return new JwtResponse(jwt, userDetails.getUsername(), roles);		
		}
}
