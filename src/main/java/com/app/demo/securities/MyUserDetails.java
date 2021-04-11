package com.app.demo.securities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.demo.models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyUserDetails implements UserDetails {
	/**
	 * serialVersionUID dùng để xác minh một object
	 * được tuần tự hoá trước đó có tương thích với Serializable class ở phiên bản hiện tại hay không.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * lombok auto generate constructor for User object. 
	 */
	private User user;
	
	/**
	 * This method is used to get Authorities - (admin/salesman/manager)
	 * @implSpec user -> <List> getRoles() -> forEach role -> getName() 
	 * -> authorities.add(SimpleGrantedAuthorities(role name))
	 * @return Collection of authorities.
	 */
	@Override
	public  Collection<? extends SimpleGrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(authority);
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPasswordHash();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
