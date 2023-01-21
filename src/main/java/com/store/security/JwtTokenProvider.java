package com.store.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.store.exceptions.FoodStoreAppException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	// Value se utiliza para obtener un valor de application.properties
	@Value("${app.jwt-secret}")
	private String jwtSecret;

	@Value("${app.jwt-expiration-milliseconds}")
	private int jwtExpirationInMs;

	public String buildToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expirationDate = new Date(currentDate.getTime() + jwtExpirationInMs);
		Key key = getKey(jwtSecret);
		String token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(expirationDate)
				.signWith(key, SignatureAlgorithm.HS512).compact(); // or signWith(Key,
		return token;
	}

	public String getUserNameOfJWT(String token) {
		Key key = getKey(jwtSecret);
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public Boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getKey(jwtSecret)).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException ex) {
			throw new FoodStoreAppException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			throw new FoodStoreAppException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			throw new FoodStoreAppException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
		} catch (UnsupportedJwtException ex) {
			throw new FoodStoreAppException(HttpStatus.BAD_REQUEST, "JWT token not supported");
		} catch (IllegalArgumentException ex) {
			throw new FoodStoreAppException(HttpStatus.BAD_REQUEST, "The claims JWT string is empty");
		}
	}

	private Key getKey(String stringKey) {
		byte[] keyBytes = Decoders.BASE64.decode(stringKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
