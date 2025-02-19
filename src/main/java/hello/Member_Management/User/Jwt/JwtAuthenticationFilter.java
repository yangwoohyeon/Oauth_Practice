package hello.Member_Management.User.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);
        System.out.println("필터 실행됨. 추출된 토큰: " + token);

        // ✅ 이미 인증된 사용자인지 확인
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (existingAuth != null && existingAuth.isAuthenticated()) {
            System.out.println("이미 인증된 사용자입니다: " + existingAuth.getName());
            filterChain.doFilter(request, response);
            return;
        }

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUserPk(token);
            System.out.println("username = " + username);

            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("✅ SecurityContext에 인증 정보 저장 완료");
                } else {
                    System.out.println("❌ 사용자 정보를 찾을 수 없습니다. username: " + username);
                    clearTokenCookie(response); // ✅ 쿠키 초기화
                }
            } catch (UsernameNotFoundException e) {
                System.out.println("❌ 사용자 조회 실패: " + e.getMessage());
                clearTokenCookie(response); // ✅ 쿠키 초기화
            }
        } else {
            clearTokenCookie(response); // ✅ 쿠키 초기화
        }

        filterChain.doFilter(request, response);
    }


    // ✅ 쿠키 삭제 메서드
    private void clearTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);  // 쿠키 삭제
        cookie.setPath("/");
        response.addCookie(cookie);
    }





    private String resolveToken(HttpServletRequest request) {
        // 1️⃣ 헤더에서 먼저 토큰을 찾음
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 2️⃣ 헤더에 없으면 쿠키에서 찾음
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }


}
