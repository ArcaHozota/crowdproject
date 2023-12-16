package jp.co.sony.ppog.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jp.co.sony.ppog.commons.CrowdPlusConstants;
import jp.co.sony.ppog.exception.CrowdPlusException;
import lombok.extern.log4j.Log4j2;

/**
 * SpringSecurity配置クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Log4j2
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	/**
	 * ログインサービス
	 */
	@Resource
	private CrowdPlusUserDetailsService pgCrowdUserDetailsService;

	@Bean
	protected AuthenticationManager authenticationManager(final AuthenticationManagerBuilder auth) {
		return auth.authenticationProvider(this.daoAuthenticationProvider()).getObject();
	}

	@Bean
	protected DaoAuthenticationProvider daoAuthenticationProvider() {
		final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.pgCrowdUserDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder(BCryptVersion.$2A, 7));
		return provider;
	}

	@Bean
	protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		final AntPathRequestMatcher[] pathMatchers = { new AntPathRequestMatcher("/static/**", "GET") };
		http.authorizeHttpRequests(authorize -> {
			authorize.requestMatchers(pathMatchers).permitAll().anyRequest().authenticated();
			try {
				authorize.and().csrf(CsrfConfigurer::disable);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}).formLogin(formLogin -> {
			formLogin.loginPage("/pgcrowd/employee/login").loginProcessingUrl("/pgcrowd/employee/do/login")
					.defaultSuccessUrl("/pgcrowd/to/mainmenu").permitAll().usernameParameter("loginAcct")
					.passwordParameter("userPswd");
			try {
				formLogin.and().logout(logout -> logout.logoutUrl("/pgcrowd/employee/logout")
						.logoutSuccessUrl("/pgcrowd/employee/login"));
			} catch (final Exception e) {
				throw new CrowdPlusException(CrowdPlusConstants.MESSAGE_STRING_FATALERROR);
			}
		}).httpBasic(Customizer.withDefaults());
		log.info(CrowdPlusConstants.MESSAGE_SPRING_SECURITY);
		return http.build();
	}
}
