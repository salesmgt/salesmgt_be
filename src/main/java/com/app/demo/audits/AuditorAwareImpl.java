package com.app.demo.audits;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.app.demo.securities.MyUserDetails;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 if (authentication == null || !authentication.isAuthenticated()) {
			   return null;
			  }
		 MyUserDetails userDetails = null;
		String username = null;
		 if(!authentication.getPrincipal().equals("anonymousUser")){
			userDetails =  (MyUserDetails) authentication.getPrincipal();
			username = userDetails.getUsername();
		 }else {
			 username ="anonymousUser";
		 }
		
			  return Optional.ofNullable(username);
	}

	
}
	