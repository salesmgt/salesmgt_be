package com.app.demo.securities;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	//Đoạn JWT này là bí mật, chỉ có phía server biết nha.
	private final String JWT_SECRET = "Hasan";
	
	//Thời gian có hiệu lực của chuỗi JWT
	private final long JWT_EXPIRATION = 604800000L; // = 7 ngày
	
	/**
	 * Tạo ra Jwt từ thông tin người dùng
	 * @param userDetails
	 * @return mã jwt dạng String
	 */
	public String generateToken(MyUserDetails userDetails) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
		//Tạo chuỗi json web token từ id của user.
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
				.compact();		
	}
	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser()
						.setSigningKey(JWT_SECRET)
						.parseClaimsJws(token)
						.getBody();
		return claims.getSubject();
	}
	public boolean validateToken(String token){
	    try {
	        Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
	        return true;
	    }catch (SignatureException ex){
	        System.out.println("Invalid JWT Signature");
	    /*}catch (MalformedJwtException ex){
	        System.out.println("Invalid JWT token");*/
	    }catch (ExpiredJwtException ex){
	        System.out.println("Expired JWT token");
	       
	    }catch (UnsupportedJwtException ex){
	        System.out.println("Unsupported JWT exception");
	    }catch (IllegalArgumentException ex){
	        System.out.println("Jwt claims string is empty");
	    }
	    return false;
	}
}
