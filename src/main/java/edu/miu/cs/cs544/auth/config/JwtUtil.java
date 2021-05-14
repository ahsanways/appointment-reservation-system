package edu.miu.cs.cs544.auth.config;

import edu.miu.cs.cs544.auth.service.ITokenService;
import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service @Getter
public class JwtUtil {

	private String secret;
	private int jwtExpirationInMs;

	@Autowired
	private ITokenService tokenService;

	@Value("${jwt.secret}")
	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Value("${jwt.expirationDateInMs}")
	public void setJwtExpirationInMs(int jwtExpirationInMs) {
		this.jwtExpirationInMs = jwtExpirationInMs;
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();

		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

		if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			claims.put("isAdmin", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
			claims.put("isCustomer", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_COUNSELOR"))) {
			claims.put("isCounselor", true);
		}
		
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
				.signWith(SignatureAlgorithm.HS512, secret).compact();

	}

	public boolean validateToken(String authToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
			return tokenService.tokenIsValid(authToken);
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		} catch (ExpiredJwtException ex) {
			throw ex;
		}
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claims.getSubject();

	}

	public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

		List<SimpleGrantedAuthority> roles = null;

		Boolean isAdmin = claims.get("isAdmin", Boolean.class);
		Boolean isCustomer = claims.get("isCustomer", Boolean.class);
		Boolean isCounselor = claims.get("isCounselor", Boolean.class);

		if (isAdmin != null && isAdmin) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		if (isCustomer != null && isCustomer) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
		}
		if (isCounselor != null && isCounselor) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_COUNSELOR"));
		}
		return roles;
	}

}
