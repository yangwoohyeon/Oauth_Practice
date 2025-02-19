package hello.Member_Management.User.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 30; // 30분
    private final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7; // 7일

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // ✅ Access Token 생성
    public String createAccessToken(String userPk) {
        return createToken(userPk, ACCESS_TOKEN_VALIDITY);
    }

    // ✅ Refresh Token 생성
    public String createRefreshToken(String userPk) {
        return createToken(userPk, REFRESH_TOKEN_VALIDITY);
    }

    // ✅ JWT 생성 공통 메서드
    public String createToken(String userPk, long validity) {
        Claims claims = Jwts.claims().setSubject(userPk);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validity))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // ✅ 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            System.out.println("❌ 토큰 파싱 실패: " + e.getMessage());
            return null;
        }
    }


    public boolean validateToken(String token) {
        try {
            return !Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println("❌ JWT 유효성 검사 실패: " + e.getMessage());
            return false;
        }
    }

}
