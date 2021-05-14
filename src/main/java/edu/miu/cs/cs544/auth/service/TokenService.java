package edu.miu.cs.cs544.auth.service;

import edu.miu.cs.cs544.auth.config.JwtUtil;
import edu.miu.cs.cs544.auth.model.AuthenticationRequest;
import edu.miu.cs.cs544.auth.model.DeadToken;
import edu.miu.cs.cs544.auth.model.IDeadTokenRepository;
import edu.miu.cs.cs544.service.PersonDetailsService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class TokenService implements ITokenService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PersonDetailsService personDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IDeadTokenRepository deadTokenRepository;


    @Override
    public String generateToken(AuthenticationRequest authenticationRequest) throws Exception {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            } catch (DisabledException e) {
                throw new Exception("USER_DISABLED", e);
            }
            catch (BadCredentialsException e) {
                throw new Exception("INVALID_CREDENTIALS", e);
            }

            UserDetails userdetails = personDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            String token = jwtUtil.generateToken(userdetails);

            return token;
    }

    @Override
    public boolean invalidateToken(String token) {
        long expirationTime = Jwts.parser().setSigningKey(jwtUtil.getSecret()).parseClaimsJws(token).getBody().getExpiration().getTime();
        DeadToken token1 = deadTokenRepository.save(new DeadToken(token,expirationTime));
        return true;
    }

    @Override
    public boolean tokenIsValid(String token) {
        Optional<DeadToken> token1 = deadTokenRepository.findDeadToken(token);
        return !token1.isPresent();
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        String token = extractJwtFromRequest(request);
        return invalidateToken(token);
    }

    @Override
    @Scheduled(fixedRate = 300000)
    public void cleanDeadTokenBean() {
        long expiration = new Date().getTime();
        deadTokenRepository.cleanDeadTokenBin(expiration);
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
