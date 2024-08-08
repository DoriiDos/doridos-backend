package kr.doridos.dosticket.config;

import kr.doridos.dosticket.domain.auth.oauth.OAuth2SuccessHandler;
import kr.doridos.dosticket.domain.auth.service.CustomOAuth2UserService;
import kr.doridos.dosticket.domain.auth.support.jwt.CustomAccessDeniedHandler;
import kr.doridos.dosticket.domain.auth.support.jwt.CustomAuthenticationEntryPoint;
import kr.doridos.dosticket.domain.auth.support.jwt.JwtFilter;
import kr.doridos.dosticket.domain.auth.support.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .headers().frameOptions().sameOrigin().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                    .antMatchers("/users/signup").permitAll()
                    .antMatchers("/actuator/**").permitAll()
                    .antMatchers("/tickets/{ticketId}/schedules/scheduleId}/**").authenticated()
                    .antMatchers("/tickets/**").permitAll()
                    .antMatchers("/auth/**").permitAll()
                    .antMatchers("/docs/**").permitAll()
                    .antMatchers(HttpMethod.GET,"/categories").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .oauth2Login()
                .successHandler(oAuth2SuccessHandler)
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                .and()

                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())

                .and()
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
