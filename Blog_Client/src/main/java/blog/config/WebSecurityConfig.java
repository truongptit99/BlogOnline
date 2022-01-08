package blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import blog.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService); //.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		http.authorizeRequests()
			.antMatchers("/", "/register", "/processRegister", "/logout",
					"/css/**", "/fonts/**", "/images/**", "/js/**", "/uploads/**", "/style.css")
			.permitAll();
		
		http.authorizeRequests()
			.antMatchers("/admin/**")
			.access("hasRole('ROLE_ADMIN')");
		
		http.authorizeRequests()
			.antMatchers("/user/**")
			.access("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')");
		
		http.authorizeRequests()
			.and()
			.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/checkLogin")
			.defaultSuccessUrl("/")
			.failureUrl("/login?error=true")
			.usernameParameter("email")
			.passwordParameter("password")
			.and()
			.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/");
		
		http.authorizeRequests()
			.and()
			.exceptionHandling()
			.accessDeniedPage("/403");
	}
}
