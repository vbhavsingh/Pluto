package com.security.common;


import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Resource
	private DbAuthenticationService userDetailsService;

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/secure/**", 
						"/page.htm",
						"/getlinesfromfile.htm",
						"/labels.htm",
						"/next.htm",
						"/search.htm",
						"/res/htm/**",
						"/admin/*.jsp",
						"/admin/views/*.jsp",
						"/version.htm")
				.permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login.htm")
				.failureForwardUrl("/login.htm?login_error=1")
				.successForwardUrl("/application.htm")
				.permitAll()
				.and()
			.logout()
				.permitAll();
	}

	@Override
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())  
        .withUser("admin")
        .password("admin")
        .roles("ADMIN")
        .and()
        .withUser("sysuser")
        .password("password")
        .roles("SYSTEM");
    }
	 
	 
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }
}