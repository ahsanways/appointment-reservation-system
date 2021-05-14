package edu.miu.cs.cs544.auth.service;

import edu.miu.cs.cs544.auth.model.AuthenticationRequest;

import javax.servlet.http.HttpServletRequest;

public interface ITokenService {
    String generateToken(AuthenticationRequest authenticationRequest) throws Exception;
    String extractJwtFromRequest(HttpServletRequest request);
    boolean invalidateToken(String token);
    boolean tokenIsValid(String token);
    boolean logout(HttpServletRequest request);
    void cleanDeadTokenBean();
}
