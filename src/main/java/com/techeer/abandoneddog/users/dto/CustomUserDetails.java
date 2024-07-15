package com.techeer.abandoneddog.users.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.techeer.abandoneddog.users.entity.Users;

public class CustomUserDetails implements UserDetails {

	private final Users userEntity;

	public CustomUserDetails(Users userEntity) {
		this.userEntity = userEntity;
	}

	public Long getId() {
		return userEntity.getId();
	}

	public String getPhoneNum() {
		return userEntity.getPhoneNum();
	}

	public boolean isDeleted() {
		return userEntity.getIsDeleted(); // Users 엔티티의 isDeleted 필드와 동기화
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> collection = new ArrayList<>();

		collection.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {

				return userEntity.getUsername();
			}
		});

		return collection;
	}

	@Override
	public String getPassword() {

		return userEntity.getPassword();
	}

	@Override
	public String getUsername() {

		return userEntity.getUsername();
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
		return !userEntity.getIsDeleted(); // isDeleted 필드에 따라 계정 활성화 상태 설정
	}
}