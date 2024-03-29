package com.lab.software.engineering.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lab.software.engineering.services.EmployeeDetailService;

@Configuration
@Profile("!non-secure")
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	PasswordEncoder passEncoder;

	@Autowired
	EmployeeDetailService detailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors()
		.and().authorizeRequests().anyRequest().authenticated()
		.and().httpBasic()
		.and().formLogin().permitAll()
		.and().logout().logoutUrl("/logout").invalidateHttpSession(true)
		.deleteCookies("JSESSIONID").clearAuthentication(true).logoutSuccessUrl("/login");

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(detailsService);
		authProvider.setPasswordEncoder(passEncoder);
		return authProvider;
	}

}
