package lk.travel.authservice.security;

import jakarta.servlet.http.HttpServletRequest;
import lk.travel.authservice.filter.CsrfCookieFilter;
import lk.travel.authservice.filter.JwtGenerateFilter;
import lk.travel.authservice.filter.JwtValidateFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors(httpCors -> {
                    httpCors.configurationSource(new CorsConfigurationSource() {
                        @Override
                        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                            CorsConfiguration corsConfiguration = new CorsConfiguration();
                            corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
                            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                            corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                            corsConfiguration.setAllowCredentials(true);
                            corsConfiguration.setExposedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION));
                            corsConfiguration.setMaxAge(3600L);
                            return corsConfiguration;

                        }
                    });
                }).sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).csrf(csrf -> {
                    csrf
                            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                            //  .ignoringRequestMatchers("/api/v1/user/register")
                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                })
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JwtGenerateFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtValidateFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers("/api/v1/user/**").hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers("/api/v1/guide/**").authenticated();
                    //   .requestMatchers("/api/v1/user/register").permitAll();

                });
        httpSecurity.httpBasic(Customizer.withDefaults()).formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}