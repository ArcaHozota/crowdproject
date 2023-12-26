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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jp.co.sony.ppog.commons.CrowdPlusConstants;
import jp.co.sony.ppog.exception.CrowdPlusException;
import jp.co.sony.ppog.listener.CrowdPlusUserDetailsService;
import jp.co.sony.ppog.utils.CrowdPlusUtils;
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
	private CrowdPlusUserDetailsService crowdPlusUserDetailsService;

	@Bean
	protected AuthenticationManager authenticationManager(final AuthenticationManagerBuilder auth) {
		return auth.authenticationProvider(this.daoAuthenticationProvider()).getObject();
	}

	@Bean
	protected DaoAuthenticationProvider daoAuthenticationProvider() {
		final CrowdPlusDaoAuthenticationProvider provider = new CrowdPlusDaoAuthenticationProvider();
		provider.setUserDetailsService(this.crowdPlusUserDetailsService);
		provider.setPasswordEncoder(new CrowdPlusPasswordEncoder());
		return provider;
	}

	@Bean
	protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		final AntPathRequestMatcher[] pathMatchers = { new AntPathRequestMatcher("/static/**", "GET") };
		http.authorizeHttpRequests(
				authorize -> authorize.requestMatchers(pathMatchers).permitAll().anyRequest().authenticated())
				.csrf(csrf -> csrf.ignoringRequestMatchers(pathMatchers)
						.csrfTokenRepository(new CookieCsrfTokenRepository()))
				.exceptionHandling().authenticationEntryPoint((request, response, authenticationException) -> {
					final ResponseLoginDto responseResult = new ResponseLoginDto(403,
							authenticationException.getMessage());
					CrowdPlusUtils.renderString(response, responseResult);
				}).and().formLogin(formLogin -> {
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
