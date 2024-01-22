package com.springjwtapplication.services;

import com.springjwtapplication.request.LoginRequest;
import com.springjwtapplication.request.RegisterRequest;
import com.springjwtapplication.response.JwtResponse;

public interface IUserService {
    JwtResponse login(LoginRequest loginRequest);
    void register (RegisterRequest registerRequest);

}
