package ru.tinkoff.fintech.parking.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${auth.login_user}")
    private String login_user;
    @Value("${auth.login_admin}")
    private String login_admin;
    @Value("${auth.password_user}")
    private String password_user;
    @Value("${auth.password_admin}")
    private String password_admin;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(login_user).password("{noop}"+password_user).roles("USER").and()
                .withUser(login_admin).password("{noop}"+password_admin).roles("ADMIN");
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests()
                .antMatchers("/cars/**").hasRole("ADMIN")
                .antMatchers("/parking-spaces/**").hasRole("ADMIN")
                .antMatchers("/bookings/**").hasRole("USER")
                .antMatchers("/parking-spaces/get").hasRole("USER")
                .and().csrf().disable().headers().frameOptions().disable();
    }
}
