package br.com.optimus.web.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;

import static br.com.optimus.web.config.JwtTokenProvider.SECRET;

public class SecurityUtil {

	private SecurityUtil() {
	};

	public static String cryptPassword(String password) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode(password);
	}

	public static String getJwtUser(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}

		var key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}

}