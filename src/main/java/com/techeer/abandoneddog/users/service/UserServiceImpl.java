package com.techeer.abandoneddog.users.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techeer.abandoneddog.global.exception.user.EmailAlreadyExistsException;
import com.techeer.abandoneddog.global.exception.user.InvalidPasswordException;
import com.techeer.abandoneddog.global.exception.user.UserNotFoundException;
import com.techeer.abandoneddog.users.dto.LoginRequestDto;
import com.techeer.abandoneddog.users.dto.LoginResponseDto;
import com.techeer.abandoneddog.users.dto.RegisterRequestDto;
import com.techeer.abandoneddog.users.dto.UserRequestDto;
import com.techeer.abandoneddog.users.dto.UserResponseDto;
import com.techeer.abandoneddog.users.entity.Users;
import com.techeer.abandoneddog.users.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	@Autowired
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;

	@Override
	@Transactional
	public void signUp(RegisterRequestDto requestDto) {

		Optional<Users> existingUser = userRepository.findByEmail(requestDto.getEmail());
		if (existingUser.isPresent()) {
			Users user = existingUser.get();
			if (!user.getIsDeleted()) {

				throw new EmailAlreadyExistsException();
			}
			userRepository.reactivateUser(user.getId());
		} else {
			Users user = requestDto.toEntity(encoder);
			userRepository.save(user);
		}
	}

	@Override
	@Transactional
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		Users user = loginRequestDto.toEntity();
		Optional<Users> loginuser = userRepository.findByEmail(loginRequestDto.getEmail());

		if (loginuser.isEmpty()) {
			throw new UserNotFoundException();
		}

		if (!encoder.matches(user.getPassword(), loginuser.get().getPassword())) {
			throw new InvalidPasswordException();
		}

		return LoginResponseDto.builder()
			.userId(loginuser.get().getId())
			.email(loginuser.get().getEmail())
			.build();
	}

	@Override
	@Transactional
	public UserResponseDto getUser(Long userId) {
		Users user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(() -> new UserNotFoundException());
		return UserResponseDto.fromEntity(user);
	}

	@Override
	@Transactional
	public Page<UserResponseDto> getUsers(Pageable pageable) {
		Page<Users> usersPage = userRepository.findAllActiveUsers(pageable);
		return usersPage.map(UserResponseDto::fromEntity);
	}

	@Override
	@Transactional
	public UserResponseDto updateUser(Long userId, UserRequestDto dto) {
		Users user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
		user.update(dto, encoder);
		return UserResponseDto.fromEntity(user);
	}

	@Override
	@Transactional
	public void deleteUser(Long userId) {
		Users user = userRepository.findUserById(userId).get();
		if (user.getIsDeleted()) {
			throw new UserNotFoundException();
		}
		userRepository.deleteById(user.getId());
	}
}

