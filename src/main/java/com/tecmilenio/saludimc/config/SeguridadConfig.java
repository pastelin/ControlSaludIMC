package com.tecmilenio.saludimc.config;

import com.tecmilenio.saludimc.repo.UsuarioRepository;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SeguridadConfig {
    @Bean public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean public UserDetailsService userDetailsService(UsuarioRepository usuarios){
        return username->usuarios.findByUsuario(username)
          .map(u->User.withUsername(u.usuario).password(u.contrasenaHash).roles("USER").build())
          .orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado"));
    }
    @Bean public SecurityFilterChain seguridad(HttpSecurity http,ApiSecurityErrorHandler apiErrors) throws Exception {
        http.authorizeHttpRequests(a->a
            .requestMatchers("/","/login","/registro","/css/**","/js/**","/favicon.ico").permitAll()
            .requestMatchers(org.springframework.http.HttpMethod.POST,"/api/user").permitAll()
            .anyRequest().authenticated())
          .csrf(c->c.ignoringRequestMatchers(new AntPathRequestMatcher("/api/user","POST")))
          .exceptionHandling(e->e.defaultAuthenticationEntryPointFor(apiErrors,
                  new AntPathRequestMatcher("/api/**")).accessDeniedHandler(apiErrors))
          .formLogin(f->f.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/imc",true)
                .failureUrl("/login?error").permitAll())
          .logout(l->l.logoutUrl("/logout").logoutSuccessUrl("/login?logout"));
        return http.build(); // CSRF activado. JS manda X-CSRF-TOKEN.
    }
}
