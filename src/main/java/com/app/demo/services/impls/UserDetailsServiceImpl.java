package com.app.demo.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.demo.models.User;
import com.app.demo.repositories.UserRepository;
import com.app.demo.securities.MyUserDetails;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository repo;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public UserDetails loadUserByUsername(String username){
		User user = null;
		try {
		 user = repo.findByUsername(username);
		}catch(Exception e) {
			throw new RuntimeException();
		}
		return new MyUserDetails(user);
	}

}
