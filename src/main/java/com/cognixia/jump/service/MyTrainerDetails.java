package com.cognixia.jump.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cognixia.jump.model.Trainer;

public class MyTrainerDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private boolean enabled;
	private List<GrantedAuthority> authorities;
	
	public MyTrainerDetails(Trainer trainer) {
		this.username = trainer.getUsername();
		this.password = trainer.getPassword();
		this.enabled = trainer.isEnabled();
		
		// Granted Authority -> permissions/grants of what a user can do based on their role
		this.authorities = Arrays.asList(new SimpleGrantedAuthority(trainer.getRole().name()));
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}
	
	// all these methods below:
	// - DON'T NEED to be stored in a user table
	// - store this info if it's worthwhile for your security
	// - have all these methods return true manually if you are NOT storing this 
	//   info in a user table
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
		return enabled;
	}
}