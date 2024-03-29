package com.mindmeets.todomanagement.jwt;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class JwtAuthenticationResource {

	private JwtEncoder jwtEncoder;

	private UserDetailsService userDetailsService;

	private AuthenticationManager authenticationManager;

	public JwtAuthenticationResource(JwtEncoder jwtEncoder, UserDetailsService userDetailsService,
			AuthenticationManager authenticationManager) {
		this.jwtEncoder = jwtEncoder;
		this.userDetailsService = userDetailsService;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("login")
	public JwtResponse login(@RequestBody JwtRequest authRequest, Authentication authentication)
			throws AuthenticationException {
		validateLogin(authRequest.username(), authRequest.password());
		return createToken(authRequest.username());
	}

	private void validateLogin(String username, String password) throws AuthenticationException {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("USER_DISABLED");
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("INVALID_CREDENTIALS");
		}
	}

	@PostMapping("authenticate")
	public JwtResponse authenticate(Authentication authentication) {
		return createToken(authentication.getName());
	}

	private JwtResponse createToken(String username) {
		var authUser = userDetailsService.loadUserByUsername(username);
		var claims = JwtClaimsSet.builder().issuer("self").issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(60 * 30)).subject(authUser.getUsername())
				.claim("roles", createScope(authUser.getAuthorities())).build();
		var jwtToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		
		return new JwtResponse(username, jwtToken, authUser.getAuthorities()
					.stream().map(authority -> authority.getAuthority().substring(5))
					.collect(Collectors.toList()));
	}

	private String createScope(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream().map(a -> a.getAuthority()).collect(Collectors.joining(" "));
	}

	record JwtResponse(String username, String token, List<String> roles) {
	}

	record JwtRequest(String username, String password) {
	}
}
