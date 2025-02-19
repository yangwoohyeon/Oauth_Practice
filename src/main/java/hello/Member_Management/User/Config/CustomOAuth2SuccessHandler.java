package hello.Member_Management.User.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.Member_Management.User.Jwt.JwtTokenProvider;
import hello.Member_Management.User.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();

        // ✅ JWT 생성
        String accessToken = jwtTokenProvider.createAccessToken(username);
        String refreshToken = jwtTokenProvider.createRefreshToken(username);

        // ✅ SecurityContext에 인증 정보 수동 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ✅ JWT를 쿠키에 저장 (선택)
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        System.out.println("accessToken = " + accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60*30); //30분동안 유효
        response.addCookie(accessTokenCookie);

        // ✅ 홈으로 리다이렉트
        response.sendRedirect("/");
    }

}
