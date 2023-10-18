package com.kaua.ecommerce.users.infrastructure.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Profile("!development")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/roles").hasAnyAuthority("manage-roles")
                                .requestMatchers(HttpMethod.GET, "/accounts").hasAnyAuthority("list-accounts")
                                .requestMatchers("/permissions").hasAnyAuthority("manage-permissions")
                                .anyRequest().authenticated())
                .oauth2ResourceServer(resource -> resource
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtConverter())))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Permite acesso apenas a este domínio
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    static class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        private final AuthoritiesConverter authoritiesConverter;

        public JwtConverter() {
            this.authoritiesConverter = new AuthoritiesConverter();
        }

        @Override
        public AbstractAuthenticationToken convert(final Jwt jwt) {
            return new JwtAuthenticationToken(jwt, extractAuthorities(jwt), extractPrincipal(jwt));
        }

        private String extractPrincipal(final Jwt jwt) {
            return jwt.getClaimAsString(JwtClaimNames.SUB);
        }

        private Collection<? extends GrantedAuthority> extractAuthorities(final Jwt jwt) {
            return this.authoritiesConverter.convert(jwt);
        }
    }

    static class AuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        private static final String ROLE = "role";
        private static final String AUTHORITIES = "authorities";

        @Override
        public Collection<GrantedAuthority> convert(final Jwt jwt) {
            final var accountRole = extractRole(jwt);
            final var resourcePermissions = extractPermissions(jwt);

            final var authorities = resourcePermissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            final var role = Collections.singleton(new SimpleGrantedAuthority("ROLE_"+accountRole));

            final Collection<GrantedAuthority> allAuthorities = new ArrayList<>(authorities);
            allAuthorities.addAll(role);

            return allAuthorities;
        }

        private List<String> extractPermissions(final Jwt jwt) {
            return Optional.ofNullable(jwt.getClaimAsStringList(AUTHORITIES))
                    .orElse(Collections.emptyList());
        }

        private String extractRole(final Jwt jwt) {
            return jwt.getClaimAsString(ROLE);
        }
    }
}
