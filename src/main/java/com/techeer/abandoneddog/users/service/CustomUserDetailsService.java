package com.techeer.abandoneddog.users.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.techeer.abandoneddog.users.dto.CustomUserDetails;
import com.techeer.abandoneddog.users.entity.Users;
import com.techeer.abandoneddog.users.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {

		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		//DB에서 조회
		Optional<Users> userData = userRepository.findByEmail(email);

		if (userData.isPresent()) {

			//UserDetails에 담아서 return하면 AutneticationManager가 검증 함
			return new CustomUserDetails(userData.orElse(null));
		}

		return null;
	}
}