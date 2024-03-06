package jp.co.sony.ppog.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import jp.co.sony.ppog.commons.CrowdProjectConstants;
import jp.co.sony.ppog.commons.CrowdProjectURLConstants;
import jp.co.sony.ppog.exception.CrowdProjectException;
import jp.co.sony.ppog.listener.CrowdProjectUserDetailsService;
import jp.co.sony.ppog.utils.CrowdProjectUtils;
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
@EnableMethodSecurity
public class WebSecurityConfiguration {

	/**
	 * ログインサービス
	 */
	@Resource
	private CrowdProjectUserDetailsService crowdPlusUserDetailsService;

	@Bean
	protected AuthenticationManager authenticationManager(final AuthenticationManagerBuilder auth) {
		return auth.authenticationProvider(this.daoAuthenticationProvider()).getObject();
	}

	@Bean
	protected DaoAuthenticationProvider daoAuthenticationProvider() {
		final CrowdProjectDaoAuthenticationProvider provider = new CrowdProjectDaoAuthenticationProvider();
		provider.setUserDetailsService(this.crowdPlusUserDetailsService);
		provider.setPasswordEncoder(new CrowdProjectPasswordEncoder());
		return provider;
	}

	@Bean
	protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				authorize -> authorize.requestMatchers(CrowdProjectURLConstants.URL_STATIC_RESOURCE).permitAll()
						.requestMatchers(CrowdProjectURLConstants.URL_EMPLOYEE_TO_PAGES,
								CrowdProjectURLConstants.URL_EMPLOYEE_PAGINATION)
						.hasAuthority("employee%retrieve")
						.requestMatchers(CrowdProjectURLConstants.URL_EMPLOYEE_INFOSAVE,
								CrowdProjectURLConstants.URL_EMPLOYEE_INFOUPD,
								CrowdProjectURLConstants.URL_EMPLOYEE_TO_ADDITION,
								CrowdProjectURLConstants.URL_EMPLOYEE_TO_EDITION)
						.hasAuthority("employee%edition").requestMatchers(CrowdProjectURLConstants.URL_EMPLOYEE_DELETE)
						.hasAuthority("employee%delete").requestMatchers(CrowdProjectURLConstants.URL_ROLE_TO_PAGES)
						.hasAuthority("role%retrieve").anyRequest().authenticated())
				.csrf(csrf -> csrf.ignoringRequestMatchers(CrowdProjectURLConstants.URL_STATIC_RESOURCE)
						.csrfTokenRepository(new CookieCsrfTokenRepository()))
				.exceptionHandling().authenticationEntryPoint((request, response, authenticationException) -> {
					final ResponseLoginDto responseResult = new ResponseLoginDto(HttpStatus.UNAUTHORIZED.value(),
							authenticationException.getMessage());
					CrowdProjectUtils.renderString(response, responseResult);
				}).accessDeniedHandler((request, response, accessDeniedException) -> {
					final ResponseLoginDto responseResult = new ResponseLoginDto(HttpStatus.FORBIDDEN.value(),
							CrowdProjectConstants.MESSAGE_SPRINGSECURITY_REQUIREDAUTH);
					CrowdProjectUtils.renderString(response, responseResult);
				}).and().formLogin(formLogin -> {
					formLogin.loginPage("/pgcrowd/employee/login").loginProcessingUrl("/pgcrowd/employee/do/login")
							.defaultSuccessUrl("/pgcrowd/to/mainmenu").permitAll().usernameParameter("loginAcct")
							.passwordParameter("userPswd");
					try {
						formLogin.and().logout(logout -> logout.logoutUrl("/pgcrowd/employee/logout")
								.logoutSuccessUrl("/pgcrowd/employee/login"));
					} catch (final Exception e) {
						throw new CrowdProjectException(CrowdProjectConstants.MESSAGE_STRING_FATAL_ERROR);
					}
				}).httpBasic(Customizer.withDefaults());
		log.info(CrowdProjectConstants.MESSAGE_SPRING_SECURITY);
		return http.build();
	}
}
