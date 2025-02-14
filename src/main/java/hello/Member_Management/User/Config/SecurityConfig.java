package hello.Member_Management.User.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록됨
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/user/**").authenticated() // 로그인 필요
                .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN") // MANAGER와 ADMIN만 허용
                .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN만 허용
                .requestMatchers("joinForm").permitAll()
                .anyRequest().permitAll() // 그 외 요청은 허용
        );

        // 폼 로그인 설정
        http.formLogin(form -> form
                .loginPage("/loginForm") // 커스텀 로그인 페이지
                .loginProcessingUrl("/login") // login URL 처리
                .defaultSuccessUrl("/") // 로그인 성공 후 이동할 페이지
                .permitAll()
        );

        // OAuth2 로그인 설정 추가
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/loginForm") // OAuth2 로그인 페이지
                .defaultSuccessUrl("/") // 로그인 성공 후 이동할 URL
        );

        return http.build();
    }
}
