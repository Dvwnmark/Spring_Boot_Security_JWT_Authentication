package com.springjwtapplication.services;

import com.springjwtapplication.models.Role;
import com.springjwtapplication.models.User;
import com.springjwtapplication.repositories.UserRepository;
import com.springjwtapplication.request.LoginRequest;
import com.springjwtapplication.request.RegisterRequest;
import com.springjwtapplication.response.JwtResponse;
import com.springjwtapplication.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("Invalid email or password");
        }
        User existingUser = user.get();
        if (!passwordEncoder.matches(password, existingUser.getPassword())){
            throw  new UsernameNotFoundException("Invalid email or password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password
        );
        authenticationManager.authenticate(authenticationToken);
        String token = jwtService.generateToken(existingUser);
        return JwtResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        User user = User.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        Role role = Role.builder()
                .name("user")
                .user(user)
                .build();
        user.setRoleList(List.of(role));
        userRepository.save(user);
    }
}
