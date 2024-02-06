package hypevoice.hypevoiceback.global.config;

import hypevoice.hypevoiceback.auth.security.CookieAuthorizationRequestRepository;
import hypevoice.hypevoiceback.auth.security.CustomOAuth2UserService;
import hypevoice.hypevoiceback.auth.security.OAuth2LoginSuccessHandler;
import hypevoice.hypevoiceback.auth.security.OAuthLoginFailureHandler;
import hypevoice.hypevoiceback.auth.security.jwt.JwtAccessDeniedHandler;
import hypevoice.hypevoiceback.auth.security.jwt.JwtAuthenticationEntryPoint;
import hypevoice.hypevoiceback.auth.security.jwt.JwtAuthenticationFilter;
import hypevoice.hypevoiceback.auth.security.jwt.JwtProvider;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuthLoginFailureHandler oAuthLoginFailureHandler;

    private final JwtProvider jwtProvider;
    private final MemberFindService memberFindService;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**", "/error", "/favicon.ico", "/api/auth/logout/**", "/");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("http//localhost:5173")); // 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(authenticationManager -> authenticationManager
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher( "/api/token/reissue"),
                                        AntPathRequestMatcher.antMatcher( "/api/boards"),
                                        AntPathRequestMatcher.antMatcher( "/api/boards/{boardId}"),
                                        AntPathRequestMatcher.antMatcher( "/api/works"),
                                        AntPathRequestMatcher.antMatcher( "/api/works/{workId}"),
                                        AntPathRequestMatcher.antMatcher("/api/works/{workId}/categoryInfos"),
                                        AntPathRequestMatcher.antMatcher( "/api/voices"),
                                        AntPathRequestMatcher.antMatcher( "/api/voices/{voiceId}")

                                ).permitAll()
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/api/**")
                                ).hasRole("USER")
                                .anyRequest().authenticated()
                )
                .oauth2Login(configure  ->
                        configure
                                .authorizationEndpoint(authorizationEndpointConfig ->
                                        authorizationEndpointConfig.authorizationRequestRepository(cookieAuthorizationRequestRepository))
                                .userInfoEndpoint(config  ->
                                        config.userService(customOAuth2UserService)
                                )
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureHandler(oAuthLoginFailureHandler)
                )
                //Authorization request와 관련된 state가 저장됨

                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, memberFindService), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
