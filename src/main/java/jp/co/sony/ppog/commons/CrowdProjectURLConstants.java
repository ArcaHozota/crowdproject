package jp.co.sony.ppog.commons;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * プロジェクトURLコンスタント
 *
 * @author ArkamaHozota
 * @since 6.51
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CrowdProjectURLConstants {

	public static final RequestMatcher URL_STATIC_RESOURCE = new AntPathRequestMatcher("/static/**",
			HttpMethod.GET.toString());

	public static final RequestMatcher URL_EMPLOYEE_TO_PAGES = new AntPathRequestMatcher("/pgcrowd/employee/to/pages",
			HttpMethod.GET.toString());

	public static final RequestMatcher URL_EMPLOYEE_PAGINATION = new AntPathRequestMatcher(
			"/pgcrowd/employee/pagination", HttpMethod.GET.toString());

	public static final RequestMatcher URL_EMPLOYEE_INFOSAVE = new AntPathRequestMatcher("/pgcrowd/employee/infosave",
			HttpMethod.POST.toString());

	public static final RequestMatcher URL_EMPLOYEE_TO_ADDITION = new AntPathRequestMatcher(
			"/pgcrowd/employee/to/addition", HttpMethod.GET.toString());

	public static final RequestMatcher URL_EMPLOYEE_TO_EDITION = new AntPathRequestMatcher(
			"/pgcrowd/employee/to/edition", HttpMethod.GET.toString());

	public static final RequestMatcher URL_EMPLOYEE_INFOUPD = new AntPathRequestMatcher("/pgcrowd/employee/infoupd",
			HttpMethod.PUT.toString());

	public static final RequestMatcher URL_EMPLOYEE_DELETE = new AntPathRequestMatcher("/pgcrowd/employee/delete/**",
			HttpMethod.DELETE.toString());

	public static final RequestMatcher URL_ROLE_TO_PAGES = new AntPathRequestMatcher("/pgcrowd/role/to/pages",
			HttpMethod.GET.toString());
}
