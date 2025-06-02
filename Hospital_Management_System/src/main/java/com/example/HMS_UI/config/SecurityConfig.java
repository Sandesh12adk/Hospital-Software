package com.example.HMS_UI.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private Filter filter;

    @Autowired
    private CustomeSuccessHandler successHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        // 👩‍⚕️ Public pages (open to all users)
                        .requestMatchers(
                                "/login",
                                "/home",
                                "/docs",
                                "/user/hello",
                                "/regi",
                                "/save",
                                "/register_patient",
                                "/user/patient_save",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/images/**",
                                "/css/**",
                                "/js/**",
                                "/webjars/**",
                                "/favicon.ico",
                                "/template/**"
                        ).permitAll()

                        // 🔐 Admin-only endpoints
                        .requestMatchers(
                                "/admin/register",
                                "/appointment/create",
                                "/appointment/findall",
                                "/department/save",
                                "/user/patient/findall",
                                "/admin/dashboard"
                        ).hasRole("ADMIN")

                        // 👨‍⚕️ Doctor-only endpoints
                        .requestMatchers(
                                "/user/doctor/dashboard",
                                "/user/doctor/make_as_schelduded/{appointmentId}",
                                "/user/doctor/make_as_canceled/{appointmentId}",
                                "/user/doctor/dashboard"
                        ).hasRole("DOCTOR")
                        .requestMatchers("/user/patient/dashboard").hasRole("PATIENT")
                        // 👨‍⚕️👩‍⚕️ Shared access for Doctor & Admin
                        .requestMatchers(
                                "/appointment/find-by-doctorid/{docId}",
                                "/appointment/find-by-docid-and-status/**"
                        ).hasAnyRole("DOCTOR", "ADMIN")

                        // 👨‍⚕️👩‍⚕️ Patient-specific data access
                        .requestMatchers(
                                "/appointment/findbypatientid/{patientId}",
                                "/user/patient/{patiendId}"
                        ).hasAnyRole("PATIENT", "ADMIN")

                        // 🛑 Any other endpoint must be authenticated
                        .anyRequest().authenticated()
                )

                // 🔐 Login and logout configuration
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true") // Redirect to login page with logout message
                        .invalidateHttpSession(true)           // Invalidate the session
                        .clearAuthentication(true)             // Clear the authentication
                        .deleteCookies("JSESSIONID")           // Optional: clear session cookie
                        .permitAll()
                )


                // ⚙️ Session handling
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        // 🔄 Custom Filter before default auth filter
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔐 Authentication Provider Bean
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(5));
        return provider;
    }

    // 🔐 Authentication Manager Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
